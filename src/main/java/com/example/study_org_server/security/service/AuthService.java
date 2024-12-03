package com.example.study_org_server.security.service;

import com.example.study_org_server.controller.user.AuthenticationResponse;
import com.example.study_org_server.exception.UserNotFoundException;
import com.example.study_org_server.service.user.UserService;
import com.example.study_org_server.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.openapitools.example.model.LoginUserForm;
import org.openapitools.example.model.UserResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManagerWithDB;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    //JWTを作成する際
    public AuthenticationResponse authenticateUser(LoginUserForm loginUserForm){
        if (!userService.isExistUser(loginUserForm.getEmail())) {
            throw new UserNotFoundException(loginUserForm.getEmail());
        }
        Authentication auth = authenticationManagerWithDB.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserForm.getEmail(),
                        loginUserForm.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtUtil.generateJWT(auth);
        String role = getRoleFormAuth(auth);
        return new AuthenticationResponse(role, loginUserForm.getEmail(), jwt);
    }

    public UserResponseDTO getUserDataFromSecurityContext(){

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        return new UserResponseDTO(getRoleFormAuth(auth),auth.getName());
    }

    private String getRoleFormAuth(Authentication auth){
        String role = "";
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority != null && authority.getAuthority() != null) {
                role = authority.getAuthority();
                break;
            }
        }
        return role;
    }
}
