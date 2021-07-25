package com.junior.pecho.exposed.web;

import com.junior.pecho.common.ApiException;
import com.junior.pecho.user.business.UserService;
import com.junior.pecho.user.model.api.UserRequest;
import com.junior.pecho.user.model.api.UserResponse;
import com.junior.pecho.user.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> saveUser(@RequestBody UserRequest request, ServerWebExchange serverWebExchange) {
        String authorization = serverWebExchange.getRequest().getHeaders().get(Constants.AUTHORIZATION).get(0);
        return userService.saveUser(request,authorization);
    }


}
