package com.test.championship.repository;

import com.test.championship.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
    Page<Team> findAllByNameContaining(String name, PageRequest of);

    boolean existsByName(String name);

    Team findByName(String name);
}