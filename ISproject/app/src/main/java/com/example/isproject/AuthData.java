package com.example.isproject;

public class AuthData {
    Integer auth_id;
    String authLogin;
    String authPassword;
    String authGrant;

    public AuthData(Integer auth_id, String authLogin, String authPassword, String authGrant) {
        this.auth_id = auth_id;
        this.authLogin = authLogin;
        this.authPassword = authPassword;
        this.authGrant = authGrant;
    }

    public void Clean(){
        this.auth_id = 0;
        this.authLogin = "";
        this.authPassword = "";
        this.authGrant = "";
    }
}
