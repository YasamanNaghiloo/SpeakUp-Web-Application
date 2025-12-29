package com.example.speak_up.business_logic.dto;

import com.example.speak_up.business_logic.entity.User;

public class ViewUserDTO {
  private long userId;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String email;

  public ViewUserDTO(User user) {
    userId = user.getId();
    firstName = user.getFirstName();
    lastName = user.getLastName();
    phoneNumber = user.getPhoneNumber();
    email = user.getEmail();
  }

  public long getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmail() {
    return email;
  }
}
