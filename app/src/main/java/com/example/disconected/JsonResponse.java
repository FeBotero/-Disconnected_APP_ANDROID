package com.example.disconected;

public class JsonResponse {
    private boolean isActive;
    private String password;

    public JsonResponse(boolean isActive, String pass) {
        this.isActive = isActive;
        this.password = pass;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

