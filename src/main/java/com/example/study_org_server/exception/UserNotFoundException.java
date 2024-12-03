package com.example.study_org_server.exception;

public class UserNotFoundException extends RuntimeException {
    private String email;
    public UserNotFoundException(String email){
        super(email +" is not Found");
        this.email=email;

    }
}
