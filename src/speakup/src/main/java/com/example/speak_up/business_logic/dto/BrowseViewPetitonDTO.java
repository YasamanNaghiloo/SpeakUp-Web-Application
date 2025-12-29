package com.example.speak_up.business_logic.dto;

import java.time.LocalDateTime;

import com.example.speak_up.business_logic.entity.Petition;

public class BrowseViewPetitonDTO {
    private Long id;

    private String title;

    private String description;

    private String ownerUserName;

    private long signatures;

    private LocalDateTime creationDate;

    public BrowseViewPetitonDTO(Petition p) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.description = p.getDescription();
        this.ownerUserName = p.getOwner().getUsername();
        this.signatures = p.getNoOfSigners();
        this.creationDate = p.getCreationDate();
    }

    public BrowseViewPetitonDTO(Petition p, long signatureCount) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.description = p.getDescription();
        this.ownerUserName = p.getOwner().getUsername();
        this.signatures = signatureCount;
        this.creationDate = p.getCreationDate();
    }

    public long getSignatures() {
        return signatures;
    }

    public void setSignatures(long signatures) {
        this.signatures = signatures;
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

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
