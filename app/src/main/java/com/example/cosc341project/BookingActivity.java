package com.example.booking_okwine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cosc341project.R;

public class BookingActivity extends AppCompatActivity {

    private TextView wineryNameText;
    private FrameLayout wineryImage;
    private LinearLayout experiencesContainer;
    private EditText nameInput, emailInput, phoneInput, dateInput, timeInput;
    private Spinner partySpinner;
    private Button backButton, continueButton;

    private String selectedExperience = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // --- Ask if user wants to autofill from profile ---
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

        // ======================================================
        // test code - comment out to use intent
        // ======================================================

        String wineryName = "Quail’s Gate Winery";
        int wineryImageRes = 0; // No image used for testing
        String[] tourDescriptions = {
                "Classic Tasting\nA casual introduction to our wines.\nPrice: $35",
                "Reserve Tour\nIncludes barrel tasting and cheese pairing.\nPrice: $65",
                "Private Cellar Experience\nGuided visit with premium wines.\nPrice: $120"
        };

        // ======================================================

        // --- Receive Winery Info and Tour Descriptions from Intent ---
//        Intent i = getIntent();
//        String wineryName = i.getStringExtra("wineryName");
//        int wineryImageRes = i.getIntExtra("wineryImageResId", 0);
//        String[] tourDescriptions = i.getStringArrayExtra("tourDescriptions");

        // --- Set Winery Info ---
        wineryNameText.setText(wineryName);
        if (wineryImageRes != 0) wineryImage.setBackgroundResource(wineryImageRes);

        // --- Dynamically Create Experience Cards ---
        for (String desc : tourDescriptions) {
            TextView tv = new TextView(this);
            tv.setText(desc);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundResource(R.drawable.experience_background);
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setSingleLine(false);
            tv.setMaxLines(5);
            tv.setEllipsize(null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(350, 300);
            params.setMargins(16, 8, 16, 8);
            tv.setLayoutParams(params);

            tv.setOnClickListener(v -> selectExperience((TextView) v));
            experiencesContainer.addView(tv);
        }

        // --- Party Spinner Setup ---
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Select party size", "1", "2", "3", "4", "5" , "6", "7", "8", "9", "10+"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partySpinner.setAdapter(adapter);

        // --- Back Button ---
        backButton.setOnClickListener(v -> onBackPressed());

        // --- Continue Button ---
        continueButton.setOnClickListener(v -> validateAndContinue(wineryName));
    }

    private void selectExperience(TextView selected) {
        for (int i = 0; i < experiencesContainer.getChildCount(); i++) {
            View v = experiencesContainer.getChildAt(i);
            v.setBackgroundResource(R.drawable.experience_background);
        }
        selected.setBackgroundResource(R.drawable.experience_background_selected);
        selectedExperience = selected.getText().toString();
    }
    private void validateAndContinue(String wineryName) {
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
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please check your email format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- Phone ---
            String phone = phoneInput.getText().toString().trim();
            if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
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
            Intent intent = new Intent(BookingActivity.this, com.example.booking_okwine.PaymentActivity.class);
            intent.putExtra("wineryName", wineryName);
            intent.putExtra("selectedExperience", selectedExperience);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("partySize", partySpinner.getSelectedItem().toString());
            intent.putExtra("date", dateStr);
            intent.putExtra("time", timeStr);

            Toast.makeText(this, "All inputs look good! Proceeding to payment...", Toast.LENGTH_SHORT).show();
            startActivity(intent);

    }

    public void onClickGoToProfile(View view){
        Intent intent = new Intent(BookingActivity.this, com.example.booking_okwine.ProfileActivity.class);
        startActivity(intent);
    }
}
