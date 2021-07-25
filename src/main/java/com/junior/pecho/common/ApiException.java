package com.junior.pecho.common;

import lombok.Getter;
import reactor.core.publisher.Mono;


@Getter
public class ApiException extends RuntimeException{


    private final String code;
    private final String description;
    private final int category;

    ApiException(String code, String description,int category,  Throwable cause) {
        super(description, cause);
        this.code = code;
        this.description = description;
        this.category = category;
    }

    public ApiExceptionBuilder mutate() {
        ApiExceptionBuilder builder = builder().cause(this.getCause())
                .category(this.getCategory())
                .systemCode(this.getCode())
                .description(this.getDescription());

        return builder;
    }

    public Mono<ApiException> asMono() {
        return Mono.just(this);
    }

    public static ApiExceptionBuilder builder() {
        return new ApiExceptionBuilder();
    }

}
