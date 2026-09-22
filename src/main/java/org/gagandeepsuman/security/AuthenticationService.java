package org.gagandeepsuman.security;

import org.gagandeepsuman.dao.CredenzialiDAO;
import org.gagandeepsuman.entity.EntityCredenziali;
import org.gagandeepsuman.util.PasswordSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final CredenzialiDAO credenzialiDAO;

    // Gestore credentials (in a real app, these would be in a database or secure properties)
    @Value("${gestore.username:gestore_admin}")
    private String gestoreUsername;
    @Value("${gestore.password:Gest0reP@ss}")
    private String gestorePassword;

    // Segreteria credentials
    @Value("${segreteria.username:segreteria_admin}")
    private String segreteriaUsername;
    @Value("${segreteria.password:Segre3t@r1a}")
    private String segreteriaPassword;

    public AuthenticationService(CredenzialiDAO credenzialiDAO) {
        this.credenzialiDAO = credenzialiDAO;
    }

    /**
     * Validates user credentials
     * Supports:
     * 1. Regular clienti via EntityCredenziali
     * 2. Gestori via hardcoded credentials (for simplicity in this educational project)
     * 3. Segreteria via hardcoded credentials (for simplicity in this educational project)
     *
     * @param username the username to validate
     * @param password the plain text password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }

        // Check for gestore login
        if (username.equals(gestoreUsername) && password.equals(gestorePassword)) {
            return true;
        }

        // Check for segreteria login
        if (username.equals(segreteriaUsername) && password.equals(segreteriaPassword)) {
            return true;
        }

        // For regular clienti, validate via EntityCredenziali
        EntityCredenziali credenziali = credenzialiDAO.findByUsername(username);
        if (credenziali == null) {
            return false;
        }

        // Verify password using existing PasswordSecurity utility
        return PasswordSecurity.verifyPassword(password, credenziali.getPassword());
    }

    /**
     * Retrieves user credentials by username
     * Only works for regular clienti (gestori/segreteria don't have EntityCredenziali entries)
     *
     * @param username the username to search for
     * @return EntityCredenziali if found (for clienti), null otherwise
     */
    public EntityCredenziali getCredentialsByUsername(String username) {
        // Only return credentials for non-gestore, non-segreteria users
        if (isGestoreUsername(username) || isSegreteriaUsername(username)) {
            return null;
        }
        return credenzialiDAO.findByUsername(username);
    }

    /**
     * Checks if username corresponds to a gestore
     */
    public boolean isGestoreUsername(String username) {
        return username != null && username.equals(gestoreUsername);
    }

    /**
     * Checks if username corresponds to a segreteria
     */
    public boolean isSegreteriaUsername(String username) {
        return username != null && username.equals(segreteriaUsername);
    }
}