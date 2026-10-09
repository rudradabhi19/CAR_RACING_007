package com.racer.game;

import java.awt.Color;
import javax.swing.*;

final class GameOverPanel extends Screen {
    private final JLabel score = UI.label(" ", 28, UI.TEXT), best = UI.label(" ", 24, UI.GOLD),
            record = UI.label(" ", 26, UI.GOLD), status = UI.label(" ", 13, UI.MUTED);

    GameOverPanel(GameFrame f) {
        add(UI.gap(30));
        add(UI.label("GAME OVER", 46, UI.ACCENT)); add(UI.gap(30));
        add(score); add(UI.gap(8)); add(best); add(UI.gap(16)); add(record); add(UI.gap(10)); add(status); add(UI.gap(26));
        add(UI.button("RESTART", UI.ACCENT, f::startGame)); add(UI.gap(14));
        add(UI.button("LEADERBOARD", new Color(0x1e90c8), f::showBoard)); add(UI.gap(14));
        add(UI.button("MAIN MENU", new Color(0x4b4f80), f::showMenu));
    }

    void setResult(int s, int b, boolean rec) {
        score.setText("SCORE : " + s);
        best.setText("BEST : " + b);
        record.setText(rec ? "\u2605 NEW RECORD \u2605" : " ");
        setStatus("Saving score...", UI.MUTED);
    }

    void setStatus(String t, Color c) { status.setForeground(c); status.setText(t); }
}
