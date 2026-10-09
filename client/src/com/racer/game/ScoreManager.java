package com.racer.game;

/** Score + personal-best logic. Static fields hold the logged-in player (client side of HttpSession). */
final class ScoreManager {
    static int playerId, best;
    static String playerName = "";

    private double score;

    void reset() { score = 0; }
    void add(double d) { score += d; }
    int score() { return (int) score; }
    int level() { return Math.min(99, 1 + score() / 400); }
    boolean isNewRecord() { return score() > best; }
}
