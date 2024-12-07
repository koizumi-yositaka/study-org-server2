package com.example.study_org_server.service;

import com.example.study_org_server.controller.user.AuthenticationResponse;
import com.example.study_org_server.exception.UserNotFoundException;
import com.example.study_org_server.security.service.AuthService;
import com.example.study_org_server.service.user.UserService;
import com.example.study_org_server.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.LoginUserForm;
import org.openapitools.example.model.UserResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;


    private String emailA = "admin@example.com";
    private String jwtSample = "jwt";
    private String roleSample = "A";
    private String pass = "pass";
    private final LoginUserForm loginUserForm = new LoginUserForm(emailA,pass);
    @Nested
    public class Test_authenticateUser{

        @Test
        @DisplayName("存在するユーザ")
        public void authenticateUser_ShouldReturnAuthenticationResponse(){
            //Ar
            Authentication mockAuth = new UsernamePasswordAuthenticationToken(emailA, pass);
            SecurityContextHolder.getContext().setAuthentication(mockAuth);
            when(userService.isExistUser(anyString())).thenReturn(true);
            when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mockAuth);
            when(jwtUtil.generateJWT(any(Authentication.class))).thenReturn(jwtSample);
            AuthenticationResponse expect =new AuthenticationResponse(roleSample, emailA, jwtSample);
            AuthenticationResponse actual =authService.authenticateUser(loginUserForm);

            //Ac As
            Assertions.assertEquals(
                    expect.getUserResponseDTO().getEmail(),
                    actual.getUserResponseDTO().getEmail()
            );
            Assertions.assertEquals(
                    expect.getJwt(),
                    actual.getJwt()
            );
        }
        @Test
        @DisplayName("存在しないユーザ")
        public void authenticateUser_ShouldThrowError(){
            //Ar
            when(userService.isExistUser(anyString())).thenReturn(false);

            Assertions.assertThrows(UserNotFoundException.class,
                    ()->authService.authenticateUser(loginUserForm)
            );
        }
    }

    @Nested
    public class Test_getUserDataFromSecurityContext{


        @Test
        public void getUserDataFromSecurityContext_ShouldReturnCOntextAuth(){
            Authentication mockAuth = new UsernamePasswordAuthenticationToken(emailA, pass);
            SecurityContextHolder.getContext().setAuthentication(mockAuth);
            UserResponseDTO expexct = new UserResponseDTO("A",emailA);
            UserResponseDTO actual = authService.getUserDataFromSecurityContext();
            Assertions.assertEquals(expexct.getEmail(),actual.getEmail());
        }
                

    }




}
