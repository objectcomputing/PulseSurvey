package com.objectcomputing.pulsesurvey.model;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import javax.persistence.Column;

@Entity
@Table(name = "responsekeys")
public class ResponseKey {

    @Id
    @Column(name="responsekey")
    @AutoPopulated
    private UUID responseKey;

    @Column(name="issuedon")
    @DateCreated
    private LocalDateTime issuedOn;

    @Column(name="used")
    private boolean used = false;

    public UUID getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(UUID responseKey) {
        this.responseKey = responseKey;
    }

    public LocalDateTime getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(LocalDateTime issuedOn) {
        this.issuedOn = issuedOn;
    }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    public ResponseKey() {
    }

    public ResponseKey(UUID responseKey, LocalDateTime issuedOn, boolean used) {
        this.responseKey = responseKey;
        this.issuedOn = issuedOn;
        this.used = used;
    }
}
