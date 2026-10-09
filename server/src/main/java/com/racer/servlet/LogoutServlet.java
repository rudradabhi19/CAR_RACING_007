package com.racer.servlet;

import com.racer.util.Web;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        if ("1".equals(req.getParameter("client"))) Web.text(resp, 200, "OK");
        else resp.sendRedirect("login");
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException { doGet(req, resp); }
}
