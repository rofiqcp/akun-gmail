package com.example.controller;

public class TokenRequest {
    private String token;
    private String idToken;
    private String email;
    private String name;
    private String picture;

    public TokenRequest() {
    }

    public TokenRequest(String token, String idToken) {
        this.token = token;
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
