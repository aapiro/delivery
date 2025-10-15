package com.example;

public class DashboardOverview {
    private String message;
    
    public DashboardOverview(String message) {
        this.message = message;
    }
    
    // Getter
    public String getMessage() {
        return message;
    }
    
    // Setter
    public void setMessage(String message) {
        this.message = message;
    }
}