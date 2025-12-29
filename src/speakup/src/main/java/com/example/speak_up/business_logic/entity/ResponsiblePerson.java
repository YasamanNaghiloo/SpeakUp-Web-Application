package com.example.speak_up.business_logic.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "responsible_persons")
public class ResponsiblePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resp_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 80)
    private String email;

    private String title;

    @Column(length = 20)
    private String phone;

    public ResponsiblePerson() {
    }

    public ResponsiblePerson(Long id, String title, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.title = title;
    }

    public ResponsiblePerson(String name, String title, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.title = title;
    }

    // getters and setters

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

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
