package com.example.speak_up.data_layer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speak_up.business_logic.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPetitionId(Long petitionId);
}
