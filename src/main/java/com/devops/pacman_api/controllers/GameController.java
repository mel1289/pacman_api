package com.devops.pacman_api.controllers;

import com.devops.pacman_api.entities.Game;
import com.devops.pacman_api.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestParam String player1) {
        return ResponseEntity.ok(gameService.createGame(player1));
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinGame(@RequestParam String code, @RequestParam String player2) {
        try {
            Game game = gameService.joinGame(code, player2);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Une erreur inconnue s'est produite.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelGame(@RequestParam String code) {
        try {
            gameService.cancelGame(code);
            return ResponseEntity.ok("Partie annulée");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
