package com.test.championship.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("playersns")
@Data
@Builder
public class Player {
    @Id
    private String id;
    private String name;
    private String position;
    private String nacionality;
    private int age;
    private int number;
}
