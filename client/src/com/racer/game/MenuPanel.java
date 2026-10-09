package com.racer.game;

import javax.swing.*;
import java.awt.*;

final class MenuPanel extends Screen {
    private final JLabel player = UI.label(" ", 16, UI.MUTED), best = UI.label(" ", 22, UI.GOLD);

    MenuPanel(GameFrame f) {
        add(UI.gap(30));
        add(UI.label("STREET RACER", 42, UI.TEXT));
        add(UI.label("ENDLESS HIGHWAY", 18, UI.CYAN));
        add(UI.gap(14)); add(player); add(UI.gap(34));
        add(UI.button("START", UI.ACCENT, f::startGame)); add(UI.gap(14));
        add(UI.button("LEADERBOARD", new Color(0x1e90c8), f::showBoard)); add(UI.gap(14));
        add(UI.button("LOGOUT", new Color(0x4b4f80), f::logout)); add(UI.gap(26));
        add(best);
    }

    void refresh() {
        player.setText("Driver: " + ScoreManager.playerName);
        best.setText("BEST SCORE: " + ScoreManager.best);
    }

    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        UI.aa(g);
        int h = getHeight();
        TrafficCar.drawCar(g, 90, h - 25, new Color(0x3498db));
        TrafficCar.drawCar(g, 240, h - 5, UI.ACCENT);
        TrafficCar.drawCar(g, 390, h - 40, new Color(0xf1c40f));
    }
}
