package com.racer.model;

public class Score {
    private final String playerName, date;
    private final int score;

    public Score(String playerName, int score, String date) { this.playerName = playerName; this.score = score; this.date = date; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getDate() { return date; }
}
