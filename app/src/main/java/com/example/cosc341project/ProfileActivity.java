package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class ProfileActivity extends AppCompatActivity {
    Button exitButton;
    Button HomeButton;
    Button MyToursButton;
    Button Profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        exitButton = findViewById(R.id.exitButton);
        HomeButton = findViewById(R.id.HomeButton);
        MyToursButton = findViewById(R.id.MyToursButton);
        Profile = findViewById(R.id.Profile);



        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Move to another page
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent); // Added intent to get from profile page to login page.


                finish();
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Move to another page
                Intent intent = new Intent(ProfileActivity.this, OwnerMainActivity.class);
                startActivity(intent); // Added intent to get from profile page to login page.


                finish();
            }
        });
        MyToursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Move to another page
                Intent intent = new Intent(ProfileActivity.this, ListingsActivity.class);
                startActivity(intent); // Added intent to get from profile page to login page.


                finish();
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void onClickGoToSearch(View view) {
        Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}