package org.gagandeepsuman.web;

import org.gagandeepsuman.security.AuthenticationService;
import org.gagandeepsuman.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SessionManager sessionManager;

    /**
     * Displays the login form
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("title", "Login - Sistema Scuola Lingue");
        return "login";
    }

    /**
     * Processes login form submission
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        // Validate credentials using the authentication service
        if (authenticationService.validateCredentials(username, password)) {
            // Determine user role
            String userRole;
            if (authenticationService.isGestoreUsername(username)) {
                userRole = "GESTORE";
            } else if (authenticationService.isSegreteriaUsername(username)) {
                userRole = "SEGRETERIA";
            } else {
                // For regular users, get role from associated entity (cliente)
                userRole = "CLIENTE";
            }

            // Create session
            HttpSession session = request.getSession(true);
            sessionManager.createUserSession(session, username, userRole);

            // Redirect to appropriate dashboard based on role
            return "redirect:/" + userRole.toLowerCase();
        } else {
            // Invalid credentials
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    /**
     * Processes logout request
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        sessionManager.invalidateSession(session);
        return "redirect:/";
    }
}