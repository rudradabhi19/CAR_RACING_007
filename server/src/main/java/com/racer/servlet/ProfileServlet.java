package com.racer.servlet;

import com.racer.dao.ScoreDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** Controller: profile page (best score + recent runs). Requires login. */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final ScoreDAO dao = new ScoreDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("playerId") == null) { resp.sendRedirect("login"); return; }
        try {
            int id = (Integer) s.getAttribute("playerId");
            req.setAttribute("best", dao.best(id));
            req.setAttribute("history", dao.history(id, 20));
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } catch (SQLException e) { throw new ServletException(e); }
    }
}
