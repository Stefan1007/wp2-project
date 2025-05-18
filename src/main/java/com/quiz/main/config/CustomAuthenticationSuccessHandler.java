package com.quiz.main.config;

import com.quiz.main.model.User;
import com.quiz.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        
        // Store the full user entity in the session for easy access in templates
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            
            // Redirect based on role
            // Check for either ROLE_ADMIN or ADMIN (to handle both formats)
            boolean isAdmin = "ROLE_ADMIN".equals(user.getRole()) || "ADMIN".equals(user.getRole());
            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/dashboard");
            }
        } else {
            response.sendRedirect("/dashboard");
        }
    }
} 