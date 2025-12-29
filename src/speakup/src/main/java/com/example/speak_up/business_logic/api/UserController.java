package com.example.speak_up.business_logic.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.example.speak_up.business_logic.dto.CreateUserDTO;
import com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO;
import com.example.speak_up.business_logic.entity.CompositeId;
import com.example.speak_up.business_logic.entity.User;
import com.example.speak_up.business_logic.services.PetitionService;
import com.example.speak_up.business_logic.services.ResponsibleService;
import com.example.speak_up.business_logic.services.SigneeService;
import com.example.speak_up.business_logic.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
  private final PetitionService petitionService;
  private final UserService userService;
  private final SigneeService signeeService;
  private final ResponsibleService responsibleService;

  public UserController(PetitionService petitionService, UserService userService,
      ResponsibleService responsibleService, SigneeService signeeService) {
    this.petitionService = petitionService;
    this.userService = userService;
    this.responsibleService = responsibleService;
    this.signeeService = signeeService;
  }

  @GetMapping("/{id}/petitionnumber")
  public Long one(@PathVariable Long id) {
    User owner = userService.findById(id);
    Long num = petitionService.checkUserPetitionNum(owner);
    return num;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
    String email = loginRequest.get("email");
    String pwd = loginRequest.get("pwd");
    Long userId = userService.checkCreds(email, pwd);
    if (userId == -1L) {
      return ResponseEntity.status(401).build();
    }
    Map<String, Object> response = new HashMap<>();
    response.put("id", userId);
    response.put("name", userService.findByEmail(email).getUsername());
    response.put("token", "token-" + userId);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/unsign/{userId}/{petitionId}")
  public ResponseEntity<String> unsignPetition(@PathVariable("userId") long userId, @PathVariable("petitionId") long petitionId) {
    boolean success = signeeService.deleteSignature(new CompositeId(userId, petitionId));
    if (success) {
      return ResponseEntity.ok("Revoked signature");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to revoke signature");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody CreateUserDTO createUserDTO) {
    String result = userService.createUser(createUserDTO);
    if ("success".equals(result)) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.badRequest().body(result);
    }
  }

  @GetMapping("/{id}/petitions")
  public List<BrowseViewPetitonDTO> getOwnedPetitions(@PathVariable Long id) {
    User owner = userService.findById(id);
    List<BrowseViewPetitonDTO> petitions = petitionService.getOwnedPetitions(owner);
    return petitions;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest().body("Invalid request data: " + ex.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralExceptions(Exception ex) {
    return ResponseEntity.badRequest().body("Registration failed: " + ex.getMessage());
  }
}