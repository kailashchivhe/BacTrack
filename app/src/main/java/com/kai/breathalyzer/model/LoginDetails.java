package com.kai.breathalyzer.model;

public class LoginDetails {
    String id;
    String jwtToken;


    public LoginDetails(String id, String jwtToken) {
        this.id = id;
        this.jwtToken = jwtToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

