package com.example.speak_up.business_logic.dto;

public class AddCommentDTO {
  private String comment;
  private Long userId;
  private Long petitionId;

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public void setPetitionId(Long petitionId) {
    this.petitionId = petitionId;
  }

  public Long getPetitionId() {
    return petitionId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
