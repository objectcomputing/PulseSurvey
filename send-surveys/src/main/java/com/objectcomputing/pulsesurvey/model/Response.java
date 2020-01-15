package com.objectcomputing.pulsesurvey.model;

import io.micronaut.data.annotation.Id;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import java.util.UUID;

public class Response {

    @Id
    private UUID responseId;

    @Column
    private UUID responseKey;

    @Column
    private int selected;

    @Column
    private DateTimeZone createdOn;

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

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public DateTimeZone getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTimeZone createdOn) {
        this.createdOn = createdOn;
    }

    public Response() {
    }

    public Response(UUID responseId, UUID responseKey, int selected, DateTimeZone createdOn) {
        this.responseId = responseId;
        this.responseKey = responseKey;
        this.selected = selected;
        this.createdOn = createdOn;
    }
}

