package com.miro.testproject.pojo;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    private String username;
    private Integer age;
    private Set<String> roles;
    private String password;

}
