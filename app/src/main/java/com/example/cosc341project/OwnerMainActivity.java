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

        // 👉 "Create your listing" should go FIRST to ListingsActivity,
        // then ListingsActivity will open EditTourListingActivity in "new listing" mode.
        cardCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ListingsActivity.class);
                intent.putExtra("startInCreateMode", true); // tell ListingsActivity to immediately open the edit screen
                startActivity(intent);
            }
        });

        // "Manage your listing" → just open the listings page normally
        cardManageListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMainActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });

        // === Bottom nav ===

        // Home → stay here (optionally you can refresh or just do nothing)
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on home, so you can leave this empty or restart activity if you want.
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