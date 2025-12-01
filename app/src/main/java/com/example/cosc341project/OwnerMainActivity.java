package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OwnerMainActivity extends AppCompatActivity {

    private CardView cardCreateListing;
    private CardView cardManageListings;

    // Bottom nav
    private Button HomeButton;
    private Button MyToursButton;
    private Button ProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        cardCreateListing = findViewById(R.id.cardCreateListing);
        cardManageListings = findViewById(R.id.cardManageListings);

        HomeButton = findViewById(R.id.HomeButton);
        MyToursButton = findViewById(R.id.MyToursButton);
        ProfileButton = findViewById(R.id.ProfileButton);


        cardCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ListingsActivity.class);
                intent.putExtra("startInCreateMode", true); // tell ListingsActivity to immediately open the edit screen
                startActivity(intent);
            }
        });


        cardManageListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });

        // === Bottom nav ===

        // Home → stay here
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // My Listings → ListingsActivity
        MyToursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });

        // Profile → ProfileActivity
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}