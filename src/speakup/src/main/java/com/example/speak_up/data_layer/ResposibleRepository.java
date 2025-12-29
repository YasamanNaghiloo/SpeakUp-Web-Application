package com.example.speak_up.data_layer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speak_up.business_logic.entity.ResponsiblePerson;

public interface ResposibleRepository extends JpaRepository<ResponsiblePerson, Long>{
    Optional<ResponsiblePerson> findByEmail(String email);

}
