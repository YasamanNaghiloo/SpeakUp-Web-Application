package com.example.speak_up.business_logic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.speak_up.business_logic.dto.CreateUserDTO;
import com.example.speak_up.business_logic.dto.ViewUserDTO;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.data_layer.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public User findById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Petition with ID " + id + " not found"));
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Invalid email or password"));
    }

    @Transactional
    public String createUser(CreateUserDTO request) {
        System.out.println("Received CreateUserDTO: " + request.getEmail() + ", " + 
                           request.getUsername() + ", " + request.getFirstName() + ", " + 
                           request.getLastName() + ", " + request.getPhoneNumber() + ", " + 
                           request.getPassword());

        try {
            // basic validation
            if (request.getEmail() == null || !request.getEmail().contains("@") || !request.getEmail().contains(".")) {
                System.out.println("Validation failed: Invalid email address.");
                return "Invalid email address.";
            }
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                System.out.println("Validation failed: Username is required.");
                return "Username is required.";
            }
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
                System.out.println("Validation failed: First name is required.");
                return "First name is required.";
            }
            if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
                System.out.println("Validation failed: Last name is required.");
                return "Last name is required.";
            }
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                System.out.println("Validation failed: Password must be at least 6 characters.");
                return "Password must be at least 6 characters.";
            }
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                System.out.println("Validation failed: Phone number is required.");
                return "Phone number is required.";
            }

            System.out.println("Validation passed, attempting to create user...");
            repo.createUser(
                request.getEmail(),
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getPhoneNumber()
            );
            System.out.println("User created successfully.");
            return "success";
        } catch (DataIntegrityViolationException e) {
            System.out.println("DataIntegrityViolationException: " + e.getMessage());
            return "Email already exists.";
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return "Registration failed due to an internal error: " + e.getMessage();
        }
    }

    public boolean deleteUser(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (IllegalArgumentException | org.springframework.dao.OptimisticLockingFailureException e) {
            return false;
        }
    }

    public Long checkCreds(String email, String pwdToCheck) {
        Optional<Long> uID = repo.checkCredentials(email, pwdToCheck);
        if (uID.isPresent()) {
            return uID.get();
        }
        return -1L;
    }

    public ViewUserDTO getUser(Long id) {
        return new ViewUserDTO(repo.getReferenceById(id));
    }
}