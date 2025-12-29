package com.example.speak_up.business_logic.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.speak_up.business_logic.enums.LocationScope;
import com.example.speak_up.business_logic.enums.Category;

@Entity
@Table(name = "petitions")
public class Petition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(name = "template_email", columnDefinition = "TEXT")
    private String templateString;

    @Column(nullable = false)
    private int goal;

    @Column(name = "start_date", insertable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_scope", nullable = false, length = 20)
    private LocationScope locationScope;

    @Column(name = "location_country", length = 80)
    private String locationCountry;

    @Column(name = "location_city", length = 80)
    private String locationCity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "u_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
        name = "petition_responsibles",
        joinColumns = @JoinColumn(name = "petition_id"),
        inverseJoinColumns = @JoinColumn(name = "rp_id")
    )
    private Set<ResponsiblePerson> responsible = new HashSet<>();

    @Transient
    private Long noOfSigners = 0L; // not stored in `petitions`, calculated from signees

    public Petition() {}

    public Petition(Long id, String title, String description, String templateString, Set<ResponsiblePerson> responsible,
                    Long noOfSigners, int goal, LocalDateTime creationDate, LocalDate endDate,
                    Category category, LocationScope locationScope, String locationCountry,
                    String locationCity, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.templateString = templateString;
        this.responsible = responsible;
        this.noOfSigners = noOfSigners;
        this.goal = goal;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.category = category;
        this.locationScope = locationScope;
        this.locationCountry = locationCountry;
        this.locationCity = locationCity;
        this.owner = owner;
    }

    // getters and setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getTemplateString() { return templateString; }

    public void setTemplateString(String templateString) { this.templateString = templateString; }

    public Set<ResponsiblePerson> getResponsible() { return responsible; }

    public void setResponsible(Set<ResponsiblePerson> responsible) { this.responsible = responsible; }

    public Long getNoOfSigners() { return noOfSigners; }

    public void setNoOfSigners(Long noOfSigners) { this.noOfSigners = noOfSigners; }

    public int getGoal() { return goal; }

    public void setGoal(int goal) { this.goal = goal; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public LocationScope getLocationScope() { return locationScope; }

    public void setLocationScope(LocationScope locationScope) { this.locationScope = locationScope; }

    public String getLocationCountry() { return locationCountry; }

    public void setLocationCountry(String locationCountry) { this.locationCountry = locationCountry; }

    public String getLocationCity() { return locationCity; }

    public void setLocationCity(String locationCity) { this.locationCity = locationCity; }

    public User getOwner() { return owner; }

    public void setOwner(User owner) { this.owner = owner; }
}
