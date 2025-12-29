package com.example.speak_up.business_logic.entity;

import java.io.Serializable;
import java.util.Objects;

public class CompositeId implements Serializable {
    private Long user;
    private Long petition;

     public CompositeId() {
    }

    public CompositeId(Long user, Long petition) {
        this.user = user;
        this.petition = petition;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getPetition() {
        return petition;
    }

    public void setPetition(Long petition) {
        this.petition = petition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeId)) return false;
        CompositeId other = (CompositeId) o;
        return Objects.equals(user, other.user) &&
               Objects.equals(petition, other.petition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, petition);
    }
}
