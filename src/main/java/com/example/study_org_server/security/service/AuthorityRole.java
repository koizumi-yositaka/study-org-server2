package com.example.study_org_server.security.service;


public class AuthorityRole {
    public static enum Role{
        ROOT_USER("A"),
        SUPER_USER("B"),
        NORMAL_USER("C")
        ;
        private final String authEnum;

        private Role(String authEnum) {
            this.authEnum = authEnum;
        }

        public String getRole(){
            return this.authEnum;
        }
    }

    public static Role getRole(String role){
        Role[] types = Role.values();
        for (Role type : types) {
            if (type.getRole().equals(role)) {
                return type;
            }
        }
        return null;
    }








}
