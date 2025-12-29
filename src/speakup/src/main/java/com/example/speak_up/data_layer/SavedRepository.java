package com.example.speak_up.data_layer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.entity.SavedPetition;
import com.example.speak_up.business_logic.entity.CompositeId;
import com.example.speak_up.business_logic.entity.User;

public interface SavedRepository extends JpaRepository<SavedPetition, CompositeId>{
    boolean existsByUserAndPetition(User user, Petition petition);
}
