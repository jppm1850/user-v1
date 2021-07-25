package com.junior.pecho.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ApiExceptionBuilder {
    private int category = 0;
    private String systemCode;
    private String description;
    private Throwable cause;

    public ApiExceptionBuilder category(int category) {
            this.category = category;

        return this;
    }

    public ApiExceptionBuilder description(String description) {
        if (StringUtils.isNotBlank(description)) {
            this.description = description;
        }
        return this;
    }

    public ApiExceptionBuilder systemCode(String systemCode) {
        if (StringUtils.isNotBlank(systemCode)) {
            this.systemCode = systemCode;
        }
        return this;
    }


    public ApiExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }



    public ApiException build() {


        return new ApiException(this.systemCode, this.description , this.category!= 0 ? this.category : HttpStatus.INTERNAL_SERVER_ERROR.value(), cause);
    }


    public <T> Mono<T> buildAsMono() {
        return Mono.error(this.build());
    }

    public <T> Flux<T> buildFlux() {
        return Flux.error(this.build());
    }



}
