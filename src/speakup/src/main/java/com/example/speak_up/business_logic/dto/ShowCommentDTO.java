package com.example.speak_up.business_logic.dto;

import com.example.speak_up.business_logic.entity.Comment;

public class ShowCommentDTO {
    private String comment;
    private String userName;


    public ShowCommentDTO(Comment c) {
        this.comment = c.getComment();
        this.userName = "anonymous";
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
