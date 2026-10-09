package com.racer.game;

import javax.swing.*;
import java.awt.*;

final class LeaderboardPanel extends Screen {
    private final JPanel rows = new JPanel(new GridLayout(0, 3, 6, 9));
    private final JLabel status = UI.label(" ", 13, UI.MUTED);

    LeaderboardPanel(GameFrame f) {
        add(UI.label("LEADERBOARD", 36, UI.GOLD)); add(UI.gap(8)); add(status); add(UI.gap(14));
        rows.setOpaque(false);
        rows.setAlignmentX(CENTER_ALIGNMENT);
        rows.setMaximumSize(new Dimension(380, 420));
        add(rows);
        add(Box.createVerticalGlue());
        add(UI.button("OPEN IN BROWSER", new Color(0x1e90c8), () -> {
            try { Desktop.getDesktop().browse(new java.net.URI(ApiClient.base + "/leaderboard")); } catch (Exception ignored) { }
        }));
        add(UI.gap(14));
        add(UI.button("BACK", new Color(0x4b4f80), f::showMenu));
    }

    void load() {
        rows.removeAll(); rows.repaint();
        status.setForeground(UI.MUTED); status.setText("Loading...");
        new Thread(() -> {
            String r;
            try { r = ApiClient.get("/leaderboard?format=text"); } catch (Exception e) { r = "ERR|offline"; }
            final String res = r;
            SwingUtilities.invokeLater(() -> fill(res));
        }).start();
    }

    private void fill(String res) {
        rows.removeAll();
        String[] lines = res.split("\\R");
        if (!lines[0].equals("OK")) { status.setForeground(UI.ACCENT); status.setText("Could not load leaderboard"); }
        else {
            status.setText(lines.length == 1 ? "No scores yet - be the first!" : "Top 10 drivers");
            if (lines.length > 1) { cell("#", 0, UI.MUTED); cell("DRIVER", 2, UI.MUTED); cell("BEST", 4, UI.MUTED); }
            for (int i = 1; i < lines.length; i++) {
                String[] p = lines[i].split("\\|");
                if (p.length < 3) continue;
                Color c = i == 1 ? UI.GOLD : i == 2 ? new Color(0xc0c8d8) : i == 3 ? new Color(0xcd7f32) : UI.TEXT;
                if (p[1].equals(ScoreManager.playerName)) c = UI.CYAN;
                cell(p[0], 0, c); cell(p[1], 2, c); cell(p[2], 4, c);
            }
        }
        rows.revalidate(); rows.repaint();
    }

    private void cell(String t, int align, Color c) {
        JLabel l = new JLabel(t, align == 0 ? SwingConstants.CENTER : align == 2 ? SwingConstants.LEFT : SwingConstants.RIGHT);
        l.setForeground(c); l.setFont(new Font("SansSerif", Font.BOLD, 17));
        rows.add(l);
    }
}
