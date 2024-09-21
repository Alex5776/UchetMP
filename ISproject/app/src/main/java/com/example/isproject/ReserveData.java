package com.example.isproject;

public class ReserveData {
    Integer reserve_id;
    Integer guest_id;
    String guest_name;
    Integer event_id;
    String event_name;
    public ReserveData(Integer reserve_id, Integer guest_id, String guest_name, Integer event_id, String event_name) {
        this.reserve_id = reserve_id;
        this.guest_id = guest_id;
        this.guest_name = guest_name;
        this.event_id = event_id;
        this.event_name = event_name;
    }
}
