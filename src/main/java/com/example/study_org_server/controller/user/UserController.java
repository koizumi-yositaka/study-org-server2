package com.example.study_org_server.controller.user;

import com.example.study_org_server.controller.UserApi;
import com.example.study_org_server.exception.UserNotFoundException;
import com.example.study_org_server.security.service.AuthService;
import com.example.study_org_server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.example.model.LoginUserForm;
import org.openapitools.example.model.UserResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.time.Duration;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final AuthService authService;
    private final UserService userService;

//    @Override
//    public ResponseEntity<UserResponseDTO> apiUserLoginPost(LoginUserForm loginUserForm) {
//        //UserResponseDTO user =authService.authenticateUser(loginUserForm);
//        return ResponseEntity.ok().body(user);
//    }


//    @Override
//    public ResponseEntity<Void> apiUserManagePost() {
//        return ResponseEntity.status(200).build();
//    }


    @Override
    public ResponseEntity<UserResponseDTO> userLoginPost(LoginUserForm loginUserForm) {
        AuthenticationResponse result= authService.authenticateUser(loginUserForm);
        ResponseCookie cookie = ResponseCookie.from("jwt", result.getJwt())
                .httpOnly(true)                // HttpOnly属性を有効にする
                .secure(true)                 // HTTPS通信のみ（開発時はfalseに設定可能）
                .path("/")                    // クッキーが有効なパス
                .maxAge(Duration.ofHours(1))  // 有効期限（1時間）
                .sameSite("Strict")           // CSRF対策
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getUserResponseDTO());
    }

    @Override
    public ResponseEntity<UserResponseDTO> userSignupPost(LoginUserForm loginUserForm) {
        String email = userService.signUp(loginUserForm);
        return ResponseEntity.created(URI.create("users/"+email)).build();
    }

    //パスワードチェンジ
    @Override
    public ResponseEntity<UserResponseDTO> userSignupPut(LoginUserForm loginUserForm) {
        String email = userService.changePassword(loginUserForm);
        //今後制限あるかもしれない

        return ResponseEntity.created(URI.create("users/"+email)).build();

    }

    @Override
    public ResponseEntity<UserResponseDTO> userRoleGet() {
        UserResponseDTO user =authService.getUserDataFromSecurityContext();
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Void> userManagePost() {
        return ResponseEntity.ok().build();
    }
}
