package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.data.annotation.Id;
import org.joda.time.DateTimeZone;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ResponseKeys")
public class ResponseKeys {

//  (?)    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
//    @GeneratedValue
    private UUID responseKey;

    @Column
    private DateTimeZone issuedOn;

    public UUID getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(UUID responseKey) {
        this.responseKey = responseKey;
    }

    public DateTimeZone getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(DateTimeZone issuedOn) {
        this.issuedOn = issuedOn;
    }

    public ResponseKeys() {
    }

    public ResponseKeys(UUID responseKey, DateTimeZone issuedOn) {
        this.responseKey = responseKey;
        this.issuedOn = issuedOn;
    }
}
