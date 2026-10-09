CREATE DATABASE IF NOT EXISTS street_racer;
USE street_racer;

CREATE TABLE IF NOT EXISTS players (
  player_id     INT AUTO_INCREMENT PRIMARY KEY,
  player_name   VARCHAR(50) NOT NULL,
  username      VARCHAR(30) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS scores (
  score_id   INT AUTO_INCREMENT PRIMARY KEY,
  player_id  INT NOT NULL,
  score      INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE,
  INDEX idx_score (score)
);
