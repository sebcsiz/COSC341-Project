package com.example.cosc341project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyToursActivity extends AppCompatActivity {

    private TextView tabUpcoming, tabPast, emptyText;
    private View underlineUpcoming, underlinePast, pastTourCard;
    private Button btnLeaveReview;

    private boolean showingUpcoming = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tours);

        // Hook up views from XML
        tabUpcoming       = findViewById(R.id.tabUpcoming);
        tabPast           = findViewById(R.id.tabPast);
        underlineUpcoming = findViewById(R.id.underlineUpcoming);
        underlinePast     = findViewById(R.id.underlinePast);
        emptyText         = findViewById(R.id.emptyText);
        pastTourCard      = findViewById(R.id.pastTourCard);
        btnLeaveReview    = findViewById(R.id.btnLeaveReview);
        Button homeButton = findViewById(R.id.mytours_home_button);
        Button myToursButton = findViewById(R.id.mytours_my_tours_button);
        Button profileButton = findViewById(R.id.mytours_profile_button);

        // Home -> go back to Search/Home screen
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyToursActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // My Tours
        myToursButton.setOnClickListener(v -> {
        });

        // Profile -> open ProfileActivity
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyToursActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Default state = Upcoming tab
        setTabSelected(true);
        updateScreen();

        // Tab clicks
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
            Context context = v.getContext();
            Intent intent = new Intent(context, LeaveReviewActivity.class);

            intent.putExtra("TOUR_NAME", "Mission Hill Family Estate");

            context.startActivity(intent);
        });
    }
    private void setTabSelected(boolean upcomingSelected) {
        showingUpcoming = upcomingSelected;

        if (upcomingSelected) {
            tabUpcoming.setAlpha(1f);
            tabPast.setAlpha(0.5f);
            underlineUpcoming.setVisibility(View.VISIBLE);
            underlinePast.setVisibility(View.INVISIBLE);
        } else {
            tabUpcoming.setAlpha(0.5f);
            tabPast.setAlpha(1f);
            underlineUpcoming.setVisibility(View.INVISIBLE);
            underlinePast.setVisibility(View.VISIBLE);
        }
    }
    private void updateScreen() {
        if (showingUpcoming) {
            emptyText.setText("You have no upcoming tours");
            emptyText.setVisibility(View.VISIBLE);
            pastTourCard.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            pastTourCard.setVisibility(View.VISIBLE);
        }
    }
}
