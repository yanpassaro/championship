package com.test.championship.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.championship.domain.*;
import com.test.championship.exception.GameNotExistException;
import com.test.championship.exception.PlayerNotExistException;
import com.test.championship.exception.TeamAlreadyExistException;
import com.test.championship.exception.TeamNotExistException;
import com.test.championship.repository.GameRepository;
import com.test.championship.repository.PlayerRepository;
import com.test.championship.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class ChampionshipService {
    final TeamRepository teamRepository;
    final PlayerRepository playerRepository;
    final GameRepository gameRepository;
    final RabbitTemplate rabbitTemplate;
    final ObjectMapper objectMapper;

    @Transactional
    public void save(TeamDTO teamDTO) throws TeamAlreadyExistException {
        if (teamRepository.existsByName(teamDTO.name())) {
            throw new TeamAlreadyExistException("Team already exist");
        }
        List<Player> players = playerRepository.saveAll(
                teamDTO.players().stream().map(player -> Player.builder()
                        .name(player.name())
                        .position(player.position())
                        .nacionality(player.nacionality())
                        .number(player.number())
                        .age(player.age())
                        .build()).toList()
        );
        log.info("Player saved: {}", players);
        Team team = teamRepository.save(Team.builder()
                .name(teamDTO.name())
                .titles(teamDTO.titles())
                .stadium(teamDTO.stadium())
                .manager(Manager.builder()
                        .name(teamDTO.manager().getName())
                        .nacionality(teamDTO.manager().getNacionality())
                        .age(teamDTO.manager().getAge())
                        .build())
                .players(players)
                .build()
        );
        log.info("Team saved: {}", team);
    }

    public List<Team> findAllTeams() {
        log.info("Fetching all teams");
        return teamRepository.findAll();
    }

    public Team findTeamById(String id) throws TeamNotExistException {
        log.info("Fetching team with id: {}", id);
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
    }

    public List<Team> searchTeam(String name, int page) {
        log.info("Searching team with name: {}", name);
        return teamRepository.findAllByNameContaining(name, PageRequest.of(page, 20))
                .getContent();
    }

    public List<Player> findAllPlayers() {
        log.info("Fetching all players");
        return playerRepository.findAll();
    }

    public Player findPlayerById(String id) throws PlayerNotExistException {
        log.info("Fetching player with id: {}", id);
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotExistException("Player does not exist"));
    }

    public List<Player> searchPlayer(String name, int page) {
        log.info("Searching team with name: {}", name);
        return playerRepository.findAllByNameContaining(name, PageRequest.of(page, 20))
                .getContent();
    }

    @Transactional
    public void deletePlayer(String id) throws PlayerNotExistException {
        log.info("Deleting player with id: {}", id);
        if (!playerRepository.existsById(id)) {
            throw new PlayerNotExistException("Player does not exist");
        }
        playerRepository.deleteById(id);
    }

    @Transactional
    public void insertPlayer(PlayerDTO playerDTO, String teamId) throws TeamNotExistException {
        log.info("Adding player in team with id: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
        Player player = playerRepository.save(Player.builder()
                .name(playerDTO.name())
                .position(playerDTO.position())
                .age(playerDTO.age())
                .nacionality(playerDTO.nacionality())
                .number(playerDTO.number())
                .build()
        );
        team.getPlayers().add(player);
        teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(String id) throws TeamNotExistException {
        log.info("Deleting team with id: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
        playerRepository.deleteAll(team.getPlayers());
        teamRepository.deleteById(id);
    }

    public void updateManager(String id, Manager manager) throws TeamNotExistException {
        log.info("Updating manager with id: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
        team.setManager(manager);
        teamRepository.save(team);
    }

    @Transactional
    public void saveGame(GameDTO gameDTO) throws TeamNotExistException {
        Team homeTeam = teamRepository.findById(gameDTO.home())
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
        Team awayTeam = teamRepository.findById(gameDTO.away())
                .orElseThrow(() -> new TeamNotExistException("Team does not exist"));
        Game game = gameRepository.save(
                Game.builder()
                        .home(homeTeam)
                        .away(awayTeam)
                        .date(gameDTO.date())
                        .stadium(gameDTO.stadium())
                        .awayScore(0)
                        .started(false)
                        .homeScore(0)
                        .finished(false)
                        .build()
        );
        log.info("Saving game: {}", game);
    }

    public List<Game> findAllGames() {
        log.info("Fetching all games");
        return gameRepository.findAll();
    }

    @Transactional
    public void startGame(String id) throws GameNotExistException {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotExistException("Game does not exist"));
        game.setStarted(true);
        gameRepository.save(game);
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 30)
    public void startGame() {
        log.info("Updating championship");
        gameRepository.findAllByStarted(true)
                .forEach(game ->
                        rabbitTemplate.convertAndSend("game", game)
                );
    }

    @Transactional
    @RabbitListener(queues = "score")
    public void finishGame(String message) {
        Game game = objectMapper.convertValue(message, Game.class);
        log.info("Finishing game: {}", game);
        gameRepository.save(game);
    }
}
