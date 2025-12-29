package com.example.speak_up.business_logic.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.speak_up.business_logic.entity.ResponsiblePerson;
import com.example.speak_up.business_logic.enums.Category;
import com.example.speak_up.business_logic.enums.LocationScope;

public class CreatePetitionDTO {

    private String title;
    private String description;
    private String templateString;
    private int goal;
    private LocalDate endDate;

    private Category category;
    private LocationScope locationScope;
    private String locationCountry;
    private String locationCity;
    private Long userId;

    private List<ResponsiblePerson> responsible; 

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateString() {
        return templateString;
    }

    public void setTemplateString(String templateString) {
        this.templateString = templateString;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocationScope getLocationScope() {
        return locationScope;
    }

    public void setLocationScope(LocationScope locationScope) {
        this.locationScope = locationScope;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public List<ResponsiblePerson> getResponsible() {
        return responsible;
    }

    public void setResponsible(List<ResponsiblePerson> responsible) {
        this.responsible = responsible;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
