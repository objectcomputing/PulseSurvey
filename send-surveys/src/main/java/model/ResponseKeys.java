package model;

import org.joda.time.DateTimeZone;

import java.util.UUID;

public class ResponseKeys {

    private UUID responseKey;

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
