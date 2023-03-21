package com.test.championship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class ChampionshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChampionshipApplication.class, args);
    }

}
