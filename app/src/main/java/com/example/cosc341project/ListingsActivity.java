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

    private static final int REQUEST_EDIT_LISTING = 1001;

    private static class Listing {
        String title;
        String location;
        String description;
        String awardsText;
        String photoUri;   // file:// URI to internal storage
        String price;
        String duration;
        String capacity;
        String status;

        Listing(String title,
                String location,
                String description,
                String awardsText,
                String photoUri,
                String price,
                String duration,
                String capacity,
                String status) {
            this.title = title;
            this.location = location;
            this.description = description;
            this.awardsText = awardsText;
            this.photoUri = photoUri;
            this.price = price;
            this.duration = duration;
            this.capacity = capacity;
            this.status = status;
        }
    }

    private static final ArrayList<Listing> listings = new ArrayList<>();

    private LinearLayout listingsContainer;
    private Button btnAddListing;

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

        boolean startInCreateMode = getIntent().getBooleanExtra("startInCreateMode", false);
        if (startInCreateMode) {
            Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
            intent.putExtra("position", -1);
            startActivityForResult(intent, REQUEST_EDIT_LISTING);
        }

        renderListings();

        btnAddListing.setOnClickListener(v -> {
            Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
            intent.putExtra("position", -1);
            startActivityForResult(intent, REQUEST_EDIT_LISTING);
        });

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

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == REQUEST_EDIT_LISTING) {
            boolean delete = data.getBooleanExtra("delete", false);
            int position = data.getIntExtra("position", -1);

            if (delete) {
                if (position >= 0 && position < listings.size()) {
                    listings.remove(position);
                    renderListings();
                }
                return;
            }

            String status = data.getStringExtra("status");
            String title = data.getStringExtra("title");
            String location = data.getStringExtra("location");
            String description = data.getStringExtra("description");
            String awardsText = data.getStringExtra("awardsText");
            String photoUri = data.getStringExtra("photoUri");

            String price = data.getStringExtra("price");
            String duration = data.getStringExtra("duration");
            String capacity = data.getStringExtra("capacity");

            if (title == null || title.isEmpty()) title = "New listing";
            if (location == null) location = "";
            if (description == null || description.isEmpty()) description = "No description provided";
            if (awardsText == null) awardsText = "";
            if (photoUri == null) photoUri = "";
            if (price == null) price = "";
            if (duration == null) duration = "";
            if (capacity == null) capacity = "";
            if (status == null) status = "Active 🟢";

            if (position == -1) {
                listings.add(new Listing(
                        title, location, description, awardsText,
                        photoUri, price, duration, capacity, status
                ));
            } else if (position >= 0 && position < listings.size()) {
                Listing l = listings.get(position);
                l.title = title;
                l.location = location;
                l.description = description;
                l.awardsText = awardsText;
                l.photoUri = photoUri;
                l.price = price;
                l.duration = duration;
                l.capacity = capacity;
                l.status = status;
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

            TextView tvStatus = cardView.findViewById(R.id.tvListingStatus);
            TextView tvTitle = cardView.findViewById(R.id.tvListingTitle);
            TextView tvLocation = cardView.findViewById(R.id.tvListingAddress);
            TextView tvDescription = cardView.findViewById(R.id.tvListingDescription);
            TextView tvAwards = cardView.findViewById(R.id.tvListingAwards);
            TextView tvPrice = cardView.findViewById(R.id.tvListingPrice);
            TextView tvDuration = cardView.findViewById(R.id.tvListingDuration);
            TextView tvCapacity = cardView.findViewById(R.id.tvListingCapacity);
            Button btnEditListing = cardView.findViewById(R.id.btnEditListing);
            android.widget.ImageView ivListingImage = cardView.findViewById(R.id.ivListingImage);

            tvStatus.setText(listing.status);
            tvTitle.setText(listing.title);
            tvLocation.setText(listing.location);
            tvDescription.setText(listing.description);

            if (!listing.price.isEmpty()) {
                tvPrice.setText("$ " + listing.price);
            } else {
                tvPrice.setText("N/A");
            }

            if (!listing.duration.isEmpty()) {
                tvDuration.setText(listing.duration + " hours");
            } else {
                tvDuration.setText("N/A");
            }

            if (!listing.capacity.isEmpty()) {
                tvCapacity.setText(listing.capacity + "⛹");
            } else {
                tvCapacity.setText("N/A");
            }

            if (listing.awardsText.isEmpty()) {
                tvAwards.setText("None");
            } else {
                tvAwards.setText(listing.awardsText);
            }

            // Show photo from internal storage if available
            if (!listing.photoUri.isEmpty()) {
                ivListingImage.setImageURI(Uri.parse(listing.photoUri));
            } else {
                ivListingImage.setImageResource(R.drawable.ic_launcher_background);
            }

            // Edit this listing
            btnEditListing.setOnClickListener(v -> {
                Intent intent = new Intent(ListingsActivity.this, EditTourListingActivity.class);
                intent.putExtra("position", index);
                intent.putExtra("status", listing.status);
                intent.putExtra("title", listing.title);
                intent.putExtra("location", listing.location);
                intent.putExtra("description", listing.description);
                intent.putExtra("awardsText", listing.awardsText);
                intent.putExtra("photoUri", listing.photoUri);
                intent.putExtra("price", listing.price);
                intent.putExtra("duration", listing.duration);
                intent.putExtra("capacity", listing.capacity);
                startActivityForResult(intent, REQUEST_EDIT_LISTING);
            });

            listingsContainer.addView(cardView);
        }
    }
}