package com.example.speak_up.business_logic.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.speak_up.business_logic.dto.AddCommentDTO;
import com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO;
import com.example.speak_up.business_logic.dto.CreatePetitionDTO;
import com.example.speak_up.business_logic.dto.DetailViewPetitionDTO;
import com.example.speak_up.business_logic.dto.ShowCommentDTO;
import com.example.speak_up.business_logic.entity.Comment;
import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.entity.ResponsiblePerson;
import com.example.speak_up.business_logic.entity.SavedPetition;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.business_logic.enums.Category;
import com.example.speak_up.business_logic.enums.LocationScope;
import com.example.speak_up.business_logic.services.CommentService;
import com.example.speak_up.business_logic.services.PetitionService;
import com.example.speak_up.business_logic.services.ResponsibleService;
import com.example.speak_up.business_logic.services.SavedService;
import com.example.speak_up.business_logic.services.SigneeService;
import com.example.speak_up.business_logic.services.UserService;
import com.example.speak_up.business_logic.entity.Signee;
import com.example.speak_up.business_logic.entity.CompositeId;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/petitions")
public class PetitionController {

  private final PetitionService petitionService;
  private final UserService userService;
  private final ResponsibleService responsibleService;
  private final ApiController apiController;
  private final SigneeService signeeService;
  private final SavedService savedService;
  private final CommentService commentService;

  public PetitionController(PetitionService petitionService, UserService userService,
      ResponsibleService responsibleService, ApiController apiController, SigneeService signeeService,
      SavedService savedService, CommentService commentService) {
    this.petitionService = petitionService;
    this.userService = userService;
    this.responsibleService = responsibleService;
    this.apiController = apiController;
    this.signeeService = signeeService;
    this.savedService = savedService;
    this.commentService = commentService;
  }

  @GetMapping("/petitions")
  public Page<BrowseViewPetitonDTO> getPetitions(
      @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return petitionService.getPetitions(pageable);
  }

  @GetMapping("/search")
  public Page<BrowseViewPetitonDTO> getPetitions(@RequestParam(required = false) String title,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) LocationScope scope,
      @RequestParam(required = false) Category category,
      @RequestParam(required = false) String location,
      @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return petitionService.searchPetitionss(title, scope, location, pageable.getPageNumber(), pageable.getPageSize(),
        pageable.getSort(), category, username);
  }

  @GetMapping("/searchbar")
  public Page<BrowseViewPetitonDTO> getPetitions(@RequestParam(required = false) String search,
      @RequestParam(required = false) LocationScope scope,
      @RequestParam(required = false) Category category,
      @RequestParam(required = false) String location,
      @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return petitionService.searchPetitionssbar(search, scope, location, pageable.getPageNumber(),
        pageable.getPageSize(), pageable.getSort(), category);
  }

  @GetMapping("/sorted")
  public ResponseEntity<Page<BrowseViewPetitonDTO>> getPetitions(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String sort) {

    Page<BrowseViewPetitonDTO> petitions = petitionService.getPetitionsSorted(page, size, sort);
    return ResponseEntity.ok(petitions);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody CreatePetitionDTO dto) {
    Petition p = new Petition();
    p.setTitle(dto.getTitle());
    p.setDescription(dto.getDescription());
    p.setTemplateString(dto.getTemplateString());
    p.setGoal(dto.getGoal());
    p.setEndDate(dto.getEndDate());
    p.setCategory(dto.getCategory());
    p.setLocationScope(dto.getLocationScope());
    if (dto.getLocationCity() != null) {
      p.setLocationCity(dto.getLocationCity());
    }
    if (dto.getLocationCountry() != null) {
      p.setLocationCountry(dto.getLocationCountry());
    }
    User owner = userService.findById(dto.getUserId());
    Long num = petitionService.checkUserPetitionNum(owner);
    if (num > apiController.MAXPETITIONSPERUSER) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body("User cannot have more than 5 active petitions.");
    }
    p.setOwner(owner);

    Set<ResponsiblePerson> responsible = responsibleService.getResponsible(dto.getResponsible());
    p.setResponsible(responsible);

    Petition savedPetition = petitionService.createPetition(p);
    petitionService.sendPetitionCreatedEmail(p.getTitle(), p.getResponsible());
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPetition);
  }

  @PostMapping("/{petitionId}/sign")
  public ResponseEntity<?> signPetition(@PathVariable Long petitionId, @RequestParam Long userid) {
    User user = userService.findById(userid);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }
    Petition petition = petitionService.findById(petitionId);
    if (petition == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Petition not found.");
    }

    if (petition.getEndDate().isBefore(LocalDate.now())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Petition has ended.");
    }

    boolean alreadySigned = signeeService.isSigned(user, petition);
    if (alreadySigned) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("You already signed this petition.");
    }

    Signee signee = new Signee(user, petition, LocalDateTime.now());
    boolean success = signeeService.saveSignature(signee);
    if (!success) {
      return ResponseEntity.internalServerError().body("Signature could not be saved");
    }
    return ResponseEntity.ok("Petition signed!");
  }

  @DeleteMapping("/{petitionId}/revoke")
  public ResponseEntity<?> revokeSignature(@PathVariable Long petitionId, @RequestParam Long userid) {
    User user = userService.findById(userid);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }
    Petition petition = petitionService.findById(petitionId);
    if (petition == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Petition not found.");
    }

    boolean hasSigned = signeeService.isSigned(user, petition);
    if (!hasSigned) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have not signed this petition.");
    }

    boolean success = signeeService.deleteSignature(new CompositeId(userid, petitionId));
    if (!success) {
      return ResponseEntity.internalServerError().body("Signature could not be revoked");
    }
    return ResponseEntity.ok("Revoked signature");
  }

  @PostMapping("/{petitionId}/save")
  public ResponseEntity<?> savePetition(@PathVariable Long petitionId, @RequestParam Long userid) {
    User user = userService.findById(userid);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }
    Petition petition = petitionService.findById(petitionId);
    if (petition == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Petition not found.");
    }

    boolean alreadySaved = savedService.isSaved(user, petition);
    if (alreadySaved) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("You already saved this petition.");
    }

    SavedPetition savedPetition = new SavedPetition(user, petition, LocalDateTime.now());
    boolean success = savedService.savePetition(savedPetition);
    if (!success) {
      return ResponseEntity.internalServerError().body("Petition could not be saved");
    }
    return ResponseEntity.ok("Petition saved successfully.");
  }

  @PostMapping("/{petitionId}/comments")
  public ResponseEntity<?> addComment(@RequestBody AddCommentDTO dto) {
    Comment comment = new Comment();
    comment.setComment(dto.getComment());
    comment.setPetitionId(dto.getPetitionId());
    comment.setUserId(dto.getUserId());
    commentService.createComment2(comment);
    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
  }

  @GetMapping("/{id}/ispetitionSigned")
  public boolean isPetitionSigned(@PathVariable Long id, @RequestParam Long userid) {
    User user = userService.findById(userid);
    if (user == null) {
      return false;
    }
    Petition petition = petitionService.findById(id);
    if (petition == null) {
      return false;
    }
    boolean alreadySigned = signeeService.isSigned(user, petition);
    return alreadySigned;
  }

  @GetMapping("/{id}/ispetitionSaved")
  public boolean isPetitionSaved(@PathVariable Long id, @RequestParam Long userid) {
    User user = userService.findById(userid);
    if (user == null) {
      return false;
    }
    Petition petition = petitionService.findById(id);
    if (petition == null) {
      return false;
    }
    boolean alreadySaved = savedService.isSaved(user, petition);
    return alreadySaved;
  }

  @GetMapping("/{id}")
  public DetailViewPetitionDTO one(@PathVariable Long id) {
    long signatures = petitionService.getNumberOfSignatures(id);
    Petition returnPetition = petitionService.findById(id);
    returnPetition.setNoOfSigners(signatures);
    return new DetailViewPetitionDTO(returnPetition);
  }

  @GetMapping("/{petitionId}/comments")
  public ResponseEntity<List<ShowCommentDTO>> getCommentsForPetition(@PathVariable Long petitionId) {
    List<Comment> comments = commentService.getCommentsByPetitionId(petitionId);
    List<ShowCommentDTO> dtos = comments.stream()
      .map(comment -> {
        ShowCommentDTO dto = new ShowCommentDTO(comment);
        dto.setUserName(userService.findById(comment.getUserId()).getUsername()); // manually set username
        return dto;
    })
    .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("withuser/{id}")
  public DetailViewPetitionDTO ones(@PathVariable Long id, @RequestParam long userId) {
    long signatures = petitionService.getNumberOfSignatures(id);
    Petition returnPetition = petitionService.findById(id);
    try {
      User logedIn = userService.findById(userId);
      String temp = returnPetition.getTemplateString();
      temp = temp.replace("[PetitionTitle]", returnPetition.getTitle());
      temp = temp.replace("[UserName]", logedIn.getFirstName() + " " + logedIn.getLastName());
      temp += "\n\n Current number of signatures: " + signatures;
      returnPetition.setTemplateString(temp);
    } catch (EntityNotFoundException e) {
      System.err.println("User not found, the template is not adapted");
    }
    returnPetition.setNoOfSigners(signatures);
    return new DetailViewPetitionDTO(returnPetition);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deletePetition(@PathVariable("id") long id, @RequestParam Long userid) {
    try {
      User user = userService.findById(userid);
      if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
      }
      boolean success = petitionService.deletePetition(id, userid);
      if (success) {
        return ResponseEntity.ok("Petition deleted");
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete petition.");
      }
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Petition not found.");
    }
  }

  @DeleteMapping("/all/{id}")
  public ResponseEntity<String> deleteUsersPetitions(@PathVariable("id") ArrayList<Long> ids,
      @RequestParam Long userid) {
    try {
      User user = userService.findById(userid);
      if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
      }
      boolean success = petitionService.deleteUsersPetitions(ids, userid);
      if (success) {
        return ResponseEntity.ok("Petitions deleted successfully.");
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete petitions.");
      }
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
  }
}