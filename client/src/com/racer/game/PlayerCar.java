package com.racer.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

final class PlayerCar {
    double x, y;

    Rectangle2D hitbox() { return new Rectangle2D.Double(x - 19, y - 35, 38, 70); }

    void draw(Graphics2D g) {
        g.setColor(new Color(255, 71, 87, 60));
        g.fillOval((int) x - 34, (int) y - 20, 68, 70);
        TrafficCar.drawCar(g, x, y, UI.ACCENT);
    }
}
