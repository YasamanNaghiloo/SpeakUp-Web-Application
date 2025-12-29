package com.example.speak_up.business_logic.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.enums.Category;
import com.example.speak_up.business_logic.enums.LocationScope;

public class DetailViewPetitionDTO {
    private Long id;
    private String title;
    private String description;
    private String templateString;
    private int goal;
    private LocalDateTime creationDate;
    private LocalDate endDate;
    private Category category;
    private LocationScope locationScope;
    private String locationCountry;
    private String locationCity;
    private UserContactDTO creator;
    private Set<ResponsibleContactDTO> responsible = new HashSet<>();
    private Long noOfSigners = 0L;
    private Long ownerId;

    public DetailViewPetitionDTO(Petition p) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.description = p.getDescription();
        this.templateString = p.getTemplateString();
        this.goal = p.getGoal();
        this.creationDate = p.getCreationDate();
        this.endDate = p.getEndDate();
        this.category = p.getCategory();
        this.locationScope = p.getLocationScope();
        this.locationCountry = p.getLocationCountry();
        this.locationCity = p.getLocationCity();
        this.creator = new UserContactDTO(p.getOwner());
        this.responsible = p.getResponsible().stream()
                        .map(ResponsibleContactDTO::new)
                        .collect(Collectors.toSet());
        this.noOfSigners = p.getNoOfSigners();
        this.ownerId = p.getOwner() != null ? p.getOwner().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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

    public UserContactDTO getCreator() {
        return creator;
    }

    public void setCreator(UserContactDTO creator) {
        this.creator = creator;
    }

    public Set<ResponsibleContactDTO> getResponsible() {
        return responsible;
    }

    public void setResponsible(Set<ResponsibleContactDTO> responsible) {
        this.responsible = responsible;
    }

    public Long getNoOfSigners() {
        return noOfSigners;
    }

    public void setNoOfSigners(Long noOfSigners) {
        this.noOfSigners = noOfSigners;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}