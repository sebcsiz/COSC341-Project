package com.example.cosc341project;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ProfileOwnerActivity extends AppCompatActivity {

    Button exitButton;
    Button homeButton;
    Button myToursButton;
    Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_owner);

        exitButton = findViewById(R.id.exitButton);
        homeButton = findViewById(R.id.HomeButton);
        myToursButton = findViewById(R.id.MyToursButton);
        profileButton = findViewById(R.id.Profile);

        // Go back to login page
        exitButton.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(ProfileOwnerActivity.this)
                    .setTitle("Logout Confirmation")
                    .setMessage("Log out of your account?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        Intent intent = new Intent(ProfileOwnerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        // Go to owner main page
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileOwnerActivity.this, OwnerMainActivity.class);
            startActivity(intent);
            finish();
        });

        // Go to listings / tours page
        myToursButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileOwnerActivity.this, ListingsActivity.class);
            startActivity(intent);
            finish();
        });


        profileButton.setOnClickListener(v -> {

        });
    }
}