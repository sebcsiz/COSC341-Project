package com.example.cosc341project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341project.R;
import com.example.cosc341project.data.TourLoader;
import com.example.cosc341project.model.Tour;

import java.util.ArrayList;

public class SelectToursActivity extends AppCompatActivity {

    private TourSelectAdapter adapter;
    RecyclerView rv;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_tours);

        rv = findViewById(R.id.rvSelectTours);
        searchBtn = findViewById(R.id.btnSearch);

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}