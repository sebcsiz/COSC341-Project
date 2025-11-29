package com.example.cosc341project.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341project.R;
import com.example.cosc341project.model.Tour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CompareToursActivity extends AppCompatActivity {

    private TourCompareAdapter adapter;
    private TextView tvSummary;
    RecyclerView rv;
    Button sortBtn, chooseForMeBtn, clearBtn;
    ImageButton backBtn;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compare_tours);

        tvSummary = findViewById(R.id.tvSummary);
        rv = findViewById(R.id.rvCompareTours);
        sortBtn = findViewById(R.id.btnSort);
        chooseForMeBtn = findViewById(R.id.btnChooseForMe);
        clearBtn = findViewById(R.id.btnClear);

        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v -> finish());


        ArrayList<Tour> tours = (ArrayList<Tour>) getIntent().getSerializableExtra("tours");

        adapter = new TourCompareAdapter(tours);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        sortBtn.setOnClickListener(v -> showSortDialog() );

        clearBtn.setOnClickListener(v -> {
            adapter.setSortMetric(TourCompareAdapter.Metric.NONE);
            updateSummary();
        });

        chooseForMeBtn.setOnClickListener(v -> {
            tvSummary.setText(adapter.computeBestTour());

            handler.postDelayed(this::updateSummary, 10000);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showSortDialog() {
        String[] options = {
                "Price",
                "Duration (Shortest)",
                "Duration (Longest)",
                "Rating"
        };

        new AlertDialog.Builder(this)
                .setTitle("Sort By")
                .setSingleChoiceItems(options, -1, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            adapter.sortBy(TourCompareAdapter.Metric.PRICE, true);
                            break;
                        case 1:
                            adapter.sortBy(TourCompareAdapter.Metric.DURATION, true);
                            break;
                        case 2:
                            adapter.sortBy(TourCompareAdapter.Metric.DURATION, false);
                            break;
                        case 3:
                            adapter.sortBy(TourCompareAdapter.Metric.RATING, false);
                            break;
                    }
                    updateSummary();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

   private void updateSummary() {
        tvSummary.setText(adapter.generateSummary());
   }
}