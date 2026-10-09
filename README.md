# Street Racer – Endless Highway (CAR_RACING_007)
Advanced Java mini project: Swing game client + Servlets / JSP / JDBC / DAO / MySQL / HttpSession.

```
street-racer/
├── sql/schema.sql        MySQL tables (players, scores)
├── server/               Maven WAR: Servlets, JSP, DAO, JDBC  (runs on Tomcat 9)
└── client/               Swing/AWT game (plain Java, no libraries)
```

## 1. Database
```
mysql -u root -p < sql/schema.sql
```
Edit `server/src/main/resources/db.properties` with your MySQL user/password.

## 2. Server (needs JDK 11+, Maven, Tomcat 9.x)
```
cd server
mvn clean package
```
Copy `target/streetracer.war` into Tomcat's `webapps/` folder and start Tomcat.
Check: http://localhost:8080/streetracer/ (login page). (Tomcat 10+ uses jakarta.*, so use Tomcat 9.)
Eclipse/IntelliJ: import `server` as a Maven project and run on Tomcat 9.

## 3. Game client
```
cd client
./run.sh            (Windows: run.bat)
```
Optional: pass the server URL, e.g. `./run.sh http://192.168.1.5:8080/streetracer`.
Register once, then log in and play.

## Controls
← → steer · ↑ accelerate · ↓ brake / reverse (clamped at bottom boundary) · P pause (WASD also works)

## How it maps to the proposal
- Game loop thread: `GameLoop` (Runnable) → `GamePanel.update()` + repaint
- Login/session: `LoginServlet` + `HttpSession` (playerId, playerName); client keeps the JSESSIONID cookie
- Scores: `ScoreServlet` → `ScoreDAO` → JDBC PreparedStatement → MySQL
- Leaderboard: `LeaderboardServlet` → `leaderboard.jsp` (and plain-text feed for the game)
- Profile/history: `ProfileServlet` → `profile.jsp`
- Passwords stored as salted SHA-256.
