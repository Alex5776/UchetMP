package com.example.isproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Button authButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalVariable appState = ((GlobalVariable)getApplicationContext());
        authButton = (Button)findViewById(R.id.MainAuthButton);
        if (appState.getAdminFlag()){
            authButton.setVisibility(View.VISIBLE);
        }

    }

    public void MainSupplyButtonCklicked(View view) {
        Intent intent = new Intent(this, ReserveActivity.class);
        startActivity(intent);
    }

    public void MainProductButtonCklicked(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void MainSupplierButtonCklicked(View view) {
        Intent intent = new Intent(this, GuestActivity.class);
        startActivity(intent);
    }

    public void MainOutPutButtonCklicked(View view) {
        Intent intent = new Intent(this, OutPutActivity.class);
        startActivity(intent);
    }

    public void MainAuthButtonCklicked(View view) {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    public void MainLogOutButtonCklicked(View view) {
        GlobalVariable appState = ((GlobalVariable)getApplicationContext());
        appState.setAdminFlag(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}