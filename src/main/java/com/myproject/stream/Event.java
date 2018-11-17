package com.myproject.stream;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Event {

    @JsonProperty("event_type")
    private String eventType;
    private String data;
    private Long timestamp;

    public Event() {

    }

    public Event(final String eventType, final String data, final Long timestamp) {
        this.eventType = eventType;
        this.data = data;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
