package com.example.cosc341project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341project.ProfileActivity;
import com.example.cosc341project.R;
import com.example.cosc341project.SearchActivity;
import com.example.cosc341project.data.TourLoader;
import com.example.cosc341project.model.Tour;

import java.util.ArrayList;

public class SelectToursActivity extends AppCompatActivity {

    private TourSelectAdapter adapter;
    RecyclerView rv;
    Button searchBtn, homeBtn, myToursBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_tours);

        rv = findViewById(R.id.rvSelectTours);
        searchBtn = findViewById(R.id.btnSearch);
        homeBtn = findViewById(R.id.SearchHomeButton);
        myToursBtn = findViewById(R.id.SearchMyToursButton);
        profileBtn = findViewById(R.id.ProfileButton);

        ArrayList<Tour> tourList = TourLoader.loadTours(this);

        adapter = new TourSelectAdapter(tourList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        searchBtn.setOnClickListener(v -> {
            ArrayList<Tour> selected = adapter.getSelectedTours();

            if (selected.size() != 2) {
                Toast.makeText(this, "Please select two tours.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, CompareToursActivity.class);
            intent.putExtra("tours", selected);
            startActivity(intent);
        });

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start SearchActivity
                Intent homeIntent = new Intent(SelectToursActivity.this, SearchActivity.class); //
                startActivity(homeIntent); //starting the new activity
            } //end onClick
        });//end homeButton listener

        myToursBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ????Activity
                Intent myToursIntent = new Intent(SelectToursActivity.this, SearchActivity.class); //(swap SearchActivity.class with ????Activity.class)
                startActivity(myToursIntent); //starting the new activity
            } //end onClick
        });//end myToursButton listener

        profileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create Intent to start ProfileActivity
                Intent profileIntent = new Intent(SelectToursActivity.this, ProfileActivity.class); //check????
                startActivity(profileIntent); //starting the new activity
            } //end onClick
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}