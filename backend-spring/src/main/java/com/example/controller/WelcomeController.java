package com.example.controller;

import com.example.config.GoogleOAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    
    @Autowired
    private GoogleOAuthConfig googleOAuthConfig;

    @GetMapping("/api/welcome")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Gmail Backend is running!");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/api/status")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        response.put("service", "Gmail Backend");
        return response;
    }
    
    @GetMapping("/api/auth/google-url")
    public Map<String, Object> getGoogleAuthUrl() {
        Map<String, Object> response = new HashMap<>();
        
        // Check if OAuth is configured
        if (googleOAuthConfig.getClientId() == null || 
            googleOAuthConfig.getClientId().equals("your-client-id-here")) {
            response.put("configured", false);
            response.put("message", "Google OAuth belum dikonfigurasi. Silakan setup Google Cloud Console terlebih dahulu.");
            response.put("setupInstructions", "https://console.cloud.google.com/apis/credentials");
            return response;
        }
        
        response.put("configured", true);
        response.put("authUrl", googleOAuthConfig.getAuthorizationUrl());
        return response;
    }
}
