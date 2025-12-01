package com.example.cosc341project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class EditTourListingActivity extends AppCompatActivity {

    private Spinner spStatus;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etPrice;
    private EditText etDuration;
    private EditText etCapacity;

    private Button btnAddPhoto;
    private Button btnRemovePhoto;
    private Button btnAddAward;
    private Button btnClearAwards;
    private Button btnSave;
    private Button btnDelete;

    private ImageView ivPhotoPreview;
    private TextView tvAwardsList;

    // Bottom nav
    private Button HomeButton;
    private Button ListingsButton;
    private Button ProfileButton;

    // Data for this listing
    private int position = -1;          // which listing (-1 = new)
    private String awardsText = "";     // "Award 1, Award 2"
    private int photoResId = 0;         // drawable resource id (0 = none)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour_listing);

        // Find views
        spStatus = findViewById(R.id.spStatus);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etDuration = findViewById(R.id.etDuration);
        etCapacity = findViewById(R.id.etCapacity);

        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnRemovePhoto = findViewById(R.id.btnRemovePhoto);
        btnAddAward = findViewById(R.id.btnAddAward);
        btnClearAwards = findViewById(R.id.btnClearAwards);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        tvAwardsList = findViewById(R.id.tvAwardsList);

        HomeButton = findViewById(R.id.HomeButton);
        ListingsButton = findViewById(R.id.ListingsButton);
        ProfileButton = findViewById(R.id.ProfileButton);

        // Read incoming data (when editing)
        position = getIntent().getIntExtra("position", -1);
        String incomingTitle = getIntent().getStringExtra("title");
        String incomingDescription = getIntent().getStringExtra("description");
        awardsText = getIntent().getStringExtra("awardsText");
        photoResId = getIntent().getIntExtra("photoResId", 0);

        String incomingPrice = getIntent().getStringExtra("price");
        String incomingDuration = getIntent().getStringExtra("duration");
        String incomingCapacity = getIntent().getStringExtra("capacity");

        if (awardsText == null) awardsText = "";
        if (incomingPrice == null) incomingPrice = "";
        if (incomingDuration == null) incomingDuration = "";
        if (incomingCapacity == null) incomingCapacity = "";

        // === PREFILL FIELDS WHEN EDITING ===
        if (position != -1) {
            if (incomingTitle != null) {
                etTitle.setText(incomingTitle);
            }
            if (incomingDescription != null) {
                etDescription.setText(incomingDescription);
            }
            etPrice.setText(incomingPrice);
            etDuration.setText(incomingDuration);
            etCapacity.setText(incomingCapacity);
        }

        // Show existing awards list
        if (awardsText.isEmpty()) {
            tvAwardsList.setText("None yet");
        } else {
            tvAwardsList.setText(awardsText);
        }

        // Show existing photo if there is one
        if (photoResId != 0) {
            ivPhotoPreview.setImageResource(photoResId);
            ivPhotoPreview.setVisibility(ImageView.VISIBLE);
        } else {
            ivPhotoPreview.setVisibility(ImageView.GONE);
        }

        setupStatusSpinner();
        setupButtons();
        setupBottomNav();
    }

    private void setupStatusSpinner() {
        List<String> options = Arrays.asList("Active", "Inactive");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
    }

    private void setupButtons() {
        // Choose from preset photos instead of gallery
        btnAddPhoto.setOnClickListener(v -> showPhotoChoiceDialog());

        // Remove photo
        btnRemovePhoto.setOnClickListener(v -> {
            photoResId = 0;
            ivPhotoPreview.setVisibility(ImageView.GONE);
            Toast.makeText(this, "Photo removed.", Toast.LENGTH_SHORT).show();
        });

        // Add award (by name)
        btnAddAward.setOnClickListener(v -> showAwardInputDialog());

        // Clear all awards
        btnClearAwards.setOnClickListener(v -> {
            awardsText = "";
            tvAwardsList.setText("None yet");
            Toast.makeText(this, "All awards cleared.", Toast.LENGTH_SHORT).show();
        });

        // SAVE listing → send all data back
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String capacity = etCapacity.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                etTitle.requestFocus();
                return;
            }

            Toast.makeText(this, "Listing saved!", Toast.LENGTH_SHORT).show();

            Intent result = new Intent();
            result.putExtra("position", position);
            result.putExtra("title", title);
            result.putExtra("description", description);
            result.putExtra("awardsText", awardsText);
            result.putExtra("photoResId", photoResId);   // IMPORTANT
            result.putExtra("price", price);
            result.putExtra("duration", duration);
            result.putExtra("capacity", capacity);
            result.putExtra("delete", false);

            setResult(RESULT_OK, result);
            finish();
        });

        // DELETE listing
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Listing deleted!", Toast.LENGTH_SHORT).show();

            Intent result = new Intent();
            result.putExtra("position", position);
            result.putExtra("delete", true);

            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void showPhotoChoiceDialog() {
        // Labels for user to see
        final String[] options = {
                "Indigenous World",
                "Lakeside Cellars",
                "Mission Hill",
                "No photo"
        };

        // Matching drawable ids (change names to your actual drawables)
        final int[] drawables = {
                R.drawable.indigenous_world,  // vineyard
                R.drawable.lakeside_cellars,  // bottles
                R.drawable.mission_hill,  // cellar
                0                            // no photo
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a photo");
        builder.setItems(options, (dialog, which) -> {
            photoResId = drawables[which];

            if (photoResId != 0) {
                ivPhotoPreview.setImageResource(photoResId);
                ivPhotoPreview.setVisibility(ImageView.VISIBLE);
                Toast.makeText(this, "Photo set: " + options[which], Toast.LENGTH_SHORT).show();
            } else {
                ivPhotoPreview.setVisibility(ImageView.GONE);
                Toast.makeText(this, "No photo selected", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showAwardInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add award name");

        final EditText input = new EditText(this);
        input.setHint("e.g. Best Winery 2024");
        builder.setView(input);

        builder.setPositiveButton("Add", (DialogInterface dialog, int which) -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Nothing entered.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!awardsText.isEmpty()) {
                awardsText += ", ";
            }
            awardsText += text;

            tvAwardsList.setText(awardsText);
            Toast.makeText(this, "Award added: " + text, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void setupBottomNav() {
        HomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OwnerMainActivity.class);
            startActivity(intent);
            finish();
        });

        ListingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListingsActivity.class);
            startActivity(intent);
            finish();
        });

        ProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileOwnerActivity.class);
            startActivity(intent);
        });
    }
}