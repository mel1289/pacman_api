package com.devops.pacman_api.services;

import com.devops.pacman_api.entities.Game;
import com.devops.pacman_api.events.NewGameEvent;
import com.devops.pacman_api.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.util.MimeType;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Game createGame(String player1) {
        String code = UUID.randomUUID().toString().substring(0, 4);
        Game game = new Game(code, player1, null, "waiting");
        return gameRepository.save(game);
    }

    public Game joinGame(String code, String player2) {
        Optional<Game> optionalGame = gameRepository.findByCode(code);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            if (game.getPlayer2() == null) {
                game.setPlayer2(player2);
                game.setStatus("in-progress");
                Game saved = gameRepository.save(game);

                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                headerAccessor.setContentType(MimeType.valueOf("application/json"));

                NewGameEvent notification = new NewGameEvent(code, "GAME_STARTED");

                messagingTemplate.convertAndSend(
                        "/topic/game/" + code,
                        notification,
                        headerAccessor.getMessageHeaders()
                );

                return saved;
            } else {
                throw new IllegalStateException("La partie est déjà pleine.");
            }
        } else {
            throw new IllegalArgumentException("Code de partie invalide.");
        }
    }

    public void finishGame(String code) {
        Optional<Game> optionalGame = gameRepository.findByCode(code);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
                game.setStatus("finished");
                Game saved = gameRepository.save(game);

        } else {
            throw new IllegalArgumentException("Partie introuvable.");
        }
    }

    public void cancelGame(String code) {
        Optional<Game> optionalGame = gameRepository.findByCode(code);
        if (optionalGame.isPresent()) {
            gameRepository.delete(optionalGame.get());
        } else {
            throw new IllegalArgumentException("Aucune partie trouvée pour ce code.");
        }
    }
}
