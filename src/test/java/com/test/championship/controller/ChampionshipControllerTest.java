package com.test.championship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.championship.domain.*;
import com.test.championship.repository.GameRepository;
import com.test.championship.repository.TeamRepository;
import com.test.championship.service.ChampionshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChampionshipControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TeamRepository teamRepository;
    private TeamDTO home;
    private TeamDTO away;

    @BeforeEach
    void setUp() {
        home = new TeamDTO("Manchester City", 10, "Etihad Stadium",
                Manager.builder().name("Pep Guardiola").nacionality("ESP").age(50).build(),
                new ArrayList<>(List.of(new PlayerDTO("Ederson", "GK", "BRA", 30, 31),
                        new PlayerDTO("Nathan Aké", "DF", "HOL", 20, 6),
                        new PlayerDTO("Kevin De Bruyne", "MF", "BEL", 30, 17),
                        new PlayerDTO("Halland", "FW", "NOR", 20, 9)
                )));
        away = new TeamDTO("Liverpool", 10, "Anfield",
                Manager.builder().name("Klop").nacionality("GER").age(50).build(),
                new ArrayList<>(List.of(new PlayerDTO("Alisson", "GK", "BRA", 30, 31),
                        new PlayerDTO("Van Dijk", "DF", "HOL", 30, 3),
                        new PlayerDTO("Moh Salah", "FW", "EGY", 30, 11),
                        new PlayerDTO("Mane", "FW", "GAN", 30, 10)
                )));
    }

    @Test
    void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/championship/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(home)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/championship/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(away)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveGame() throws Exception {
        Team idHome = teamRepository.findByName(home.name());
        Team idAway = teamRepository.findByName(away.name());
        GameDTO game = new GameDTO(idHome.getId(), idAway.getId(), "2023-03-20", "Anfield");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/championship/game/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(game)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}