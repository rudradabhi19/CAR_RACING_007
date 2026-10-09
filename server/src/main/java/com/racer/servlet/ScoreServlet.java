package com.racer.servlet;

import com.racer.dao.ScoreDAO;
import com.racer.util.Web;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** Controller: stores a finished race score for the logged-in player (HttpSession). */
@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
    private final ScoreDAO dao = new ScoreDAO();

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("playerId") == null) { Web.text(resp, 401, "ERR|Not logged in - please log in again"); return; }
        int score;
        try { score = Integer.parseInt(req.getParameter("score")); } catch (NumberFormatException e) { score = -1; }
        if (score < 0 || score > 10_000_000) { Web.text(resp, 400, "ERR|Invalid score"); return; }
        try {
            int id = (Integer) s.getAttribute("playerId");
            int prev = dao.best(id);
            dao.insert(id, score);
            Web.text(resp, 200, "OK|" + Math.max(prev, score) + "|" + (score > prev ? 1 : 0));
        } catch (SQLException e) { Web.text(resp, 500, "ERR|Database error"); }
    }
}
