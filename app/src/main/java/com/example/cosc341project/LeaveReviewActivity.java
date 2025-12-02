package com.example.cosc341project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LeaveReviewActivity extends AppCompatActivity {

    private TextView tvReviewTourName;
    private EditText etReviewText;
    private RatingBar ratingBar;
    private Button btnCancelReview, btnSubmitReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

        // Title at top of screen
        TextView tvYourToursTitle = findViewById(R.id.tvYourToursTitle);
        tvYourToursTitle.setText("Your Tours");

        tvReviewTourName = findViewById(R.id.tvReviewTourName);
        etReviewText = findViewById(R.id.etReviewText);
        ratingBar = findViewById(R.id.ratingBar);
        btnCancelReview = findViewById(R.id.btnCancelReview);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        // Get winery/tour name from intent
        String tourName = getIntent().getStringExtra("TOUR_NAME");
        if (tourName == null || tourName.isEmpty()) {
            tourName = "this winery";
        }

        tvReviewTourName.setText("Leave a Review for " + tourName + ":");

        btnCancelReview.setOnClickListener(v -> finish());

        String finalTourName = tourName;
        btnSubmitReview.setOnClickListener(v -> {
            // You could later save reviewText + ratingBar.getRating()
            String reviewText = etReviewText.getText().toString().trim();
            float rating = ratingBar.getRating();
            // (not used yet, but ready if you need it)

            new AlertDialog.Builder(LeaveReviewActivity.this)
                    .setMessage(getString(R.string.review_confirmation, finalTourName))
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        finish();   // go back to MyToursActivity
                    })
                    .show();
        });
    }
}
