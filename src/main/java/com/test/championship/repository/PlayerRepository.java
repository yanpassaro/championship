package com.test.championship.repository;

import com.test.championship.domain.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Page<Player> findAllByNameContaining(String name, PageRequest of);
}
