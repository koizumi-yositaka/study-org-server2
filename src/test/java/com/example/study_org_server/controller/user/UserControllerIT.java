package com.example.study_org_server.controller.user;

import com.example.study_org_server.StudyOrgServerApplication;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StudyOrgServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIT {

    private static final String emailA ="admin@example.com";
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    static HttpHeaders headers = new HttpHeaders();
    ResponseEntity<Map> response;
    HttpEntity<String> entity;
    @Test
    @Order(1)
    public void signup() throws JSONException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        String loginRequestBody = """
                {
                    "email": "admin@example.com",
                    "password": "passX"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(loginRequestBody, headers);


        response = restTemplate.exchange(
                createURLWithPort("/user/signup"),
                HttpMethod.PUT, entity, Map.class);

        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }
    @Test
    @Order(2)
    public void login() throws JSONException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        String loginRequestBody = """
                {
                    "email": "admin@example.com",
                    "password": "passX"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(loginRequestBody, headers);


        response = restTemplate.exchange(
                createURLWithPort("/user/login"),
                HttpMethod.POST, entity, Map.class);

        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
