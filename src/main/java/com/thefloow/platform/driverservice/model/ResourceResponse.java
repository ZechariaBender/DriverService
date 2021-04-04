package com.thefloow.platform.driverservice.model;

import java.time.Instant;

public abstract class ResourceResponse<T> {

    private Instant timestamp;
    private String message;
    protected T resource;

    public ResourceResponse(String message, T resource) {
        this.timestamp = Instant.now();
        this.message = message;
        this.resource = resource;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
