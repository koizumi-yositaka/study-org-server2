package com.example.study_org_server.controller.user;

import lombok.Getter;
import org.openapitools.example.model.UserResponseDTO;
@Getter
public class AuthenticationResponse {
    private final UserResponseDTO userResponseDTO;
    private final String jwt;

    public AuthenticationResponse(String role,String email, String jwt) {
        this.userResponseDTO = new UserResponseDTO(role,email);
        this.jwt = jwt;
    }

}