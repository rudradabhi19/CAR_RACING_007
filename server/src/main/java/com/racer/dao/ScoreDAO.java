package com.racer.dao;

import com.racer.model.Score;
import com.racer.util.DBUtil;
import java.sql.*;
import java.util.*;

public class ScoreDAO {
    public void insert(int playerId, int score) throws SQLException {
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement("INSERT INTO scores(player_id, score) VALUES(?,?)")) {
            ps.setInt(1, playerId); ps.setInt(2, score);
            ps.executeUpdate();
        }
    }

    public int best(int playerId) throws SQLException {
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement("SELECT COALESCE(MAX(score),0) FROM scores WHERE player_id=?")) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    /** Best score per player, highest first. */
    public List<Score> top(int limit) throws SQLException {
        String sql = "SELECT p.player_name, MAX(s.score) AS best, MAX(s.created_at) AS last_played "
                + "FROM scores s JOIN players p ON p.player_id = s.player_id "
                + "GROUP BY p.player_id, p.player_name ORDER BY best DESC, last_played ASC LIMIT ?";
        List<Score> list = new ArrayList<>();
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new Score(rs.getString(1), rs.getInt(2), fmt(rs.getTimestamp(3))));
            }
        }
        return list;
    }

    public List<Score> history(int playerId, int limit) throws SQLException {
        String sql = "SELECT score, created_at FROM scores WHERE player_id=? ORDER BY created_at DESC, score_id DESC LIMIT ?";
        List<Score> list = new ArrayList<>();
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playerId); ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(new Score("", rs.getInt(1), fmt(rs.getTimestamp(2))));
            }
        }
        return list;
    }

    private static String fmt(Timestamp t) { return t == null ? "" : t.toString().substring(0, 16); }
}
