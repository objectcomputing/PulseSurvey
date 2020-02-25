package com.objectcomputing.pulsesurvey.model;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usercomments")
public class UserComments {

    @Id
    @Column(name="commentid")
    @AutoPopulated
    private UUID commentId;

    @Column(name="responsekey")
    private UUID responseKey;

    @Column(name="commenttext")
    private String commentText;

    @Column(name="createdon")
    @DateCreated
    private LocalDateTime createdOn;

    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(UUID responseKey) {
        this.responseKey = responseKey;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public UserComments() {
    }

    public UserComments(UUID commentId, UUID responseKey, String commentText, LocalDateTime createdOn) {
        this.commentId = commentId;
        this.responseKey = responseKey;
        this.commentText = commentText;
        this.createdOn = createdOn;
    }
}

