package com.racer.game;

import javax.swing.*;

final class LoginPanel extends Screen {
    private final JTextField server = UI.style(new JTextField(ApiClient.base)), user = UI.style(new JTextField()),
            display = UI.style(new JTextField());
    private final JPasswordField pass = UI.style(new JPasswordField());
    private final JLabel msg = UI.label(" ", 13, UI.ACCENT);

    LoginPanel(GameFrame f) {
        add(UI.label("STREET RACER", 38, UI.TEXT));
        add(UI.label("ENDLESS HIGHWAY", 16, UI.CYAN));
        add(UI.gap(18));
        row("SERVER URL", server); row("USERNAME", user); row("PASSWORD", pass); row("DISPLAY NAME (register only)", display);
        add(UI.gap(4)); add(msg); add(UI.gap(8));
        add(UI.button("LOGIN", UI.ACCENT, () -> submit(f, "login")));
        add(UI.gap(12));
        add(UI.button("REGISTER", new java.awt.Color(0x1e90c8), () -> submit(f, "register")));
        pass.addActionListener(e -> submit(f, "login"));
    }

    private void row(String title, JTextField field) {
        add(UI.label(title, 11, UI.MUTED)); add(UI.gap(3)); add(field); add(UI.gap(8));
    }

    private void submit(GameFrame f, String action) {
        String u = user.getText().trim(), p = new String(pass.getPassword()), n = display.getText().trim();
        ApiClient.base = server.getText().trim().replaceAll("/+$", "");
        msg.setForeground(UI.CYAN); msg.setText("Connecting...");
        new Thread(() -> {
            String r;
            try { r = ApiClient.post("/login", "client", "1", "action", action, "username", u, "password", p, "name", n); }
            catch (Exception e) { r = "ERR|Cannot reach server (" + e.getClass().getSimpleName() + ")"; }
            final String res = r;
            SwingUtilities.invokeLater(() -> {
                String[] x = res.split("\\|", 4);
                if (x[0].equals("OK") && x.length == 4) {
                    ScoreManager.playerId = Integer.parseInt(x[1]);
                    ScoreManager.playerName = x[2];
                    ScoreManager.best = Integer.parseInt(x[3]);
                    pass.setText(""); msg.setText(" ");
                    f.showMenu();
                } else {
                    msg.setForeground(UI.ACCENT);
                    msg.setText(x[0].equals("ERR") && x.length > 1 ? x[1] : "Unexpected server response");
                }
            });
        }).start();
    }
}
