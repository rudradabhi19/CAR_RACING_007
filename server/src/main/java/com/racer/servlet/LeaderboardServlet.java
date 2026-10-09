package com.racer.servlet;

import com.racer.dao.ScoreDAO;
import com.racer.model.Score;
import com.racer.util.Web;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** Controller: top-10 ranking. ?format=text for the game client, otherwise leaderboard.jsp. */
@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    private final ScoreDAO dao = new ScoreDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Score> top = dao.top(10);
            if ("text".equals(req.getParameter("format"))) {
                StringBuilder sb = new StringBuilder("OK");
                for (int i = 0; i < top.size(); i++) {
                    Score s = top.get(i);
                    sb.append('\n').append(i + 1).append('|').append(s.getPlayerName()).append('|').append(s.getScore()).append('|').append(s.getDate());
                }
                Web.text(resp, 200, sb.toString());
            } else {
                req.setAttribute("scores", top);
                req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
            }
        } catch (SQLException e) { throw new ServletException(e); }
    }
}
