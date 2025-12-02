package com.example.cosc341project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListingsActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_LISTING = 1;

    // Model for one listing
    private static class Listing {
        String title;
        String description;
        String awardsText;

        int photoResId;   // drawable resource id (0 means none)

        String price;
        String duration;
        String capacity;

        Listing(String title,
                String description,
                String awardsText,
                int photoResId,
                String price,
                String duration,
                String capacity) {
            this.title = title;
            this.description = description;
            this.awardsText = awardsText;
            this.photoResId = photoResId;
            this.price = price;
            this.duration = duration;
            this.capacity = capacity;
        }
    }

    // In-memory store of all listings
    private static final ArrayList<Listing> listings = new ArrayList<>();

    private LinearLayout listingsContainer;
    private Button btnAddListing;

    // Bottom nav
    private Button HomeButton;
    private Button ListingsButton;
    private Button ProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        listingsContainer = findViewById(R.id.listingsContainer);
        btnAddListing = findViewById(R.id.btnAddListing);

        HomeButton = findViewById(R.id.HomeButton);
        ListingsButton = findViewById(R.id.ListingsButton);
        ProfileButton = findViewById(R.id.ProfileButton);

        // If launched from OwnerMain in "create mode", open new listing editor immediately
        boolean startInCreateMode = getIntent().getBooleanExtra("startInCreateMode", false);
        if (startInCreateMode) {
            Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
            intent.putExtra("position", -1); // new listing
            startActivityForResult(intent, REQUEST_EDIT_LISTING);
        }

        renderListings();

        // + Add listing → new listing
        btnAddListing.setOnClickListener(v -> {
            Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
            intent.putExtra("position", -1);
            startActivityForResult(intent, REQUEST_EDIT_LISTING);
        });

        // Bottom nav
        HomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListingsActivity.this, OwnerMainActivity.class);
            startActivity(intent);
            finish();
        });

        ListingsButton.setOnClickListener(v -> {
            // already here
        });

        ProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListingsActivity.this, ProfileOwnerActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_LISTING && resultCode == RESULT_OK && data != null) {

            boolean delete = data.getBooleanExtra("delete", false);
            int position = data.getIntExtra("position", -1);

            if (delete) {
                if (position >= 0 && position < listings.size()) {
                    listings.remove(position);
                    renderListings();
                }
                return;
            }

            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            String awardsText = data.getStringExtra("awardsText");
            int photoResId = data.getIntExtra("photoResId", 0);

            String price = data.getStringExtra("price");
            String duration = data.getStringExtra("duration");
            String capacity = data.getStringExtra("capacity");

            if (title == null || title.isEmpty()) title = "New listing";
            if (description == null || description.isEmpty()) description = "No description provided";
            if (awardsText == null) awardsText = "";
            if (price == null) price = "";
            if (duration == null) duration = "";
            if (capacity == null) capacity = "";

            if (position == -1) {
                // New listing
                listings.add(new Listing(
                        title,
                        description,
                        awardsText,
                        photoResId,
                        price,
                        duration,
                        capacity
                ));
            } else if (position >= 0 && position < listings.size()) {
                // Update existing
                Listing l = listings.get(position);
                l.title = title;
                l.description = description;
                l.awardsText = awardsText;
                l.photoResId = photoResId;
                l.price = price;
                l.duration = duration;
                l.capacity = capacity;
            }

            renderListings();
        }
    }

    private void renderListings() {
        listingsContainer.removeAllViews();

        for (int i = 0; i < listings.size(); i++) {
            final int index = i;
            Listing listing = listings.get(i);

            View cardView = getLayoutInflater().inflate(
                    R.layout.item_listing,
                    listingsContainer,
                    false
            );

            TextView tvTitle = cardView.findViewById(R.id.tvListingTitle);
            TextView tvLocation = cardView.findViewById(R.id.tvListingLocation);
            TextView tvAwards = cardView.findViewById(R.id.tvListingAwards);
            TextView tvPrice = cardView.findViewById(R.id.tvListingPrice);
            TextView tvDuration = cardView.findViewById(R.id.tvListingDuration);
            TextView tvCapacity = cardView.findViewById(R.id.tvListingCapacity);
            Button btnEditListing = cardView.findViewById(R.id.btnEditListing);
            android.widget.ImageView ivListingImage = cardView.findViewById(R.id.ivListingImage);

            tvTitle.setText("Title: " + listing.title);
            tvLocation.setText(listing.description);

            if (listing.price != null && !listing.price.isEmpty()) {
                tvPrice.setText("Price: $" + listing.price);
            } else {
                tvPrice.setText("Price: N/A");
            }

            if (listing.duration != null && !listing.duration.isEmpty()) {
                tvDuration.setText("Duration: " + listing.duration + " hours");
            } else {
                tvDuration.setText("Duration: N/A");
            }

            if (listing.capacity != null && !listing.capacity.isEmpty()) {
                tvCapacity.setText("Capacity: " + listing.capacity + " people");
            } else {
                tvCapacity.setText("Capacity: N/A");
            }

            if (listing.awardsText == null || listing.awardsText.isEmpty()) {
                tvAwards.setText("Awards: None");
            } else {
                tvAwards.setText("Awards: " + listing.awardsText);
            }

            // Use selected photo if available
            if (listing.photoResId != 0) {
                ivListingImage.setImageResource(listing.photoResId);
            } else {
                ivListingImage.setImageResource(R.drawable.ic_launcher_background);
            }

            // Edit this listing
            btnEditListing.setOnClickListener(v -> {
                Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
                intent.putExtra("position", index);
                intent.putExtra("title", listing.title);
                intent.putExtra("description", listing.description);
                intent.putExtra("awardsText", listing.awardsText);
                intent.putExtra("photoResId", listing.photoResId);
                intent.putExtra("price", listing.price);
                intent.putExtra("duration", listing.duration);
                intent.putExtra("capacity", listing.capacity);
                startActivityForResult(intent, REQUEST_EDIT_LISTING);
            });

            listingsContainer.addView(cardView);
        }
    }
}