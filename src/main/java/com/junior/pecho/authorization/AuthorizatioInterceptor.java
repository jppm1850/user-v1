package com.junior.pecho.authorization;


import com.junior.pecho.common.ApiException;
import com.junior.pecho.user.model.jpa.TokenAuthorization;
import com.junior.pecho.user.dao.repository.TokenAuthorizationRepository;
import com.junior.pecho.user.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizatioInterceptor implements WebFilter {

    @Autowired
    private TokenAuthorizationRepository repository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authorization = exchange.getRequest().getHeaders().get(Constants.AUTHORIZATION).get(0);

        return repository.findByToken(authorization)
                .defaultIfEmpty(new TokenAuthorization())
                .flatMap(auth -> {
                    if (!authorization.equals(auth.getToken())) {
                        return Mono.error(ApiException.builder().systemCode("PE1003")
                                .category(HttpStatus.UNAUTHORIZED.value())
                                .description("Error token Incorrecto")
                                .build());
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(ex->Mono.error(ex));

    }
}
