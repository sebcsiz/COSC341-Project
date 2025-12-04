package com.example.cosc341project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyToursActivity extends AppCompatActivity {

    private TextView tabUpcoming, tabPast, emptyText;
    private View underlineUpcoming, underlinePast;
    private View pastTourCard, upcomingTourCard;
    private TextView tvUpcomingName, tvUpcomingDate, tvUpcomingDetails;
    private Button btnLeaveReview;

    private boolean showingUpcoming = true;
    private boolean hasUpcoming = false;   // <--- NEW
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tours);

        prefs = getSharedPreferences("MyToursPrefs", MODE_PRIVATE);

        // Hook views
        tabUpcoming = findViewById(R.id.tabUpcoming);
        tabPast = findViewById(R.id.tabPast);
        underlineUpcoming = findViewById(R.id.underlineUpcoming);
        underlinePast = findViewById(R.id.underlinePast);
        emptyText = findViewById(R.id.emptyText);

        upcomingTourCard = findViewById(R.id.upcomingTourCard);
        pastTourCard = findViewById(R.id.pastTourCard);

        tvUpcomingName = findViewById(R.id.tvUpcomingName);
        tvUpcomingDate = findViewById(R.id.tvUpcomingDate);
        tvUpcomingDetails = findViewById(R.id.tvUpcomingDetails);

        btnLeaveReview = findViewById(R.id.btnLeaveReview);

        // Bottom nav
        Button homeButton = findViewById(R.id.mytours_home_button);
        Button myToursButton = findViewById(R.id.mytours_my_tours_button);
        Button profileButton = findViewById(R.id.mytours_profile_button);

        homeButton.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        myToursButton.setOnClickListener(v -> {
            // already here
        });
        profileButton.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        // Load any saved upcoming tour BEFORE drawing the screen
        loadUpcomingTours();

        setTabSelected(true);
        updateScreen();

        tabUpcoming.setOnClickListener(v -> {
            if (!showingUpcoming) {
                setTabSelected(true);
                updateScreen();
            }
        });

        tabPast.setOnClickListener(v -> {
            if (showingUpcoming) {
                setTabSelected(false);
                updateScreen();
            }
        });

        btnLeaveReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaveReviewActivity.class);
            intent.putExtra("TOUR_NAME", "Mission Hill Family Estate");
            startActivity(intent);
        });
    }

    private void setTabSelected(boolean upcomingSelected) {
        showingUpcoming = upcomingSelected;

        tabUpcoming.setAlpha(upcomingSelected ? 1f : 0.5f);
        tabPast.setAlpha(upcomingSelected ? 0.5f : 1f);

        underlineUpcoming.setVisibility(upcomingSelected ? View.VISIBLE : View.INVISIBLE);
        underlinePast.setVisibility(upcomingSelected ? View.INVISIBLE : View.VISIBLE);
    }

    private void updateScreen() {
        if (showingUpcoming) {
            // If we have an upcoming tour, show it.
            if (hasUpcoming) {
                upcomingTourCard.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
            } else {
                upcomingTourCard.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
                emptyText.setText("You have no upcoming tours");
            }
            pastTourCard.setVisibility(View.GONE);
        } else {
            // Past tab
            emptyText.setVisibility(View.GONE);
            upcomingTourCard.setVisibility(View.GONE);
            pastTourCard.setVisibility(View.VISIBLE);
        }
    }

    private void loadUpcomingTours() {
        boolean exists = prefs.getBoolean("hasUpcoming", false);

        if (exists) {
            hasUpcoming = true;

            String name = prefs.getString("up_name", "Unknown Tour");
            String date = prefs.getString("up_date", "Unknown Date");
            String details = prefs.getString("up_details", "Details not available");

            tvUpcomingName.setText(name);
            tvUpcomingDate.setText("Date: " + date);
            tvUpcomingDetails.setText("Details: " + details);

        } else {
            hasUpcoming = false;
        }
    }
}
