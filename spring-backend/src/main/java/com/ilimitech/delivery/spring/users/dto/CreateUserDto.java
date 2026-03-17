package com.ilimitech.delivery.spring.users.dto;

public class CreateUserDto {
    private String email;
    private String phone;
    private String fullName;
    private String userType;

    public CreateUserDto() {}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}

