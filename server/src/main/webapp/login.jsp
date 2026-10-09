<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.racer.util.Web" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Street Racer - Login</title><link rel="stylesheet" href="css/style.css">
<script>
function check(f){
  if(!/^\w{3,20}$/.test(f.username.value)){alert("Username must be 3-20 letters, digits or _");return false;}
  if(f.password.value.length<4){alert("Password must be at least 4 characters");return false;}
  return true;
}
</script></head>
<body><div class="card">
  <h1>STREET RACER</h1><div class="sub">ENDLESS HIGHWAY</div>
  <% String error = (String) request.getAttribute("error"); if (error != null) { %><p class="err"><%= Web.esc(error) %></p><% } %>
  <form method="post" action="login" onsubmit="return check(this)">
    <label>USERNAME</label><input name="username" maxlength="20" autofocus required>
    <label>PASSWORD</label><input name="password" type="password" required>
    <label>DISPLAY NAME (register only)</label><input name="name" maxlength="30">
    <div class="btns"><button name="action" value="login">LOGIN</button><button class="alt" name="action" value="register">REGISTER</button></div>
  </form>
  <p class="muted" style="margin-top:20px"><a class="muted" href="leaderboard">View leaderboard</a></p>
</div></body></html>
