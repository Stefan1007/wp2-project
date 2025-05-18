package com.quiz.main.service;

import com.quiz.main.model.User;
import com.quiz.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // If role is stored with ROLE_ prefix, strip it for hasRole()
        String role = user.getRole();
        if (role != null) {
            if (role.startsWith("ROLE_")) {
                // For hasRole() - the ROLE_ prefix is automatically added by Spring Security
                authorities.add(new SimpleGrantedAuthority(role));
                
                // Also add without prefix for possible hasAuthority() checks
                String authority = role.substring("ROLE_".length());
                authorities.add(new SimpleGrantedAuthority(authority));
            } else {
                // If no ROLE_ prefix, add both versions
                authorities.add(new SimpleGrantedAuthority(role));
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
} 