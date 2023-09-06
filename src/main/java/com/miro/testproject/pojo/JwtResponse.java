package com.miro.testproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
}
