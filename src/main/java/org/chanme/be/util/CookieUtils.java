package org.chanme.be.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class CookieUtils {
    private CookieUtils() {}
    public static final String LOGIN_COOKIE = "CHNME_UID";

    public static void addLoginCookie(HttpServletResponse res, String memberCode) {
//        Cookie c = new Cookie(LOGIN_COOKIE, memberCode);
//        c.setHttpOnly(true);    // JS에서 접근 불가 (보안)
//        c.setPath("/");         // 전체 경로
//        // 개발 중엔 Secure 미설정(HTTP). 운영 HTTPS면 true 권장:
//        // c.setSecure(true);
//        // 유효기간: 30일 (자동로그인)
//        c.setMaxAge(60 * 60 * 24 * 30);
//        res.addCookie(c);

        String encoded = URLEncoder.encode(memberCode, StandardCharsets.UTF_8);
        Cookie c = new Cookie(LOGIN_COOKIE, encoded);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge(60 * 60 * 24 * 30);
        res.addCookie(c);


    }

    public static void clearLoginCookie(HttpServletResponse res) {
        Cookie c = new Cookie(LOGIN_COOKIE, "");
        c.setPath("/");
        c.setMaxAge(0);
        res.addCookie(c);
    }

    public static String resolveMemberCode(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (LOGIN_COOKIE.equals(c.getName())) {
                try {
                    return URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                } catch (Exception ignore) {
                }

                
            }
        }
        return null;
    }
}
