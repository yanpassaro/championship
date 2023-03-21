package com.test.championship.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document("teamns")
@Data
@Builder
public class Team {
    @Id
    private String id;
    private String name;
    private int titles;
    private String stadium;
    private Manager manager;
    @DocumentReference
    private List<Player> players;
}
