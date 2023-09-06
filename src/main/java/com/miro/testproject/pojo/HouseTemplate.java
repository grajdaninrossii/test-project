package com.miro.testproject.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
public class HouseTemplate {

    private String address;
    private Set<Long> residents;
}
