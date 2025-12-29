package com.example.speak_up.business_logic.services;

import org.springframework.stereotype.Service;

import com.example.speak_up.business_logic.entity.CompositeId;
import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.entity.Signee;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.data_layer.SigneeRepository;

@Service
public class SigneeService {
    private SigneeRepository signeeRepository;

    public SigneeService(SigneeRepository signeeRepository) {
        this.signeeRepository = signeeRepository;
    }

    public Boolean isSigned(User u, Petition p) {
        return signeeRepository.existsByUserAndPetition(u, p);
    }

    public boolean saveSignature(Signee s) {
        try {
            signeeRepository.save(s);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR MESSAGE FROM SAVE SIGNATURE: " + e.getMessage());
            return false;
        }
        
    }

    public boolean deleteSignature(CompositeId compositeId) {
        signeeRepository.deleteById(compositeId);
        if (signeeRepository.existsById(compositeId)) {
            return false;
        } else {
            return true;
        }
    }
}