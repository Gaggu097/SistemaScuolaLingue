package org.gagandeepsuman.security;

import jakarta.servlet.http.HttpSession;
import org.gagandeepsuman.entity.EntityCredenziali;
import org.springframework.stereotype.Service;

@Service
public class SessionManager {

    public static final String USER_SESSION_ATTRIBUTE = "authenticatedUser";
    public static final String USER_ROLE_ATTRIBUTE = "userRole";

    /**
     * Creates a user session after successful authentication
     *
     * @param session the HTTP session
     * @param username the authenticated username
     * @param role the user role (CLIENTE, GESTORE, SEGRETERIA)
     */
    public void createUserSession(HttpSession session, String username, String role) {
        if (session != null) {
            session.setAttribute(USER_SESSION_ATTRIBUTE, username);
            session.setAttribute(USER_ROLE_ATTRIBUTE, role);
            // Set session timeout to 30 minutes
            session.setMaxInactiveInterval(30 * 60);
        }
    }

    /**
     * Invalidates the user session (logout)
     *
     * @param session the HTTP session to invalidate
     */
    public void invalidateSession(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Checks if the user is authenticated in the current session
     *
     * @param session the HTTP session
     * @return true if user is authenticated, false otherwise
     */
    public boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(USER_SESSION_ATTRIBUTE) != null;
    }

    /**
     * Gets the authenticated username from session
     *
     * @param session the HTTP session
     * @return username if authenticated, null otherwise
     */
    public String getUsername(HttpSession session) {
        if (session != null) {
            return (String) session.getAttribute(USER_SESSION_ATTRIBUTE);
        }
        return null;
    }

    /**
     * Gets the user role from session
     *
     * @param session the HTTP session
     * @return role if authenticated, null otherwise
     */
    public String getUserRole(HttpSession session) {
        if (session != null) {
            return (String) session.getAttribute(USER_ROLE_ATTRIBUTE);
        }
        return null;
    }
}