package com.example.isproject;

public class GuestData {
    Integer guest_id;
    String guest_name, guest_phone;
    Long guest_date;

    public GuestData(Integer guest_id, String guest_name, String guest_phone, Long guest_date) {
        this.guest_id = guest_id;
        this.guest_name = guest_name;
        this.guest_phone = guest_phone;
        this.guest_date = guest_date;
    }
}
