package com.junior.pecho.user.model.jpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("token_authorization")
@Getter
@Setter
public class TokenAuthorization {

    @Id
    private Long id;
    private String token;
}
