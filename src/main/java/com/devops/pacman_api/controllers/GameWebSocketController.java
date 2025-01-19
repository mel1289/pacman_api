package com.devops.pacman_api.controllers;

import com.devops.pacman_api.events.EndGameEvent;
import com.devops.pacman_api.events.NewGameEvent;
import com.devops.pacman_api.events.PlayerMoveEvent;
import com.devops.pacman_api.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameService gameService;

    @MessageMapping("/start")
    @SendTo("/topic/game-started")
    public NewGameEvent startGame(NewGameEvent notification) {
        return notification;
    }

    @MessageMapping("/position")
    public void handlePosition(PlayerMoveEvent position) {
        String destination = "/topic/pacman/" + position.getCode();
        messagingTemplate.convertAndSend(destination, position);
    }

    @MessageMapping("/ghost")
    public void handleGhost(PlayerMoveEvent position) {
        String destination = "/topic/ghost/" + position.getCode();
        messagingTemplate.convertAndSend(destination, position);
    }

    @MessageMapping("/endGame")
    public void handleEndGame(EndGameEvent endGame) {
        String destination = "/topic/endGame/" + endGame.getCode();
        messagingTemplate.convertAndSend(destination, endGame);
        gameService.finishGame(endGame.getCode());
    }
}
