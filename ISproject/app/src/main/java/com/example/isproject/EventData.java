package com.example.isproject;

public class EventData {
    Integer event_id, event_place;
    String event_name;
    Long event_date;

    public EventData(Integer event_id, String event_name, Integer event_place, Long event_date) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_place = event_place;
        this.event_date = event_date;
    }
}
