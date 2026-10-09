package com.racer.servlet;

import com.racer.dao.*;
import com.racer.model.Player;
import com.racer.util.Web;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** Controller: login + register. Browser form (JSP) or Swing client (client=1 -> plain text). */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final PlayerDAO players = new PlayerDAO();
    private final ScoreDAO scores = new ScoreDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean client = "1".equals(req.getParameter("client"));
        String action = req.getParameter("action");
        String u = trim(req.getParameter("username")), p = req.getParameter("password") == null ? "" : req.getParameter("password");
        String n = trim(req.getParameter("name")).replaceAll("[|\\r\\n]", "");
        if (n.length() > 30) n = n.substring(0, 30);

        String err = null;
        Player player = null;
        int best = 0;
        if (!u.matches("\\w{3,20}")) err = "Username must be 3-20 letters, digits or _";
        else if (p.length() < 4) err = "Password must be at least 4 characters";
        else {
            try {
                if ("register".equals(action)) {
                    player = players.register(n.isEmpty() ? u : n, u, p);
                    if (player == null) err = "Username already taken";
                } else {
                    player = players.authenticate(u, p);
                    if (player == null) err = "Invalid username or password";
                }
                if (player != null) best = scores.best(player.getId());
            } catch (SQLException e) { err = "Database error: " + e.getMessage(); }
        }

        if (err != null) {
            if (client) Web.text(resp, 200, "ERR|" + err.replace('|', ' ').replaceAll("[\\r\\n]+", " "));
            else { req.setAttribute("error", err); doGet(req, resp); }
            return;
        }
        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();                       // avoid session fixation
        HttpSession s = req.getSession(true);
        s.setAttribute("playerId", player.getId());
        s.setAttribute("playerName", player.getName());
        if (client) Web.text(resp, 200, "OK|" + player.getId() + "|" + player.getName() + "|" + best);
        else resp.sendRedirect("profile");
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
}
