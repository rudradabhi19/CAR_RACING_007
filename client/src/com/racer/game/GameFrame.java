package com.racer.game;

import javax.swing.*;
import java.awt.*;

/** Main window (View). Switches between screens with a CardLayout. */
public final class GameFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final MenuPanel menu;
    private final GamePanel game;
    private final GameOverPanel over;
    private final LeaderboardPanel board;

    GameFrame() {
        super("Street Racer - Endless Highway");
        menu = new MenuPanel(this); game = new GamePanel(this); over = new GameOverPanel(this); board = new LeaderboardPanel(this);
        root.add(new LoginPanel(this), "login"); root.add(menu, "menu"); root.add(game, "game");
        root.add(over, "over"); root.add(board, "board");
        setContentPane(root);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    void goTo(String card) { game.stop(); cards.show(root, card); }

    void showMenu() { menu.refresh(); goTo("menu"); }

    void startGame() {
        goTo("game");
        game.start();
        SwingUtilities.invokeLater(game::requestFocusInWindow);
    }

    void showBoard() { goTo("board"); board.load(); }

    void logout() {
        new Thread(() -> {
            try { ApiClient.get("/logout?client=1"); } catch (Exception ignored) { }
            SwingUtilities.invokeLater(() -> { ScoreManager.playerId = 0; ScoreManager.best = 0; goTo("login"); });
        }).start();
    }

    /** Called when the race ends: show result, then save it through ScoreServlet. */
    void finish(int score) {
        boolean rec = score > ScoreManager.best;
        if (rec) ScoreManager.best = score;
        over.setResult(score, ScoreManager.best, rec);
        goTo("over");
        new Thread(() -> {
            String r;
            try { r = ApiClient.post("/score", "score", String.valueOf(score)); } catch (Exception e) { r = "ERR|Server offline"; }
            final String res = r;
            SwingUtilities.invokeLater(() -> {
                boolean ok = res.startsWith("OK");
                over.setStatus(ok ? "Score saved to leaderboard" : "Could not save score: " + (res.startsWith("ERR|") ? res.substring(4) : "server error"),
                        ok ? UI.GREEN : UI.ACCENT);
            });
        }).start();
    }

    public static void main(String[] args) {
        if (args.length > 0) ApiClient.base = args[0];
        SwingUtilities.invokeLater(() -> new GameFrame().setVisible(true));
    }
}
