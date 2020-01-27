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
@Table(name = "response")
public class Response {

    @Id
    @Column(name="responseid")
    @AutoPopulated
    private UUID responseId;

    @Column(name="responsekey")
    private UUID responseKey;

    @Column
    private String selected;

    @Column(name="createdon")
    @DateCreated
    private LocalDateTime createdOn;

    public UUID getResponseId() {
        return responseId;
    }

    public void setResponseId(UUID responseId) {
        this.responseId = responseId;
    }

    public UUID getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(UUID responseKey) {
        this.responseKey = responseKey;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Response() {
    }

    public Response(UUID responseId, UUID responseKey, String selected, LocalDateTime createdOn) {
        this.responseId = responseId;
        this.responseKey = responseKey;
        this.selected = selected;
        this.createdOn = createdOn;
    }
}

