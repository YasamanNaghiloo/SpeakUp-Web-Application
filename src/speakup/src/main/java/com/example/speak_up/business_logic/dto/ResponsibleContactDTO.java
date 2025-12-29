package com.example.speak_up.business_logic.dto;

import com.example.speak_up.business_logic.entity.ResponsiblePerson;

public class ResponsibleContactDTO {
private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String title;
    
    public ResponsibleContactDTO(ResponsiblePerson r) {
        this.id = r.getId();
        this.name = r.getName();
        this.email = r.getEmail();
        this.phoneNumber = r.getPhone();
        this.title = r.getTitle();
    }
    // EMpty constructor needed for jsonserialization
    public ResponsibleContactDTO() {}

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
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }  

}
