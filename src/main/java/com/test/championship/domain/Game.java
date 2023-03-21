package com.test.championship.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("gamesns")
@Data
@Builder
public class Game {
    @Id
    private String id;
    @DocumentReference
    private Team home;
    @DocumentReference
    private Team away;
    private int homeScore;
    private int awayScore;
    private String date;
    private String stadium;
    private boolean started;
    private boolean finished;
}
