package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Patterns;

import com.example.cosc341project.model.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private TextView wineryNameText;
    private FrameLayout wineryImage;
    private LinearLayout experiencesContainer;
    private EditText nameInput, emailInput, phoneInput, dateInput, timeInput;
    private Spinner partySpinner;
    private Button backButton, continueButton;

    private String selectedExperience = null;
    private Tour tour;   // Tour received from previous Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        // --- Handle system insets (status bar / nav bar padding only) ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Initialize Views ---
        wineryNameText = findViewById(R.id.WineryName);
        wineryImage = findViewById(R.id.WineryImage);
        experiencesContainer = findViewById(R.id.linearLayout);
        nameInput = findViewById(R.id.Name);
        emailInput = findViewById(R.id.Email);
        phoneInput = findViewById(R.id.PhoneNumber);
        dateInput = findViewById(R.id.editTextDate);
        timeInput = findViewById(R.id.editTextTime);
        partySpinner = findViewById(R.id.PartySize);
        backButton = findViewById(R.id.BackButton);
        continueButton = findViewById(R.id.ContinueButton);

        // --- Ask once if user wants to autofill from profile ---
        new android.app.AlertDialog.Builder(this)
                .setTitle("Autofill Profile Information")
                .setMessage("Would you like to autofill your booking details based on your saved profile?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    nameInput.setText("User's Name");
                    emailInput.setText("user@someemail.com");
                    phoneInput.setText("123-456-7890");
                    Toast.makeText(this, "Profile information autofilled!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();

        // ======================================================
        // RECEIVE TOUR OBJECT FROM INTENT
        // ======================================================
        Intent i = getIntent();
        tour = (Tour) i.getSerializableExtra("tour");

        if (tour == null) {
            tour = new Tour(
                    "TEST WINERY",
                    "Test description",
                    "Test tasting",
                    "123 Test St",
                    "(000) 000-0000",
                    "Test Driver",
                    3.0,
                    4.5,
                    100,
                    199.99,
                    "@drawable/quail_gate.png",
                    java.util.Arrays.asList("Test Option 1", "Test Option 2")
            );
        }


        // --- Set Winery Name ---
        String wineryName = tour.getName();
        wineryNameText.setText(wineryName);

        // --- Set Winery Image from @drawable/... string ---
        int imageResId = resolveImageResource(tour.getImage());
        if (imageResId != 0) {
            wineryImage.setBackgroundResource(imageResId);

            // Hide the placeholder text inside the FrameLayout (first child)
            if (wineryImage.getChildCount() > 0) {
                View child = wineryImage.getChildAt(0);
                child.setVisibility(View.GONE);
            }
        }

        // --- Build Experience List from tastingDescription + menuItems ---
        List<String> experienceList = new ArrayList<>();

        // Main experience: tasting description + total tour price
        String mainExperience = tour.getTastingDescription();
        if (tour.getPrice() > 0) {
            String priceText = String.format(Locale.CANADA, "Tour Price: $%.2f", tour.getPrice());
            mainExperience = mainExperience + "\n" + priceText;
        }
        experienceList.add(mainExperience);

        // Add menu items as additional selectable experiences (if any)
        List<String> menuItems = tour.getMenuItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            experienceList.addAll(menuItems);
        }

        // --- Dynamically Create Experience Cards ---
        for (String desc : experienceList) {
            TextView tv = new TextView(this);
            tv.setText(desc);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundResource(R.drawable.experience_background);
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setSingleLine(false);
            tv.setMaxLines(8);
            tv.setEllipsize(null);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(350, 300);
            params.setMargins(16, 8, 16, 8);
            tv.setLayoutParams(params);

            tv.setOnClickListener(v -> selectExperience((TextView) v));
            experiencesContainer.addView(tv);
        }

        // --- Party Spinner Setup ---
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select party size", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10+"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partySpinner.setAdapter(adapter);

        // --- Back Button ---
        backButton.setOnClickListener(v -> onBackPressed());

        // --- Continue Button ---
        continueButton.setOnClickListener(v -> validateAndContinue());
    }

    private void selectExperience(TextView selected) {
        for (int i = 0; i < experiencesContainer.getChildCount(); i++) {
            View v = experiencesContainer.getChildAt(i);
            v.setBackgroundResource(R.drawable.experience_background);
        }
        selected.setBackgroundResource(R.drawable.experience_background_selected);
        selectedExperience = selected.getText().toString();
    }

    private void validateAndContinue() {
        // --- Experience selection ---
        if (selectedExperience == null) {
            Toast.makeText(this, "Please select a tour option.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Name ---
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Email ---
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please check your email format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Phone ---
        String phone = phoneInput.getText().toString().trim();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Party size ---
        if (partySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select your party size.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Date ---
        String dateStr = dateInput.getText().toString().trim();
        if (dateStr.isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy");
        sdf.setLenient(false);
        try {
            java.util.Date selectedDate = sdf.parse(dateStr);
            java.util.Date today = new java.util.Date();
            if (selectedDate.before(today)) {
                Toast.makeText(this, "Please enter a future date.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (java.text.ParseException e) {
            Toast.makeText(this, "Invalid date format. Use MM/DD/YY.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Time ---
        String timeStr = timeInput.getText().toString().trim();
        if (timeStr.isEmpty()) {
            Toast.makeText(this, "Please select a time.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Try parsing time in both 24h and 12h formats
        java.text.SimpleDateFormat timeFormats[] = {
                new java.text.SimpleDateFormat("HH:mm"),
                new java.text.SimpleDateFormat("hh:mm a")
        };
        boolean validTime = false;
        for (java.text.SimpleDateFormat tf : timeFormats) {
            tf.setLenient(false);
            try {
                tf.parse(timeStr);
                validTime = true;
                break;
            } catch (java.text.ParseException ignored) {}
        }
        if (!validTime) {
            Toast.makeText(this, "Please check your time format (e.g. 14:30 or 02:30 PM).", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Continue to PaymentActivity ---
        Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
        intent.putExtra("tour", tour); // send full Tour object
        intent.putExtra("selectedExperience", selectedExperience);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("partySize", partySpinner.getSelectedItem().toString());
        intent.putExtra("date", dateStr);
        intent.putExtra("time", timeStr);
        intent.putExtra("price", tour.getPrice());

        Toast.makeText(this, "All inputs look good! Proceeding to payment...", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void onClickGoToProfile(View view) {
        Intent intent = new Intent(BookingActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void onClickGoToSearch(View view) {
        Intent intent = new Intent(BookingActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Convert a Tour.image string like "@drawable/quail_gate.png"
     * into a real R.drawable.quail_gate resource ID.
     */
    private int resolveImageResource(String imageRef) {
        if (imageRef == null || imageRef.isEmpty()) return 0;

        String clean = imageRef.trim();

        // Remove leading '@' if present
        if (clean.startsWith("@")) {
            clean = clean.substring(1); // "drawable/quail_gate.png"
        }

        // Remove "drawable/" prefix if present
        if (clean.startsWith("drawable/")) {
            clean = clean.substring("drawable/".length()); // "quail_gate.png"
        }

        // Remove extension (.png, .jpg, .jpeg, etc.)
        int dotIndex = clean.lastIndexOf('.');
        if (dotIndex != -1) {
            clean = clean.substring(0, dotIndex);
        }

        return getResources().getIdentifier(clean, "drawable", getPackageName());
    }
}
