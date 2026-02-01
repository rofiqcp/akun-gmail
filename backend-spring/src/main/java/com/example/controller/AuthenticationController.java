package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.config.UserService;
import com.example.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        // For testing purposes, accept any email/password combination
        if (loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty() &&
            loginRequest.getPassword() != null && !loginRequest.getPassword().isEmpty()) {
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", "test-token-" + System.currentTimeMillis());
            response.put("user", new HashMap<String, String>() {{
                put("email", loginRequest.getEmail());
                put("name", "Test User");
            }});
        } else {
            response.put("success", false);
            response.put("message", "Invalid credentials");
        }
        
        return response;
    }
    
    @PostMapping("/google")
    public Map<String, Object> loginWithGoogle(@RequestBody TokenRequest tokenRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // In production, verify Google ID token using Google API client library
            // For now, we'll accept the token and create a session
            
            String googleToken = tokenRequest.getToken();
            if (googleToken != null && !googleToken.isEmpty()) {
                // Extract user info from token (simplified for demo)
                // In production, use: GoogleIdTokenVerifier to verify and decode
                
                // For now, use dummy data - in production this would come from token
                String email = tokenRequest.getEmail() != null ? tokenRequest.getEmail() : "user@gmail.com";
                String name = tokenRequest.getName() != null ? tokenRequest.getName() : "Gmail User";
                String picture = tokenRequest.getPicture() != null ? tokenRequest.getPicture() : "https://lh3.googleusercontent.com/a/default-user";
                
                // Save or update user
                com.example.model.User savedUser = userService.saveOrUpdateUser(email, name, picture);
                
                Map<String, String> user = new HashMap<>();
                user.put("email", savedUser.getEmail());
                user.put("name", savedUser.getName());
                user.put("picture", savedUser.getPicture());
                
                response.put("success", true);
                response.put("message", "Google login successful");
                response.put("token", "google-token-" + System.currentTimeMillis());
                response.put("user", user);
            } else {
                response.put("success", false);
                response.put("message", "Invalid token");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Authentication failed: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            response.put("success", true);
            response.put("count", users.size());
            response.put("users", users);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch users: " + e.getMessage());
        }
        return response;
    }
    
    @PostMapping("/send-verification-email")
    public Map<String, Object> sendVerificationEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            String applicationUrl = request.getOrDefault("applicationUrl", "http://localhost:4200");
            
            User user = userService.findByEmail(email);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            if (user.getEmailVerified()) {
                response.put("success", false);
                response.put("message", "Email already verified");
                return response;
            }
            
            userService.sendVerificationEmail(user, applicationUrl);
            response.put("success", true);
            response.put("message", "Verification email sent to " + email);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/verify-email")
    public Map<String, Object> verifyEmail(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean verified = userService.verifyEmail(token);
            if (verified) {
                response.put("success", true);
                response.put("message", "Email verified successfully!");
            } else {
                response.put("success", false);
                response.put("message", "Invalid or expired verification token");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Verification failed: " + e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/check-verification")
    public Map<String, Object> checkVerification(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            response.put("success", true);
            response.put("email", user.getEmail());
            response.put("emailVerified", user.getEmailVerified());
            response.put("name", user.getName());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
}

class LoginRequest {
    private String email;
    private String password;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
