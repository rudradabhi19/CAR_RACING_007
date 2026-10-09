package com.racer.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/** JDBC layer: opens connections using db.properties. */
public final class DBUtil {
    private static String url, user, pass;

    static {
        try (InputStream in = DBUtil.class.getResourceAsStream("/db.properties")) {
            Properties p = new Properties();
            p.load(in);
            url = p.getProperty("url"); user = p.getProperty("user"); pass = p.getProperty("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }

    private DBUtil() { }

    public static Connection get() throws SQLException { return DriverManager.getConnection(url, user, pass); }
}
