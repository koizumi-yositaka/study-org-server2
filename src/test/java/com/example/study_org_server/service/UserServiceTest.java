package com.example.study_org_server.service;

import com.example.study_org_server.repository.user.UserRecord;
import com.example.study_org_server.repository.user.UserRepository;
import com.example.study_org_server.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.LoginUserForm;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;



    private String emailA = "admin@example.com";
    private String emailDummy = "dummy@example.com";
    private String jwtSample = "jwt";
    private String roleSample = "A";
    private String pass = "pass";
    private final LoginUserForm loginUserForm = new LoginUserForm(emailA,pass);
    private final LoginUserForm loginUserFormNotExist = new LoginUserForm(emailDummy,pass);

    @Test
    public void loadUserByUsername_ShouldReturnUserDetails(){
        when(userRepository.findByEmail(emailA)).thenReturn(Optional.of(new UserRecord(null, emailA, "", "A", null, null)));
        UserDetails expect= new org.springframework.security.core.userdetails.User(
                emailA,
                pass,
                Collections.singleton(new SimpleGrantedAuthority("A")) // 権限を設定
        );

        Assertions.assertEquals(
                expect,
                userService.loadUserByUsername(emailA)
        );

    }
    @Test
    public void loadUserByUsername_ShouldThrowError(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,()->userService.loadUserByUsername(emailDummy));
    }

    @Test
    @DisplayName("成功")
    public void signUp_ShouldCallUpdateAndReturnEmailWhenSuccess(){
        when(userRepository.findByEmail(emailA)).thenReturn(Optional.of(new UserRecord(null, emailA, "", "A", null, null)));
        doNothing().when(userRepository).update(anyString(),anyString());
        userService.signUp(loginUserForm);
        verify(userRepository,times(1)).update(anyString(),anyString());
    }

    @Test
    @DisplayName("ユーザ存在しない")
    public void signUp_ShouldReturnEmptyWhenNotFoundUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,()->userService.signUp(loginUserFormNotExist));
        verify(userRepository,times(0)).update(anyString(),anyString());

    }


    @Test
    public void signUp_ShouldThrowErrorWhenIsNotFirstTime(){
        when(userRepository.findByEmail(emailA)).thenReturn(Optional.of(new UserRecord(null, emailA, "Dummy", "A", null, null)));
        Assertions.assertThrows(RuntimeException.class,()-> userService.signUp(loginUserForm));
        verify(userRepository,times(0)).update(anyString(),anyString());
    }


    //changePassword
    @Test
    @DisplayName("成功")
    public void changePassword_ShouldCallUpdateAndReturnEmailWhenSuccess(){
        when(userRepository.findByEmail(emailA)).thenReturn(Optional.of(new UserRecord(null, emailA, "XX", "A", null, null)));
        doNothing().when(userRepository).update(anyString(),anyString());
        userService.changePassword(loginUserForm);
        verify(userRepository,times(1)).update(anyString(),anyString());
    }

    @Test
    @DisplayName("ユーザ存在しない")
    public void changePassword_ShouldReturnEmptyWhenNotFoundUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,()->userService.changePassword(loginUserFormNotExist));
        verify(userRepository,times(0)).update(anyString(),anyString());
    }


    @Test
    public void changePassword_ShouldThrowErrorWhenIsNotFirstTime(){
        when(userRepository.findByEmail(emailA)).thenReturn(Optional.of(new UserRecord(null, emailA, "", "A", null, null)));
        Assertions.assertThrows(RuntimeException.class,()-> userService.changePassword(loginUserForm));
        verify(userRepository,times(0)).update(anyString(),anyString());
    }

    //isExistUser
    @Test
    public void isExistUser_ShouldReturnTrue(){
        when(userRepository.isExistEmail(anyString())).thenReturn(1);
        Assertions.assertTrue(userService.isExistUser(emailA));
    }

    @Test
    public void isExistUser_ShouldReturnFalse(){
        when(userRepository.isExistEmail(anyString())).thenReturn(0);
        Assertions.assertFalse(userService.isExistUser(emailA));
    }
}