package com.example.isproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText LoginText, PasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ImageView logo = findViewById(R.id.imageView);
        //logo.setImageResource(R.drawable.ic_inv_logo);
        LoginText = findViewById(R.id.LoginText);
        PasswordText = findViewById(R.id.PasswordText);
        DBHelper dbHelper = new DBHelper(this);
        GlobalVariable appState = ((GlobalVariable)getApplicationContext());
        if (dbHelper.GetAllAuth().isEmpty()){
            appState.setAdminFlag(true);
            LogNav();}
    }

    private void LogNav() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }

    public void LoginButtonClick(View view) {
        DBHelper dbHelper = new DBHelper(this);
        GlobalVariable appState = ((GlobalVariable)getApplicationContext());
        TextView error = findViewById(R.id.LoginErrorView);
        if (LoginText.getText().toString().equals("") || PasswordText.getText().toString().equals("")) {
            error.setText("Вы не ввели логин или пароль");
        } else if (dbHelper.checkLoginPassword(LoginText.getText().toString(), PasswordText.getText().toString())) {
            if (dbHelper.cheackAdmin(LoginText.getText().toString()) || !dbHelper.globalCheackAdmin())
                appState.setAdminFlag(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else error.setText("Вы ввели неверный логин или пароль");
    }

}