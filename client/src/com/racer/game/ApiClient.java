package com.racer.game;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/** Talks to the Servlet layer over HTTP. The JSESSIONID cookie keeps the HttpSession. */
final class ApiClient {
    static String base = "http://localhost:8080/streetracer";
    static { CookieHandler.setDefault(new CookieManager()); }

    static String get(String path) throws IOException { return call("GET", path, null); }

    static String post(String path, String... kv) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            if (sb.length() > 0) sb.append('&');
            sb.append(URLEncoder.encode(kv[i], "UTF-8")).append('=').append(URLEncoder.encode(kv[i + 1], "UTF-8"));
        }
        return call("POST", path, sb.toString());
    }

    private static String call(String method, String path, String body) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(base + path).openConnection();
        c.setConnectTimeout(4000); c.setReadTimeout(8000); c.setRequestMethod(method);
        if (body != null) {
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (OutputStream o = c.getOutputStream()) { o.write(body.getBytes(StandardCharsets.UTF_8)); }
        }
        InputStream in = c.getResponseCode() >= 400 ? c.getErrorStream() : c.getInputStream();
        if (in == null) return "ERR|HTTP " + c.getResponseCode();
        try (InputStream s = in) { return new String(s.readAllBytes(), StandardCharsets.UTF_8).trim(); }
    }
}
