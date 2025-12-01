package com.example.cosc341project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WineryInfoActivity extends AppCompatActivity {

    //INITIATING VARIABLES

    Button homeButton, myToursButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_winery_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //getting reference to the buttons
        homeButton = findViewById(R.id.SearchHomeButton);
        myToursButton = findViewById(R.id.SearchMyToursButton);
        profileButton = findViewById(R.id.ProfileButton);

        //setting up onClick listeners for buttons
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ????Activity
                Intent homeIntent = new Intent(WineryInfoActivity.this, SearchActivity.class); //import Intent class (swap SearchActivity.class with ????Activity)
                startActivity(homeIntent); //starting the new activity
            } //end onClick
        });//end homeButton listener

        myToursButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ????Activity
                Intent myToursIntent = new Intent(WineryInfoActivity.this, SearchActivity.class); //import Intent class (swap SearchActivity.class with ????Activity)
                startActivity(myToursIntent); //starting the new activity
            } //end onClick
        });//end myToursButton listener

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ProfileActivity
                Intent profileIntent = new Intent(WineryInfoActivity.this, ProfileActivity.class); //import Intent class
                startActivity(profileIntent); //starting the new activity
            } //end onClick
        });//end profileButton listener

    }//end onCreate

    /* Layout IDs
     * TextView for winery name: WineryNameTextView
     * ImageView for header image: WineryImageView
     * TextView for winery details (header): WineryDetailsHeaderTextView
     * TextView for winery details: WineryDetailsTextView
     * TextView for winery awards (header): WineryAwardsHeaderTextView
     * TextView for winery awards: WineryAwardsTextView
     * TextView for available tours header: WineryAvailableToursTextView
     * HorizontalScrollView to hole the available tour options: TourScrollView
     * ImageView for tour option A: A_TourImageView
     * TextView for tour option A: A_TourTextView
     * Botton for tour option A: A_TourButton
     * ImageView for tour option B: B_TourImageView
     * TextView for tour option B: B_TourTextView
     * Botton for tour option B: B_TourButton
     * ImageView for tour option C: C_TourImageView
     * TextView for tour option C: C_TourTextView
     * Botton for tour option C: C_TourButton
     * ImageView for tour option D: D_TourImageView
     * TextView for tour option D: D_TourTextView
     * Botton for tour option D: D_TourButton
     * ImageView for tour option E: E_TourImageView
     * TextView for tour option E: E_TourTextView
     * Botton for tour option E: E_TourButton
     * TextView for contact header: ContactTextView
     * ImageButton for facebook link: FacebookButton
     * ImageButton for instagram link: InstagramButton
     * ImageButton for X link: XButton
     * TextView for location header: LocationHeaderTextView
     * TextView for location/address: LocationTextView
     * ImageView for map image: MapImageView
     * TextView for image header: ImageTextView
     * HorizontalScrollView for winery images: ImageScrollView
     * ImageView for first winery image: WineImageView1
     * ImageView for second winery image: WineImageView2
     * ImageView for third winery image: WineImageView3
     * ImageView for fourth winery image: WineImageView4
     * Button to book a tour at this winery: BookTourButton
     * Button to access the home page: SearchHomeButton
     * Button for user to check their upcoming and previous tours: SearchMyToursButton
     * Button to access profile page: ProfileButton
     */

}//end MissionHillActivity Class

