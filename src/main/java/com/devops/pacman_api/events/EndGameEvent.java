package com.devops.pacman_api.events;

public class EndGameEvent {
    private String code;
    private String winner;

    public EndGameEvent() { }

    public EndGameEvent(String code, String winner) {
        this.code = code;
        this.winner = winner;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}

