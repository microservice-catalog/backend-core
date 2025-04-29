package ru.stepagin.dockins.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieService {

    public void setAuthCookies(String accessToken, String refreshToken, HttpServletResponse response) {
        response.addCookie(createCookie("dockins_access_token", accessToken, 15 * 60));
        response.addCookie(createCookie("dockins_refresh_token", refreshToken, 7 * 24 * 60 * 60));
    }

    public void clearAuthCookies(HttpServletResponse response) {
        response.addCookie(deleteCookie("dockins_access_token"));
        response.addCookie(deleteCookie("dockins_refresh_token"));
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    private Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }
}
