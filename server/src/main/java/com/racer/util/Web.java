package com.racer.util;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public final class Web {
    private Web() { }

    /** HTML-escape for JSP output. */
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    /** Plain-text reply used by the Swing client. */
    public static void text(HttpServletResponse r, int status, String body) throws IOException {
        r.setStatus(status);
        r.setContentType("text/plain;charset=UTF-8");
        r.getWriter().print(body);
    }
}
