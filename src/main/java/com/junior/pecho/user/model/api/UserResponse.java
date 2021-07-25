package com.junior.pecho.user.model.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {

    private String id;
    private LocalDate created;
    private LocalDate modified;
    private LocalDateTime lastLogin;
    private String token;
    private Boolean active;
}
