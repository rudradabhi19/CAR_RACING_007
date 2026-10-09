<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,com.racer.model.Score,com.racer.util.Web" %>
<%
  List<Score> history = (List<Score>) request.getAttribute("history");
  if (history == null) { response.sendRedirect("profile"); return; }
%>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Street Racer - Profile</title><link rel="stylesheet" href="css/style.css"></head>
<body><div class="card">
  <nav><a class="btn alt" href="leaderboard">Leaderboard</a><a class="btn alt" href="profile">My Profile</a><a class="btn" href="logout">Logout</a></nav>
  <h2><%= Web.esc((String) session.getAttribute("playerName")) %></h2>
  <p class="muted">PERSONAL BEST</p><div class="big"><%= request.getAttribute("best") %></div>
  <h2 style="margin-top:26px;font-size:1.1rem">RECENT RUNS</h2>
  <% if (history.isEmpty()) { %><p class="muted">No runs yet - start the game and drive!</p><% } else { %>
  <table><tr><th>SCORE</th><th>DATE</th></tr>
  <% for (Score s : history) { %><tr><td><%= s.getScore() %></td><td><%= s.getDate() %></td></tr><% } %></table><% } %>
</div></body></html>
