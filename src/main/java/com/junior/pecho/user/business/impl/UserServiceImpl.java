package com.junior.pecho.user.business.impl;

import com.junior.pecho.user.business.UserService;
import com.junior.pecho.user.model.api.PhoneRequest;
import com.junior.pecho.user.model.api.UserRequest;
import com.junior.pecho.user.model.api.UserResponse;
import com.junior.pecho.user.model.jpa.Phone;
import com.junior.pecho.user.model.jpa.User;
import com.junior.pecho.user.model.jpa.UserSession;
import com.junior.pecho.user.dao.repository.PhoneRepository;
import com.junior.pecho.user.dao.repository.UserRepository;
import com.junior.pecho.user.dao.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserResponse> saveUser(UserRequest request, String authorizathion) {
        return Mono.fromCallable(() ->
                User.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build())
                .flatMap(userRepository::save)
                .flatMap(user -> phoneRepository.saveAll(buildPhoneEntity(request.getPhones(), user))
                        .collectList()
                        .flatMap(phones -> userSessionRepository.save(buildPUserSessionEntity(user,authorizathion))
                                .map(userSession ->
                                        UserResponse.builder()
                                                .active(user.getEnabled())
                                                .created(user.getCreated())
                                                .modified(user.getModified())
                                                .build())
                        )
                );
    }

    private List<Phone> buildPhoneEntity(List<PhoneRequest> phoneRequests, User user) {
        return phoneRequests.stream()
                .map(phoneRequest -> Phone.builder()
                        .phoneNumber(phoneRequest.getNumber())
                        .cityCode(phoneRequest.getCityCode())
                        .country(phoneRequest.getCountryCode())
                        .userId(user.getId())
                        .build()
                ).collect(Collectors.toList());
    }

    private UserSession buildPUserSessionEntity(User user, String authorizathion) {
        return UserSession.builder()
                .userId(user.getId())
                .lastLogin(LocalDateTime.now())
                .token(authorizathion)
                .build();
    }
}
