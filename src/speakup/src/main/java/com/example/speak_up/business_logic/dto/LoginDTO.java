package com.example.speak_up.business_logic.dto;

public class LoginDTO {
  private String email;
  private String pwd;

  public LoginDTO() {}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

}
