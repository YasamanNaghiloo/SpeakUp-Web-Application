package com.example.speak_up.business_logic.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.speak_up.business_logic.dto.AddCommentDTO;
import com.example.speak_up.business_logic.entity.Comment;
import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.data_layer.CommentRepository;

@Service
public class CommentService {
  CommentRepository repo;

  public CommentService(CommentRepository repo) {
    this.repo = repo;
  }

  public Comment createComment(AddCommentDTO dto) {
    Comment comment = new Comment();
    comment.setComment(dto.getComment());
    comment.setPetitionId(dto.getPetitionId());
    comment.setUserId(dto.getUserId());
    return comment;
  }

  @Transactional
  public Comment createComment2(Comment c) {
    return repo.save(c);
  }

  public List<Comment> getCommentsByPetitionId(Long petitionId) {
    return repo.findByPetitionId(petitionId);
  }
}
