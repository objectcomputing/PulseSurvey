package model;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import javax.persistence.Column;

@Entity
@Table(name = "keys")
public class ResponseKey {

    @Id
    @Column(name="responsekey")
    @AutoPopulated
    private UUID responseKey;

    @Column(name="issuedon")
    @DateCreated
    private LocalDateTime issuedOn;

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

    public ResponseKey() {
    }

    public ResponseKey(UUID responseKey, LocalDateTime issuedOn) {
        this.responseKey = responseKey;
        this.issuedOn = issuedOn;
    }
}
