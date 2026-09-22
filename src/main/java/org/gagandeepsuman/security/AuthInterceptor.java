package org.gagandeepsuman.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor to check authentication for protected endpoints
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/", "/login", "/login*", "/css/", "/js/", "/images/", "/favicon.ico",
            "/cliente/corsi", "/cliente/corsi*",  // Allow course consultation without auth
            "/gestore/login", "/gestore/login*",  // Gestore login page
            "/segreteria/login", "/segreteria/login*"  // Segreteria login page
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // Check if the requested endpoint is public
        if (isPublicEndpoint(requestURI)) {
            return true;
        }

        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SessionManager.USER_SESSION_ATTRIBUTE) != null) {
            return true;
        }

        // Not authenticated - redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Add user info to model for views
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute(SessionManager.USER_SESSION_ATTRIBUTE);
            String role = (String) session.getAttribute(SessionManager.USER_ROLE_ATTRIBUTE);
            if (username != null && role != null) {
                if (modelAndView == null) {
                    modelAndView = new ModelAndView();
                }
                modelAndView.addObject("username", username);
                modelAndView.addObject("userRole", role);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cleanup resources if needed
    }

    /**
     * Checks if the request URI matches a public endpoint pattern
     *
     * @param requestURI the request URI to check
     * @return true if public endpoint, false otherwise
     */
    private boolean isPublicEndpoint(String requestURI) {
        for (String pattern : PUBLIC_ENDPOINTS) {
            if (pattern.endsWith("*")) {
                if (requestURI.startsWith(pattern.substring(0, pattern.length() - 1))) {
                    return true;
                }
            } else if (requestURI.equals(pattern)) {
                return true;
            }
        }
        return false;
    }
}