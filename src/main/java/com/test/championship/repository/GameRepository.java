package com.test.championship.repository;

import com.test.championship.domain.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findAllByStarted(boolean started);
}
