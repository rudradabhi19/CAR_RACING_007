package com.racer.game;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

final class UI {
    static final Color BG1 = new Color(0x0b0d1e), BG2 = new Color(0x1d1f45), ACCENT = new Color(0xff4757),
            CYAN = new Color(0x2ed5ff), GOLD = new Color(0xffd32a), TEXT = new Color(0xeceffd),
            MUTED = new Color(0x8b90b8), GREEN = new Color(0x2ed573);

    static void aa(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    static JLabel label(String t, int size, Color c) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        l.setForeground(c);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    static JButton button(String t, Color c, Runnable r) {
        RoundButton b = new RoundButton(t, c);
        b.addActionListener(e -> r.run());
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    static <T extends JTextField> T style(T f) {
        Dimension d = new Dimension(300, 40);
        f.setPreferredSize(d); f.setMaximumSize(d);
        f.setBackground(new Color(0x10122a)); f.setForeground(TEXT); f.setCaretColor(CYAN);
        f.setFont(new Font("SansSerif", Font.PLAIN, 15));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0x3a3e78), 1, true), new EmptyBorder(4, 10, 4, 10)));
        return f;
    }

    static Component gap(int h) { return Box.createRigidArea(new Dimension(0, h)); }

    static void center(Graphics2D g, String t, int cx, int y) {
        g.drawString(t, cx - g.getFontMetrics().stringWidth(t) / 2, y);
    }
}

/** Base screen: gradient background + vertical box layout. */
class Screen extends JPanel {
    Screen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(40, 50, 40, 50));
        setPreferredSize(new Dimension(GamePanel.W, GamePanel.H));
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setPaint(new GradientPaint(0, 0, UI.BG1, 0, getHeight(), UI.BG2));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

class RoundButton extends JButton {
    private final Color base;
    private boolean hover, press;

    RoundButton(String t, Color c) {
        super(t);
        base = c;
        setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
        setFocusable(false); setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Dimension d = new Dimension(300, 52);
        setPreferredSize(d); setMaximumSize(d); setMinimumSize(d);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            public void mouseExited(MouseEvent e) { hover = false; press = false; repaint(); }
            public void mousePressed(MouseEvent e) { press = true; repaint(); }
            public void mouseReleased(MouseEvent e) { press = false; repaint(); }
        });
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        UI.aa(g);
        int w = getWidth(), h = getHeight() - 5, off = press ? 3 : 0;
        Color top = hover ? base.brighter() : base, bot = base.darker();
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(0, 5, w, h, 28, 28);
        g.setPaint(new GradientPaint(0, off, top, 0, h + off, bot));
        g.fillRoundRect(0, off, w, h, 28, 28);
        g.setColor(new Color(255, 255, 255, hover ? 110 : 60));
        g.drawRoundRect(0, off, w - 1, h - 1, 28, 28);
        g.setFont(getFont());
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(getText(), (w - fm.stringWidth(getText())) / 2, off + (h + fm.getAscent() - fm.getDescent()) / 2);
        g.dispose();
    }
}
