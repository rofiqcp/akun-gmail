package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "google.oauth")
public class GoogleOAuthConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authUri;
    private String tokenUri;
    private List<String> scopes;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
    
    public String getAuthorizationUrl() {
        String scopeString = String.join(" ", scopes);
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&access_type=offline&prompt=consent",
            authUri, clientId, redirectUri, scopeString);
    }
}
