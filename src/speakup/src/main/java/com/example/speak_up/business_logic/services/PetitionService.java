package com.example.speak_up.business_logic.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO;
import com.example.speak_up.business_logic.dto.CreatePetitionDTO;
import com.example.speak_up.business_logic.entity.Email;
import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.entity.ResponsiblePerson;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.business_logic.enums.Category;
import com.example.speak_up.business_logic.enums.LocationScope;
import com.example.speak_up.data_layer.PetitionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetitionService {
    final String SIGNATURESTRING = "signatures";
    private final PetitionRepository repo;

    public PetitionService(PetitionRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Petition createPetition(Petition p) {
        p.setNoOfSigners(0L);
        return repo.save(p);
    }

    public Page<Petition> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Petition findById(Long id) {
        return repo.findByIdWithOwnerAndResponsibles(id)
                .orElseThrow(() -> new EntityNotFoundException("Petition with ID " + id + " not found"));
    }

    public Long checkUserPetitionNum(User user) {
        Long activeCount = repo.countActivePetitionsByUserId(user.getId(), LocalDate.now());
        return activeCount;
    }

    public List<BrowseViewPetitonDTO> getOwnedPetitions(User user) {
        List<Object[]> results = repo.findPetitionsAndSigneeCountByOwnerId(user.getId());
        List<Petition> petitions = new ArrayList<>();

        for (Object[] row : results) {
            Petition petition = (Petition) row[0];
            Long signeeCount = (Long) row[1];
            petition.setNoOfSigners(signeeCount);
            petitions.add(petition);
        }
        List<BrowseViewPetitonDTO> browseViewDtos = petitions.stream()
                .map(BrowseViewPetitonDTO::new)
                .collect(Collectors.toList());
        return browseViewDtos;
    }

    public Petition createPetition(CreatePetitionDTO request) {
        Petition petition = new Petition();
        petition.setTitle(request.getTitle());
        petition.setDescription(request.getDescription());
        petition.setNoOfSigners(0L);
        petition.setTemplateString(request.getTemplateString());
        petition.setEndDate(request.getEndDate());
        return petition;
    }

    @Transactional
    public boolean deletePetition(long id, long userId) {
        try {
            Petition petition = findById(id);
            if (petition.getOwner() == null || !petition.getOwner().getId().equals(userId)) {
                throw new SecurityException("User is not authorized to delete this petition.");
            }
            repo.deleteById(id);
            return !repo.existsById(id);
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Transactional
    public boolean deleteUsersPetitions(Iterable<Long> ids, long userId) {
        try {
            List<Petition> petitions = repo.findAllById(ids);
            boolean allOwned = petitions.stream()
                    .allMatch(p -> p.getOwner() != null && p.getOwner().getId().equals(userId));
            if (!allOwned) {
                throw new SecurityException("User is not authorized to delete one or more petitions.");
            }
            repo.deleteAllById(ids);
            return repo.findAllById(ids).isEmpty();
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public long getNumberOfSignatures(Long petitionId) {
        return repo.countByPetitionIdNative(petitionId);
    }

    @Transactional(readOnly = true)
    public Page<BrowseViewPetitonDTO> getPetitions(Pageable pageable) {
        return repo.findPetitionsSorted(pageable);
    }

    public Page<BrowseViewPetitonDTO> searchPetitionss(String title, LocationScope scope, String location,
            int page, int size, Sort sortParam, Category category, String userName) {
        String titleFilter = (title == null || title.isBlank()) ? null : title;
        String usernamee = (userName == null || userName.isBlank()) ? null : userName;
        String locationFilter = (location == null || location.isBlank()) ? null : location;

        Sort.Order order = sortParam.stream().findFirst()
                .orElse(new Sort.Order(Sort.Direction.DESC, "creationDate"));

        Pageable pageable = PageRequest.of(page, size);

        if (SIGNATURESTRING.equalsIgnoreCase(order.getProperty())) {
            if (order.getDirection().equals(Sort.Direction.ASC)) {
                return repo.findAllOrderBySignaturesAscc(pageable, titleFilter, scope, category, locationFilter, usernamee);
            } else {
                return repo.findAllOrderBySignaturesDescc(pageable, titleFilter, scope, category, locationFilter, usernamee);
            }
        }

        pageable = PageRequest.of(page, size, Sort.by(order.getDirection(), order.getProperty()));
        return repo.findPetitionsByScopeAndLocation(titleFilter, scope, category, locationFilter, usernamee, pageable);
    }

	 public Page<BrowseViewPetitonDTO> searchPetitionssbar(String search, LocationScope scope, String location,
            int page, int size, Sort sortParam, Category category) {
        String titleFilter = (search == null || search.isBlank()) ? null : search;
        String locationFilter = (location == null || location.isBlank()) ? null : location;

        Sort.Order order = sortParam.stream().findFirst()
                .orElse(new Sort.Order(Sort.Direction.DESC, "creationDate"));

        Pageable pageable = PageRequest.of(page, size);

        if (SIGNATURESTRING.equalsIgnoreCase(order.getProperty())) {
            if (order.getDirection().equals(Sort.Direction.ASC)) {
                return repo.findAllOrderBySignaturesAsccbar(pageable, titleFilter, scope, category, locationFilter);
            } else {
                return repo.findAllOrderBySignaturesDesccbar(pageable, titleFilter, scope, category, locationFilter);
            }
        }

        pageable = PageRequest.of(page, size, Sort.by(order.getDirection(), order.getProperty()));
        return repo.findPetitionsByScopeAndLocationbar(titleFilter, scope, category, locationFilter, pageable);
    }

    public Page<BrowseViewPetitonDTO> getPetitionsSorted(int page, int size, String sortParam) {
        String sortField = "creationDate";
        Sort.Direction sortDirection = Sort.Direction.DESC;

        if (sortParam != null && !sortParam.isEmpty()) {
            String[] parts = sortParam.split(",");
            if (parts.length == 2) {
                sortField = parts[0];
                sortDirection = parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            } else if (parts.length == 1) {
                sortField = parts[0];
            }
        }

        if ("signatures".equalsIgnoreCase(sortField)) {
            Pageable pageable = PageRequest.of(page, size);
            if (sortDirection == Sort.Direction.ASC) {
                return repo.findAllOrderBySignaturesAsc(pageable);
            } else {
                return repo.findAllOrderBySignaturesDesc(pageable);
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        return repo.findPetitionsSorted(pageable);
    }

    public void sendPetitionCreatedEmail(String petitionName, Set<ResponsiblePerson> related) {
        String message = "Dear %s, you are receiving this email because you are considered a person of interest when it comes " +
                "to a newly created petition %s. If you regard yourself as non essential in the field that this petition is concerned please " +
                "relieve yourself from further updates and obligations by clicking the provided link bellow! Kind regards from the " +
                "staff at SpeakUp";
        for (ResponsiblePerson person : related) {
            Email email = new Email(person.getEmail(), petitionName,
                    String.format(message, person.getName(), petitionName));
        }
    }

    public void sendEmailRegardingPetition(Petition petition, Set<ResponsiblePerson> related) {
        String message = "Name: %s \n Created by: %s \n %s \n\n Number of signee's: %d";
        Email email = new Email();
        email.setTitle(petition.getTitle());
        email.setMessage(String.format(message, petition.getTitle(), petition.getOwner(), petition.getDescription(),
                petition.getNoOfSigners()));
        for (ResponsiblePerson person : related) {
            email.setReceiver(person.getEmail());
        }
    }
}