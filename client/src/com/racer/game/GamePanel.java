package com.racer.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Draws road, player, traffic and obstacles; holds the game state updated by GameLoop. */
final class GamePanel extends JPanel {
    static final int W = 480, H = 720, ROAD_X = 60, LANES = 4, LANE_W = 90, ROAD_W = LANES * LANE_W;
    static final int BOTTOM = H - 90, TOP = 300;   // reverse boundary / forward limit

    private final GameFrame frame;
    private final ScoreManager sm = new ScoreManager();
    private final PlayerCar player = new PlayerCar();
    private final List<TrafficCar> traffic = new CopyOnWriteArrayList<>();
    private final List<Obstacle> obstacles = new CopyOnWriteArrayList<>();
    private final Random rnd = new Random();
    private volatile boolean left, right, up, down, paused;
    private double speed, lineOffset, spawn;
    private int crash, levelFlash, lastLevel;
    private GameLoop loop;

    GamePanel(GameFrame f) {
        frame = f;
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { key(e.getKeyCode(), true); }
            public void keyReleased(KeyEvent e) { key(e.getKeyCode(), false); }
        });
    }

    private void key(int k, boolean v) {
        switch (k) {
            case KeyEvent.VK_LEFT: case KeyEvent.VK_A: left = v; break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: right = v; break;
            case KeyEvent.VK_UP: case KeyEvent.VK_W: up = v; break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S: down = v; break;
            case KeyEvent.VK_P: if (v) paused = !paused; break;
            default:
        }
    }

    void start() {
        stop();
        traffic.clear(); obstacles.clear(); sm.reset();
        player.x = ROAD_X + ROAD_W / 2.0; player.y = BOTTOM - 60;
        speed = 4; spawn = 0; crash = 0; lastLevel = 1; levelFlash = 0; paused = false;
        left = right = up = down = false;
        loop = new GameLoop(this);
        loop.start();
        requestFocusInWindow();
    }

    void stop() { if (loop != null) loop.stop(); }

    void update() {
        if (paused) return;
        if (crash > 0) {
            if (--crash == 0) {
                loop.stop();
                int s = sm.score();
                SwingUtilities.invokeLater(() -> frame.finish(s));
            }
            return;
        }
        int lvl = sm.level();
        double max = Math.min(16, 7 + lvl * 0.8), cruise = Math.min(max - 1, 4 + lvl * 0.5);
        if (up) speed = Math.min(max, speed + 0.12);
        else if (down) speed = Math.max(1.5, speed - 0.3);
        else speed += speed > cruise ? -0.04 : 0.05;

        if (left) player.x -= 5;
        if (right) player.x += 5;
        player.x = Math.max(ROAD_X + 28, Math.min(ROAD_X + ROAD_W - 28, player.x));
        if (up) player.y -= 2.5;
        if (down) player.y += 3.5;
        if (player.y > BOTTOM) player.y = BOTTOM;      // boundary rule from the proposal
        if (player.y < TOP) player.y = TOP;

        lineOffset = (lineOffset + speed) % 80;
        sm.add(speed * 0.06);
        if (lvl != lastLevel) { lastLevel = lvl; levelFlash = 90; }
        if (levelFlash > 0) levelFlash--;

        for (TrafficCar c : traffic) { c.y += speed - c.v; if (c.y > H + 100 || c.y < -300) traffic.remove(c); }
        for (Obstacle o : obstacles) { o.y += speed; if (o.y > H + 60) obstacles.remove(o); }

        spawn += speed;
        if (spawn > Math.max(230, 460 - lvl * 22)) {
            spawn = 0;
            int a = rnd.nextInt(LANES);
            spawnAt(a);
            if (lvl >= 3 && rnd.nextInt(100) < 20 + lvl * 3) spawnAt((a + 1 + rnd.nextInt(LANES - 1)) % LANES);
        }

        Rectangle2D p = player.hitbox();
        for (TrafficCar c : traffic) if (p.intersects(c.hitbox())) crash = 45;
        for (Obstacle o : obstacles) if (p.intersects(o.hitbox())) crash = 45;
    }

    private void spawnAt(int lane) {
        double x = ROAD_X + lane * LANE_W + LANE_W / 2.0;
        for (TrafficCar c : traffic) if (c.lane == lane && c.y < 170) return;
        for (Obstacle o : obstacles) if (o.lane == lane && o.y < 170) return;
        if (rnd.nextInt(100) < 65)
            traffic.add(new TrafficCar(lane, x, -90, 2.0 + lane * 0.8, TrafficCar.COLORS[rnd.nextInt(TrafficCar.COLORS.length)]));
        else obstacles.add(new Obstacle(lane, x, -50, rnd.nextInt(2)));
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        UI.aa(g);
        int off = (int) lineOffset;
        g.setColor(new Color(0x14532d)); g.fillRect(0, 0, W, H);
        g.setColor(new Color(0x166534));
        for (int y = -80 + off; y < H; y += 80) {
            g.fillRect(0, y, ROAD_X - 14, 40);
            g.fillRect(ROAD_X + ROAD_W + 14, y, W, 40);
        }
        g.setColor(new Color(0x2b2d3a)); g.fillRect(ROAD_X, 0, ROAD_W, H);
        for (int y = -80 + off; y < H; y += 80) {
            g.setColor(UI.ACCENT);
            g.fillRect(ROAD_X - 10, y, 10, 40); g.fillRect(ROAD_X + ROAD_W, y, 10, 40);
            g.setColor(Color.WHITE);
            g.fillRect(ROAD_X - 10, y + 40, 10, 40); g.fillRect(ROAD_X + ROAD_W, y + 40, 10, 40);
        }
        g.setColor(new Color(255, 255, 255, 170));
        for (int i = 1; i < LANES; i++)
            for (int y = -80 + off; y < H; y += 80) g.fillRect(ROAD_X + i * LANE_W - 2, y, 4, 40);

        for (Obstacle o : obstacles) o.draw(g);
        for (TrafficCar c : traffic) c.draw(g);
        player.draw(g);
        if (crash > 0) {
            g.setColor(new Color(255, 40, 40, Math.min(150, crash * 5))); g.fillRect(0, 0, W, H);
            g.setFont(new Font("SansSerif", Font.BOLD, 54)); g.setColor(Color.WHITE);
            UI.center(g, "CRASH!", W / 2, H / 2);
        }
        hud(g);
        g.dispose();
    }

    private void hud(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 175)); g.fillRect(0, 0, W, 64);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(UI.TEXT); g.drawString(String.format("SCORE: %05d", sm.score()), 14, 26);
        String b = String.format("BEST: %05d", Math.max(ScoreManager.best, sm.score()));
        g.setColor(UI.GOLD); g.drawString(b, W - 14 - g.getFontMetrics().stringWidth(b), 26);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(UI.TEXT); g.drawString("SPEED:", 14, 52);
        int seg = (int) Math.round(speed / 16 * 9);
        for (int i = 0; i < 9; i++) {
            g.setColor(i < seg ? (i < 5 ? UI.GREEN : i < 7 ? UI.GOLD : UI.ACCENT) : new Color(255, 255, 255, 40));
            g.fillRect(88 + i * 16, 40, 12, 14);
        }
        String l = String.format("LEVEL: %02d", sm.level());
        g.setColor(UI.CYAN); g.drawString(l, W - 14 - g.getFontMetrics().stringWidth(l), 52);

        g.setColor(new Color(0, 0, 0, 150)); g.fillRoundRect(40, H - 38, W - 80, 28, 14, 14);
        g.setFont(new Font("SansSerif", Font.BOLD, 12)); g.setColor(UI.TEXT);
        UI.center(g, "\u2190 \u2192 STEER     \u2191 ACCEL     \u2193 BRAKE/REVERSE     P PAUSE", W / 2, H - 19);

        if (levelFlash > 0) {
            g.setFont(new Font("SansSerif", Font.BOLD, 44));
            g.setColor(new Color(255, 211, 42, Math.min(255, levelFlash * 5)));
            UI.center(g, "LEVEL " + sm.level(), W / 2, 200);
        }
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150)); g.fillRect(0, 64, W, H - 64);
            g.setFont(new Font("SansSerif", Font.BOLD, 46)); g.setColor(Color.WHITE);
            UI.center(g, "PAUSED", W / 2, H / 2);
        }
    }
}
