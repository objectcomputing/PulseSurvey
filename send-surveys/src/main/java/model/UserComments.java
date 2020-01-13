package model;

import io.micronaut.data.annotation.Id;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import java.util.UUID;

public class UserComments {

    @Id
    private UUID commentId;

    @Column
    private UUID responseKey;

    @Column
    private String commentText;

    @Column
    private DateTimeZone createdOn;

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

    public DateTimeZone getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTimeZone createdOn) {
        this.createdOn = createdOn;
    }

    public UserComments() {
    }

    public UserComments(UUID commentId, UUID responseKey, String commentText, DateTimeZone createdOn) {
        this.commentId = commentId;
        this.responseKey = responseKey;
        this.commentText = commentText;
        this.createdOn = createdOn;
    }
}

