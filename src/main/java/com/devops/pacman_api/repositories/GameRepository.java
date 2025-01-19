package com.devops.pacman_api.repositories;

import com.devops.pacman_api.entities.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends MongoRepository<Game, String> {

    Optional<Game> findByCode(String code);

    List<Game> findByStatus(String status);

    List<Game> findByPlayer1OrPlayer2(String player1, String player2);

    @Query("{ 'player1': ?0, 'player2': ?1 }")
    List<Game> findGamesByPlayers(String player1, String player2);

    void deleteByStatus(String status);
}
