package com.example.cosc341project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WineryInfoActivity extends AppCompatActivity {

    //INITIATING VARIABLES
    Button homeButton, myToursButton, profileButton, aTourButton, bTourButton, cTourButton, dTourButton, eTourButton, bookTourButton;

    ImageButton facebookButton, instagramButton, xButton;

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
        aTourButton = findViewById(R.id.A_TourButton);
        bTourButton = findViewById(R.id.B_TourButton);
        cTourButton = findViewById(R.id.C_TourButton);
        dTourButton = findViewById(R.id.D_TourButton);
        eTourButton = findViewById(R.id.E_TourButton);
        bookTourButton = findViewById(R.id.BookTourButton);

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

        aTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start AvailableTour1Activity
                Intent aTourIntent = new Intent(WineryInfoActivity.this, AvailableTour1Activity.class);
                startActivity(aTourIntent); //starting the new activity
            } //end onClick
        });//end aTourButton listener

        bTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start AvailableTour2Activity
                Intent bTourIntent = new Intent(WineryInfoActivity.this, AvailableTour2Activity.class);
                startActivity(bTourIntent); //starting the new activity
            } //end onClick
        });//end bTourButton listener

        cTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start AvailableTour3Activity
                Intent cTourIntent = new Intent(WineryInfoActivity.this, AvailableTour3Activity.class);
                startActivity(cTourIntent); //starting the new activity
            } //end onClick
        });//end cTourButton listener

        dTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start AvailableTour4Activity
                Intent dTourIntent = new Intent(WineryInfoActivity.this, AvailableTour4Activity.class);
                startActivity(dTourIntent); //starting the new activity
            } //end onClick
        });//end dTourButton listener

        eTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start AvailableTour5Activity
                Intent eTourIntent = new Intent(WineryInfoActivity.this, AvailableTour5Activity.class);
                startActivity(eTourIntent); //starting the new activity
            } //end onClick
        });//end eTourButton listener

        bookTourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ????Activity
                Intent bookTourIntent = new Intent(WineryInfoActivity.this, SearchActivity.class); //import Intent class (swap SearchActivity.class with ????Activity.class)
                startActivity(bookTourIntent); //starting the new activity
            } //end onClick
        });//end bookTourButton listener

        //DEALING WITH IMAGEBUTTONS
        //getting reference to the buttons
        facebookButton = findViewById(R.id.FacebookButton);
        instagramButton = findViewById(R.id.InstagramButton);
        xButton = findViewById(R.id.XButton);

        //setting up onClick listeners for ImageButtons
        facebookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //URL as string
                String url = "https://www.facebook.com/login/";
                //Creating an intent with ACTION_VIEW and pass url
                Intent facebookIntent = new Intent (Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(url));
                startActivity(facebookIntent); //open link
            }
        });//end instagramButton listener

        instagramButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //URL as string
                String url = "https://www.instagram.com/accounts/login/?hl=en";
                //Creating an intent with ACTION_VIEW and pass url
                Intent instagramIntent = new Intent (Intent.ACTION_VIEW);
                instagramIntent.setData(Uri.parse(url));
                startActivity(instagramIntent); //open link
            }
        });//end instagramButton listener

        xButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //URL as string
                String url = "https://x.com/i/flow/login?lang=es%20target%3D";
                //Creating an intent with ACTION_VIEW and pass url
                Intent xIntent = new Intent (Intent.ACTION_VIEW);
                xIntent.setData(Uri.parse(url));
                startActivity(xIntent); //open link
            }
        });//end xButton listener

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

