package com.example.speak_up.business_logic.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.speak_up.business_logic.entity.ResponsiblePerson;
import com.example.speak_up.data_layer.ResposibleRepository;

@Service
public class ResponsibleService {

    ResposibleRepository repo;

    public ResponsibleService(ResposibleRepository repo) {
        this.repo = repo;
    }

    public Set<ResponsiblePerson> getResponsible(List<ResponsiblePerson> responsibleDTOs) {
        return responsibleDTOs.stream()
                .map(dto -> {
                    Long id = getResponsiblePersonId(dto); // get the id from existing or create new user
                    return repo.findById(id)
                            .orElseThrow(() -> new RuntimeException("ResponsiblePerson not found for ID: " + id));
                })
                .collect(Collectors.toSet()); // Turn into a Set
    }

    public Long getResponsiblePersonId(ResponsiblePerson responsible) {
        Optional<ResponsiblePerson> optionalPerson = repo.findByEmail(responsible.getEmail());

        ResponsiblePerson responsiblePerson = null;
        if (optionalPerson.isPresent()) {
            responsiblePerson = optionalPerson.get(); // if found, use existing
        } else {
            responsiblePerson = repo.save(responsible); // if not found, save new
        }
        return responsiblePerson.getId();
    }

}
