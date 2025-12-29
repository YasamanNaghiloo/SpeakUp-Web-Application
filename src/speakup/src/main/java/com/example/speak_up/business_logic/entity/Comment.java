package com.example.speak_up.business_logic.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "c_id")
  private Long commentId;

  @Column(nullable = false, length = 2000, name = "content")
  private String comment;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "petition_id", nullable = false)
  private Long petitionId;

  @Column(name = "created", insertable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime creationDate;

  public Comment() {
  }

  public Comment(Long commentId, String comment, Long userId, Long petitionId, LocalDateTime date) {
    setComment(comment);
    setCommentId(commentId);
    setPetitionId(petitionId);
    setUserId(userId);
    setCreationDate(date);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }
  
  public Long getCommentId() {
    return commentId;
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

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }
}
