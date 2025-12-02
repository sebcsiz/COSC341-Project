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

public class AvailableTour4Activity extends AppCompatActivity {

    //INITIATING VARIABLES
    Button homeButton, myToursButton, profileButton, toWineryInfoButton, bookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_tour4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //DEALING WITH BUTTONS
        //getting reference to the buttons
        homeButton = findViewById(R.id.SearchHomeButton);
        myToursButton = findViewById(R.id.SearchMyToursButton);
        profileButton = findViewById(R.id.ProfileButton);
        toWineryInfoButton = findViewById(R.id.BackToWineryInfoButton);
        bookButton = findViewById(R.id.BookTourButton);

        //setting up onClick listeners for buttons
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start SearchActivity
                Intent homeIntent = new Intent(AvailableTour4Activity.this, SearchActivity.class); //import Intent class
                startActivity(homeIntent); //starting the new activity
            } //end onClick
        });//end homeButton listener

        myToursButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ????Activity
                Intent myToursIntent = new Intent(AvailableTour4Activity.this, SearchActivity.class); //(swap SearchActivity.class with ????Activity.class)
                startActivity(myToursIntent); //starting the new activity
            } //end onClick
        });//end myToursButton listener

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ProfileActivity
                Intent profileIntent = new Intent(AvailableTour4Activity.this, ProfileActivity.class); //check????
                startActivity(profileIntent); //starting the new activity
            } //end onClick
        });//end profileButton listener

        toWineryInfoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start WineryInfoActivity
                Intent toWineryInfoIntent = new Intent(AvailableTour4Activity.this, WineryInfoActivity.class);
                startActivity(toWineryInfoIntent); //starting the new activity
            } //end onClick
        });//end toWineryInfoButton listener

        bookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start BookingActivity
                Intent bookIntent = new Intent(AvailableTour4Activity.this, BookingActivity.class); //(swap SearchActivity.class with ????Activity.class)
                startActivity(bookIntent); //starting the new activity
            } //end onClick
        });//end bookButton listener

    }//end onCreate

    /* Layout IDs
     * TextView for the winery name: WineryNameTextView
     * ImageView for the available tour image: AvailableTourImageView
     * TextView for tour details header: MoreDetailsTextView
     * TextView for the paragraph of the tour details: MoreParagraphDetailsTextView
     * Button to go back to the winery info page that they user just come from: ackToWineryInfoButton
     * Button to book a tour: BookTourButton
     * Button to access the home page: SearchHomeButton
     * Button for user to check their upcoming and previous tours: SearchMyToursButton
     * Button to access profile page: ProfileButton
     */

}//end AvailableTour4Activity