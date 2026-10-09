package com.racer.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/** Road hazard: type 0 = cone, type 1 = barricade. */
final class Obstacle {
    final int lane, type;
    final double x;
    double y;

    Obstacle(int lane, double x, double y, int type) { this.lane = lane; this.x = x; this.y = y; this.type = type; }

    Rectangle2D hitbox() {
        return type == 0 ? new Rectangle2D.Double(x - 14, y - 14, 28, 28) : new Rectangle2D.Double(x - 34, y - 11, 68, 22);
    }

    void draw(Graphics2D g) {
        int cx = (int) x, cy = (int) y;
        if (type == 0) {
            g.setColor(new Color(0x333333)); g.fillRoundRect(cx - 15, cy + 6, 30, 9, 4, 4);
            g.setColor(new Color(0xff7f11)); g.fillPolygon(new int[]{cx - 12, cx + 12, cx}, new int[]{cy + 8, cy + 8, cy - 16}, 3);
            g.setColor(Color.WHITE); g.fillPolygon(new int[]{cx - 7, cx + 7, cx + 4, cx - 4}, new int[]{cy - 2, cy - 2, cy - 9, cy - 9}, 4);
        } else {
            g.setColor(new Color(0x444444));
            g.fillRect(cx - 28, cy + 9, 6, 8); g.fillRect(cx + 22, cy + 9, 6, 8);
            Shape old = g.getClip();
            g.setClip(cx - 34, cy - 9, 68, 18);
            g.setColor(Color.WHITE); g.fillRect(cx - 34, cy - 9, 68, 18);
            g.setColor(UI.ACCENT);
            for (int i = -40; i < 40; i += 20)
                g.fillPolygon(new int[]{cx + i, cx + i + 10, cx + i + 20, cx + i + 10}, new int[]{cy + 9, cy + 9, cy - 9, cy - 9}, 4);
            g.setClip(old);
        }
    }
}
