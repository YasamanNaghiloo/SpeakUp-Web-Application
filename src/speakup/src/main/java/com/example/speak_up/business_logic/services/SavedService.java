package com.example.speak_up.business_logic.services;

import org.springframework.stereotype.Service;

import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.entity.SavedPetition;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.data_layer.SavedRepository;

@Service
public class SavedService {
    private SavedRepository savedRepository;

    public SavedService(SavedRepository savedRepository) {
        this.savedRepository = savedRepository;
    }

    public SavedRepository getSavedRepository() {
        return savedRepository;
    }

    public void setSavedRepository(SavedRepository savedRepository) {
        this.savedRepository = savedRepository;
    }
    
   public boolean savePetition(SavedPetition s) {
        try {
            savedRepository.save(s);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR MESSAGE FROM SAVE SIGNATURE: " + e.getMessage());
            return false;
        }
        
    }

    public Boolean isSaved(User u, Petition p) {
        return savedRepository.existsByUserAndPetition(u, p);
    }
}
