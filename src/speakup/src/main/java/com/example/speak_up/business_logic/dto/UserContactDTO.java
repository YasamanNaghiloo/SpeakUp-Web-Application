package com.example.speak_up.business_logic.dto;


import com.example.speak_up.business_logic.entity.UserInterface;

public class UserContactDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    
    public UserContactDTO(UserInterface u) {
        this.id = u.getId();
        this.name = u.getPublicName();
        this.email = u.getEmail();
        this.phoneNumber = u.getPhoneNumber();
    }
    // EMpty constructor needed for jsonserialization
    public UserContactDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }  

     
}
