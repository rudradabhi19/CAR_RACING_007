package com.racer.dao;

import com.racer.model.Player;
import com.racer.util.DBUtil;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;

public class PlayerDAO {
    /** Creates a player; returns null if the username already exists. */
    public Player register(String name, String username, String password) throws SQLException {
        String sql = "INSERT INTO players(player_name, username, password_hash) VALUES(?,?,?)";
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name); ps.setString(2, username); ps.setString(3, hash(password, newSalt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return new Player(rs.getInt(1), name, username); }
        } catch (SQLIntegrityConstraintViolationException e) { return null; }
    }

    /** Returns the player if credentials are valid, otherwise null. */
    public Player authenticate(String username, String password) throws SQLException {
        String sql = "SELECT player_id, player_name, password_hash FROM players WHERE username=?";
        try (Connection c = DBUtil.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String stored = rs.getString(3);
                String salt = stored.substring(0, stored.indexOf('$'));
                return hash(password, salt).equals(stored) ? new Player(rs.getInt(1), rs.getString(2), username) : null;
            }
        }
    }

    private static String newSalt() { byte[] b = new byte[8]; new SecureRandom().nextBytes(b); return hex(b); }

    private static String hash(String pw, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return salt + "$" + hex(md.digest((salt + pw).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }

    private static String hex(byte[] b) {
        StringBuilder s = new StringBuilder();
        for (byte x : b) s.append(String.format("%02x", x));
        return s.toString();
    }
}
