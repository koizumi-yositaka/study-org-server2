package com.example.study_org_server.controller.user;

import com.example.study_org_server.security.SecurityConfig;
import com.example.study_org_server.security.service.AuthService;
import com.example.study_org_server.service.user.UserService;
import com.example.study_org_server.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.LoginUserForm;
import org.openapitools.example.model.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.study_org_server.util.TestUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;


@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
public class UserControlTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private PasswordEncoder passwordEncoder;



    private static final String emailA ="admin@example.com";
    private static final String emailB ="moderator@example.com";
    private static final String emailC ="auser@example.com";
    private static final String role = "A";
    private static final String password = "pass";
    private static final String jwt = "jwt";
    private static final LoginUserForm loginUserForm = new LoginUserForm();
    @BeforeAll
    public static void setUp(){

        loginUserForm.setEmail(emailA);
        loginUserForm.setPassword(password);
    }


    @Nested

    public class TestLogin{
        @Test
        @DisplayName("未ログイン時にuserManagePostは呼べない")
        @WithAnonymousUser
        public void userLoginPost_ShouldReturnUserResponseAndSetJWTInCookie() throws Exception {
            // Arrange
            AuthenticationResponse mockResponse = new AuthenticationResponse(role, emailA,jwt);

            when(authService.authenticateUser(any(LoginUserForm.class)))
                    .thenReturn(mockResponse);

            // Act & Assert
            mockMvc.perform(post("/user/login")
                            .content(asJsonString(loginUserForm))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(cookie().value("jwt", jwt))
                    .andExpect(cookie().httpOnly("jwt", true))
                    .andExpect(cookie().secure("jwt", true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(emailA)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(role)));
        }

    }
    @Nested
    public class TestSignup{
        @Test
        @DisplayName("存在するユーザ")

        public void userSignupPost_ShouldUpdatePassword() throws  Exception{
            //Ar

            when(userService.signUp(any(LoginUserForm.class))).thenReturn(emailA);

            //Ac As
            mockMvc.perform(post("/user/signup")
                            .content(asJsonString(loginUserForm))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
//        @Test
//        @DisplayName("存在しないユーザ")
//        public void userSignupPost_ShouldThrowException() throws  Exception{
//            //Ar
//            Optional<String> mockEmail = Optional.empty();
//            when(userService.signUp(any(LoginUserForm.class))).thenReturn(emailA);
//
//            //Ac As
//            mockMvc.perform(post("/user/signup")
//                            .content(asJsonString(loginUserForm))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(MockMvcResultMatchers.status().isNotFound());
//        }
    }

    @Nested
    public class TestChangePassword{
        @Test
        @DisplayName("存在するユーザ")
        public void userSignupPost_ShouldUpdatePassword() throws  Exception{
            //Ar
            when(userService.changePassword(any(LoginUserForm.class))).thenReturn(emailA);

            //Ac As
            mockMvc.perform(put("/user/signup")
                            .content(asJsonString(loginUserForm))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
//        @Test
//        @DisplayName("存在しないユーザ")
//        public void userSignupPost_ShouldThrowException() throws  Exception{
//            //Ar
//            Optional<String> mockEmail = Optional.empty();
//            when(userService.changePassword(any(LoginUserForm.class))).thenReturn(emailA);
//
//            //Ac As
//            mockMvc.perform(put("/user/signup")
//                            .content(asJsonString(loginUserForm))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(MockMvcResultMatchers.status().isNotFound());
//        }
    }
    @Nested
    public class WithCredential{

        @Test
        @WithMockUser(username = emailA, roles = {role})
        public void userRoleGet_ShouldReturnOK() throws Exception{
            //Ar
            UserResponseDTO user = new UserResponseDTO("A",emailA);
            //Ac As
            mockMvc.perform(get("/user/role")
                            .content(asJsonString(loginUserForm))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @WithMockUser
        public void userManagePost_ShouldReturnOK( ) throws Exception{
            //Ar
            //UserResponseDTO user = new UserResponseDTO("A",emailA);
            //Ac As
            mockMvc.perform(post("/user/manage")
                            .content(asJsonString(loginUserForm))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }

    }




}
