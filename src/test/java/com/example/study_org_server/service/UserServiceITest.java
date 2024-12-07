package com.example.study_org_server.service;

import com.example.study_org_server.Config.TestMyCacheManagerConfiguration;
import com.example.study_org_server.repository.user.UserRecord;
import com.example.study_org_server.repository.user.UserRepository;
import com.example.study_org_server.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.LoginUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableCaching
@SpringBootTest(classes = {UserService.class, TestMyCacheManagerConfiguration.class})
public class UserServiceITest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private CacheManager cacheManager;

    // ★★★ テスト実施前後にキャッシュをクリアする
    @BeforeEach
    @AfterEach
    void clearCache() {

        cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }


    private String emailA = "admin@example.com";
    private String emailDummy = "dummy@example.com";

    private String pass = "pass";
    private final LoginUserForm loginUserForm = new LoginUserForm(emailA,pass);
    private final LoginUserForm loginUserForm_2 = new LoginUserForm(emailA,pass+"CHANGED");
    private final LoginUserForm loginUserFormNotExist = new LoginUserForm(emailDummy,pass);


    @Test
    public void loadUserByUsername_ShouldUseCache() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(
                new UserRecord(null, "admin@example.com", "password", "A", null, null)));

        // 初回呼び出し（キャッシュされる）
        userService.loadUserByUsername("admin@example.com");
        // 2回目以降はキャッシュを使用
        userService.loadUserByUsername("admin@example.com");

        // リポジトリが1回しか呼び出されていないことを検証
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    public void changePassword_ShouldEvictCache() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(
                new UserRecord(null, "admin@example.com", "password", "A", null, null)));
        var xx = cacheManager.getCache("getUser");
        // 初回呼び出し（キャッシュされる）
        userService.loadUserByUsername("admin@example.com");
        xx = cacheManager.getCache("getUser");
        userService.changePassword(loginUserForm_2);
        xx = cacheManager.getCache("getUser");

        // 2回目以降はキャッシュを使用しない
        userService.loadUserByUsername("admin@example.com");
        xx = cacheManager.getCache("getUser");

        // リポジトリが1回しか呼び出されていないことを検証
        verify(userRepository, times(3)).findByEmail("admin@example.com");
    }
}