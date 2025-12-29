package com.example.speak_up.business_logic.entity;

public class Email {
  private String receiver = "";
  private String title = "";
  private String message = "";
  private Petition petition = null;

  public Email() {
    
  }

  public Email(String receiver, String title, String message) {
    setReceiver(receiver);
    setTitle(title);
    setMessage(message);
  }

  public Email(String receiver, String title, String message, Petition petition) {
    setReceiver(receiver);
    setTitle(title);
    setMessage(message);
    setPetition(petition);
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setPetition(Petition petition) {
    this.petition = petition;
  }

  public Petition getPetition() {
    return petition;
  }
}
