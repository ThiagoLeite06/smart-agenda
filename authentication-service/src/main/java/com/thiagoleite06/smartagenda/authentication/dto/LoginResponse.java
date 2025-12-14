package com.thiagoleite06.smartagenda.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private String email;
    private String fullName;
    private Long expiresIn;

    public LoginResponse(String token, String email, String fullName, Long expiresIn) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.expiresIn = expiresIn;
        this.type = "Bearer";
    }
}
