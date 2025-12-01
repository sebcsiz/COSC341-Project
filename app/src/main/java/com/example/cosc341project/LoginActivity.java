package com.example.cosc341project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (email.equals("owner@someemail.com") && password.equals("1234")) {
                    Toast.makeText(LoginActivity.this,
                            "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Move to another page
                    Intent intent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                    startActivity(intent);

                    finish();

                } else if (email.equals("user@someemail.com") && password.equals("1234")) {
                    Toast.makeText(LoginActivity.this,
                            "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Move to another page
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    startActivity(intent);

                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}