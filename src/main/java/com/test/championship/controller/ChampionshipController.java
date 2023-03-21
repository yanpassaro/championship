package com.test.championship.controller;

import com.test.championship.domain.*;
import com.test.championship.exception.GameNotExistException;
import com.test.championship.exception.PlayerNotExistException;
import com.test.championship.exception.TeamAlreadyExistException;
import com.test.championship.exception.TeamNotExistException;
import com.test.championship.service.ChampionshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/championship")
@RequiredArgsConstructor
public class ChampionshipController {
    final ChampionshipService championshipService;

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody @Valid TeamDTO teamDTO)
            throws TeamAlreadyExistException {
        championshipService.save(teamDTO);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Team saved successfully")
                .build());
    }

    @GetMapping("/teams")
    public ResponseEntity<Response> findAll() {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Teams fetched successfully")
                .data(championshipService.findAllTeams())
                .build());
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<Response> findById(@PathVariable String id) throws TeamNotExistException {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Team fetched successfully")
                .data(championshipService.findTeamById(id))
                .build());
    }

    @GetMapping("team/search/{name}")
    public ResponseEntity<Response> search(@PathVariable String name, @RequestParam int page) {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Teams fetched successfully")
                .data(championshipService.searchTeam(name, page))
                .build());
    }

    @GetMapping("/players")
    public ResponseEntity<Response> findAllPlayers() {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Player fetched successfully")
                .data(championshipService.findAllPlayers())
                .build());
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<Response> findPlayerById(@PathVariable String id) throws PlayerNotExistException {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Player fetched successfully")
                .data(championshipService.findPlayerById(id))
                .build());
    }

    @GetMapping("/search/player/{name}")
    public ResponseEntity<Response> searchPlayer(@PathVariable String name, @RequestParam int page) {
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Player fetched successfully")
                .data(championshipService.searchPlayer(name, page))
                .build());
    }

    @DeleteMapping("/player/delete/{id}")
    public ResponseEntity<Response> deletePlayer(@PathVariable String id) throws PlayerNotExistException {
        championshipService.deletePlayer(id);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Player deleted successfully")
                .build());
    }

    @PutMapping("/team/add-player/{id}")
    public ResponseEntity<Response> insertPlayer(@PathVariable String id,
                                                 @RequestBody @Valid PlayerDTO playerDTO)
            throws TeamNotExistException {
        championshipService.insertPlayer(playerDTO, id);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Player added successfully")
                .build());
    }

    @DeleteMapping("/team/delete/{id}")
    public ResponseEntity<Response> deleteTeam(@PathVariable String id) throws TeamNotExistException {
        championshipService.deleteTeam(id);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Team deleted successfully")
                .build());
    }

    @PutMapping("/team/update-manager/{id}")
    public ResponseEntity<Response> updateManager(@PathVariable String id,
                                                  @RequestBody @Valid Manager manager)
            throws TeamNotExistException {
        championshipService.updateManager(id, manager);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Team updated successfully")
                .build());
    }

    @PostMapping("/game/save")
    public ResponseEntity<Response> saveGame(@RequestBody @Valid GameDTO gameDTO)
            throws TeamNotExistException {
        championshipService.saveGame(gameDTO);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Game saved successfully")
                .build());
    }

    @GetMapping("/game/start/{id}")
    public ResponseEntity<Response> startGame(@PathVariable String id)
            throws GameNotExistException {
        championshipService.startGame(id);
        return ResponseEntity.ok(Response.builder()
                .statusCode(200).status(OK)
                .message("Game started successfully")
                .build());
    }

}
