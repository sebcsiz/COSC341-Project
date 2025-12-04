package com.example.cosc341project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class EditTourListingActivity extends AppCompatActivity {

    private static final int MAX_HOURS = 12;

    private TextView helpStatus;
    private TextView helpTitle;
    private TextView helpPhoto;

    private Spinner spStatus;
    private EditText etTitle;
    private AutoCompleteTextView etLocation;
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
    private Button btnCancel;

    private ImageView ivPhotoPreview;
    private TextView tvAwardsList;

    // Bottom nav
    private Button HomeButton;
    private Button ListingsButton;
    private Button ProfileButton;

    // Data for this listing
    private int position = -1;          // which listing (-1 = new)
    private String awardsText = "";     // "Award 1, Award 2"
    private String photoUri = "";       // file:// URI string for saved image
    private String initialStatus = "Active 🟢";
    // Modern Photo Picker launcher
    private ActivityResultLauncher<PickVisualMediaRequest> pickPhotoLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour_listing);

        // Find views
        spStatus = findViewById(R.id.spStatus);
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
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
        btnCancel = findViewById(R.id.btnCancel);

        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        tvAwardsList = findViewById(R.id.tvAwardsList);

        HomeButton = findViewById(R.id.HomeButton);
        ListingsButton = findViewById(R.id.ListingsButton);
        ProfileButton = findViewById(R.id.ProfileButton);

        helpStatus = findViewById(R.id.helpStatus);
        helpTitle = findViewById(R.id.helpTitle);
        helpPhoto = findViewById(R.id.helpPhoto);
        View bottomNav = findViewById(R.id.bottomNav);

        setupHelpBubbles();
        setupPhotoPicker();

        etLocation.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int[] locCoords = new int[2];
                        int[] navCoords = new int[2];

                        etLocation.getLocationOnScreen(locCoords);
                        int etBottomY = locCoords[1] + etLocation.getHeight();

                        bottomNav.getLocationOnScreen(navCoords);
                        int navTopY = navCoords[1];

                        int maxHeight = navTopY - etBottomY;

                        if (maxHeight > 0) {
                            etLocation.setDropDownHeight(maxHeight);
                        }

                        etLocation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );

        String[] wineryLocations = getResources().getStringArray(R.array.okanagan_wineries);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                wineryLocations
        );

        etLocation.setAdapter(locationAdapter);
        etLocation.setThreshold(1);

        // Read incoming data
        position = getIntent().getIntExtra("position", -1);
        String incomingTitle = getIntent().getStringExtra("title");
        String incomingLocation = getIntent().getStringExtra("location");
        String incomingDescription = getIntent().getStringExtra("description");
        awardsText = getIntent().getStringExtra("awardsText");
        photoUri = getIntent().getStringExtra("photoUri");

        String incomingPrice = getIntent().getStringExtra("price");
        String incomingDuration = getIntent().getStringExtra("duration");
        String incomingCapacity = getIntent().getStringExtra("capacity");
        String incomingStatus = getIntent().getStringExtra("status");

        if (incomingStatus != null && !incomingStatus.isEmpty()) {
            initialStatus = incomingStatus;
        }

        if (awardsText == null) awardsText = "";
        if (photoUri == null) photoUri = "";
        if (incomingPrice == null) incomingPrice = "";
        if (incomingDuration == null) incomingDuration = "";
        if (incomingCapacity == null) incomingCapacity = "";

        // Prefill fields if editing
        if (position != -1) {
            if (incomingTitle != null) {
                etTitle.setText(incomingTitle);
            }
            if (incomingLocation != null) {
                etLocation.setText(incomingLocation);
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
        if (!photoUri.isEmpty()) {
            ivPhotoPreview.setImageURI(Uri.parse(photoUri));
            ivPhotoPreview.setVisibility(ImageView.VISIBLE);
        } else {
            ivPhotoPreview.setVisibility(ImageView.GONE);
        }

        setupStatusSpinner();
        setupButtons();
        setupBottomNav();
    }

    private void setupPhotoPicker() {
        pickPhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        // Copy picked image into app's private storage and get a file:// URI
                        String savedUri = saveImageToInternalStorage(uri);
                        if (!savedUri.isEmpty()) {
                            photoUri = savedUri;
                            ivPhotoPreview.setImageURI(Uri.parse(photoUri));
                            ivPhotoPreview.setVisibility(ImageView.VISIBLE);
                            Toast.makeText(this, "Photo selected.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Could not save image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private String saveImageToInternalStorage(Uri sourceUri) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = getContentResolver().openInputStream(sourceUri);
            if (in == null) return "";

            Bitmap bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) return "";

            // Create a subfolder for listing photos
            File dir = new File(getFilesDir(), "listing_photos");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Unique filename
            String fileName = "listing_" + System.currentTimeMillis() + ".png";
            File outFile = new File(dir, fileName);

            out = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();

            // Return a file:// URI string
            return Uri.fromFile(outFile).toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {}
            try {
                if (out != null) out.close();
            } catch (IOException ignored) {}
        }
    }

    private void setupHelpBubbles() {
        helpStatus.setOnClickListener(v -> {
            new AlertDialog.Builder(EditTourListingActivity.this)
                    .setTitle("Help: Status")
                    .setMessage("Active listings are shown to customers. Inactive listings are hidden but kept in your account.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        helpTitle.setOnClickListener(v -> {
            new AlertDialog.Builder(EditTourListingActivity.this)
                    .setTitle("Help: Title")
                    .setMessage("Use a clear and specific tour or winery name that guests will recognize.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        helpPhoto.setOnClickListener(v -> {
            new AlertDialog.Builder(EditTourListingActivity.this)
                    .setTitle("Help: Photo")
                    .setMessage("Choose a clear photo that represents the winery or tour experience.")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void setupStatusSpinner() {
        List<String> options = Arrays.asList("Active 🟢", "Inactive 🔴");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
        int index = options.indexOf(initialStatus);
        if (index >= 0) {
            spStatus.setSelection(index);
        }
    }

    private void setupButtons() {
        // Pick photo from gallery using Photo Picker
        btnAddPhoto.setOnClickListener(v -> {
            pickPhotoLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        // Remove photo
        btnRemovePhoto.setOnClickListener(v -> {
            photoUri = "";
            ivPhotoPreview.setVisibility(ImageView.GONE);
            Toast.makeText(this, "Photo removed.", Toast.LENGTH_SHORT).show();
        });

        // Add award
        btnAddAward.setOnClickListener(v -> showAwardInputDialog());

        // Clear all awards
        btnClearAwards.setOnClickListener(v -> {
            awardsText = "";
            tvAwardsList.setText("None yet");
            Toast.makeText(this, "All awards cleared.", Toast.LENGTH_SHORT).show();
        });

        // SAVE listing
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String capacity = etCapacity.getText().toString().trim();
            String status = spStatus.getSelectedItem().toString();

            // === REQUIRED FIELD CHECKS ===
            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                etTitle.requestFocus();
                return;
            }

            if (location.isEmpty()) {
                etLocation.setError("Location is required");
                etLocation.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                etDescription.setError("Description is required");
                etDescription.requestFocus();
                return;
            }

            if (price.isEmpty() || !isValidPrice(price)) {
                etPrice.setError("Enter price in format 0.00");
                etPrice.requestFocus();
                return;
            }

            if (duration.isEmpty() || !isValidDuration(duration)) {
                etDuration.setError("Enter duration in hh:mm (e.g. 02:30)");
                etDuration.requestFocus();
                return;
            }

            if (capacity.isEmpty()) {
                etCapacity.setError("Capacity is required");
                etCapacity.requestFocus();
                return;
            }

            if (photoUri == null || photoUri.isEmpty()) {
                Toast.makeText(this,
                        "Please select a photo for this listing.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // === END REQUIRED FIELD CHECKS ===

            new AlertDialog.Builder(EditTourListingActivity.this)
                    .setTitle("Publish listing?")
                    .setMessage("Are you sure you want to publish this tour listing?")
                    .setPositiveButton("Publish", (dialog, which) -> {
                        Toast.makeText(EditTourListingActivity.this,
                                "Listing saved and published!",
                                Toast.LENGTH_SHORT).show();

                        Intent result = new Intent();
                        result.putExtra("position", position);
                        result.putExtra("title", title);
                        result.putExtra("location", location);
                        result.putExtra("description", description);
                        result.putExtra("awardsText", awardsText);
                        result.putExtra("photoUri", photoUri);
                        result.putExtra("price", price);
                        result.putExtra("duration", duration);
                        result.putExtra("capacity", capacity);
                        result.putExtra("status", status);
                        result.putExtra("delete", false);

                        setResult(RESULT_OK, result);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // DELETE listing
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(EditTourListingActivity.this)
                    .setTitle("Delete listing?")
                    .setMessage("This action cannot be undone. Are you sure you want to delete this listing?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        Toast.makeText(EditTourListingActivity.this,
                                "Listing deleted!",
                                Toast.LENGTH_SHORT).show();

                        Intent result = new Intent();
                        result.putExtra("position", position);
                        result.putExtra("delete", true);

                        setResult(RESULT_OK, result);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // CANCEL button
        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Discard changes?")
                    .setMessage("Are you sure you want to cancel? All unsaved changes will be lost.")
                    .setPositiveButton("Yes, cancel", (dialog, which) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    })
                    .setNegativeButton("Keep editing", null)
                    .show();
        });
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

    // Validate duration in hh:mm format
    private boolean isValidDuration(String duration) {
        if (duration == null) return false;
        duration = duration.trim();

        String[] parts = duration.split(":");
        if (parts.length != 2) return false;

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hours < 0 || hours > MAX_HOURS) return false;
            if (minutes < 0 || minutes >= 60) return false;
            if (hours == 0 && minutes == 0) return false;

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate price as 0.00
    private boolean isValidPrice(String price) {
        if (price == null) return false;
        price = price.trim();
        return price.matches("\\d+\\.\\d{2}");
    }
}