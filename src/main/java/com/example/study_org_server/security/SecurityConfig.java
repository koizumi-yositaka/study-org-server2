package com.example.study_org_server.security;

import com.example.study_org_server.security.filter.JwtAuthenticationFilter;
import com.example.study_org_server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf->csrf.disable())
            // Jwtを使用する場合は、リクエストごとにセッションを確立する必要はない。
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // "/api/auth/**"は認証時に使用するエンドポイントのため許可、
            // その他のエンドポイントへのリクエストは認証が必要とした。
            .authorizeHttpRequests(auth->auth.requestMatchers("/user/login","/user/signup").permitAll()
                    .anyRequest().authenticated())
            // jwtAuthenticationFilterを先に配置することで、リクエストごとにJWTを解析して認証を済ませることができる。
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManagerWithDB(HttpSecurity http)throws Exception{
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(daoAuthenticationProviderWithDB());
        return builder.build();
    }


    @Bean
    DaoAuthenticationProvider daoAuthenticationProviderWithDB(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }


}
