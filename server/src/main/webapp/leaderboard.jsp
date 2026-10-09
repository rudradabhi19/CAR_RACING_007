<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,com.racer.model.Score,com.racer.util.Web" %>
<%
  List<Score> scores = (List<Score>) request.getAttribute("scores");
  if (scores == null) { response.sendRedirect("leaderboard"); return; }
  String me = (String) session.getAttribute("playerName");
%>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Street Racer - Leaderboard</title><link rel="stylesheet" href="css/style.css"></head>
<body><div class="card">
  <nav><a class="btn alt" href="leaderboard">Leaderboard</a><a class="btn alt" href="profile">My Profile</a>
  <% if (me == null) { %><a class="btn" href="login">Login</a><% } else { %><a class="btn" href="logout">Logout</a><% } %></nav>
  <h2>&#127942; LEADERBOARD</h2>
  <% if (scores.isEmpty()) { %><p class="muted">No scores yet - be the first!</p><% } else { %>
  <table><tr><th>#</th><th>DRIVER</th><th>BEST SCORE</th><th>LAST PLAYED</th></tr>
  <% for (int i = 0; i < scores.size(); i++) { Score s = scores.get(i);
       String cls = i < 3 ? "r" + (i + 1) : ""; if (s.getPlayerName().equals(me)) cls += " me"; %>
    <tr class="<%= cls %>"><td><%= i + 1 %></td><td><%= Web.esc(s.getPlayerName()) %></td><td><%= s.getScore() %></td><td><%= s.getDate() %></td></tr>
  <% } %></table><% } %>
</div></body></html>
