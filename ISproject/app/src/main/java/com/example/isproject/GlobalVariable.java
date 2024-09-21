package com.example.isproject;
import android.app.Application;
public class GlobalVariable extends Application {
    private boolean adminFlag = false;

    public void setAdminFlag(boolean bool){
        adminFlag = bool;
    }

    public boolean getAdminFlag(){
        return adminFlag;
    }
}
