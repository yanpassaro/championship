package com.test.championship.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Manager {
    private String name;
    private String nacionality;
    private int age;
}
