package com.racer.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/** Computer-controlled car. Each lane has its own constant speed so cars never overlap. */
final class TrafficCar {
    static final Color[] COLORS = {new Color(0x3498db), new Color(0x9b59b6), new Color(0x1abc9c),
            new Color(0xe67e22), new Color(0xecf0f1), new Color(0xf1c40f)};
    final int lane;
    final double v;
    final Color color;
    double x, y;

    TrafficCar(int lane, double x, double y, double v, Color color) {
        this.lane = lane; this.x = x; this.y = y; this.v = v; this.color = color;
    }

    Rectangle2D hitbox() { return new Rectangle2D.Double(x - 19, y - 35, 38, 70); }
    void draw(Graphics2D g) { drawCar(g, x, y, color); }

    static void drawCar(Graphics2D g, double cx, double cy, Color body) {
        int x = (int) cx, y = (int) cy;
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRoundRect(x - 20, y - 34, 46, 80, 18, 18);
        g.setColor(new Color(0x111111));
        g.fillRoundRect(x - 26, y - 26, 6, 18, 3, 3); g.fillRoundRect(x + 20, y - 26, 6, 18, 3, 3);
        g.fillRoundRect(x - 26, y + 14, 6, 18, 3, 3); g.fillRoundRect(x + 20, y + 14, 6, 18, 3, 3);
        g.setPaint(new GradientPaint(x - 23, 0, body.brighter(), x + 23, 0, body.darker()));
        g.fillRoundRect(x - 23, y - 40, 46, 80, 18, 18);
        g.setColor(new Color(0x1b2a41));
        g.fillRoundRect(x - 16, y - 20, 32, 14, 6, 6);
        g.fillRoundRect(x - 16, y + 12, 32, 10, 6, 6);
        g.setColor(body.darker());
        g.fillRoundRect(x - 14, y - 4, 28, 14, 4, 4);
        g.setColor(new Color(0xfff3a0));
        g.fillOval(x - 18, y - 38, 9, 6); g.fillOval(x + 9, y - 38, 9, 6);
        g.setColor(new Color(0xff3b30));
        g.fillRect(x - 19, y + 35, 10, 4); g.fillRect(x + 9, y + 35, 10, 4);
    }
}
