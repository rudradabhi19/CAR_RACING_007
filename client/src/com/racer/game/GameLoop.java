package com.racer.game;

/** Game thread: update -> collide -> score -> repaint, repeated ~60 times per second. */
final class GameLoop implements Runnable {
    private final GamePanel panel;
    private volatile boolean alive = true;

    GameLoop(GamePanel p) { panel = p; }

    void start() { Thread t = new Thread(this, "game-loop"); t.setDaemon(true); t.start(); }
    void stop() { alive = false; }

    @Override public void run() {
        while (alive) {
            long t0 = System.nanoTime();
            panel.update();
            panel.repaint();
            long ms = 16 - (System.nanoTime() - t0) / 1_000_000;
            try { Thread.sleep(Math.max(1, ms)); } catch (InterruptedException e) { return; }
        }
    }
}
