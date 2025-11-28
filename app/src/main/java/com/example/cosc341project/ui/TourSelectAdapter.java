package com.example.cosc341project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341project.R;
import com.example.cosc341project.model.Tour;

import java.util.ArrayList;

public class TourSelectAdapter extends RecyclerView.Adapter<TourSelectAdapter.ViewHolder> {

    private ArrayList<Tour> tours;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();
    private static final int MAX_SELECTED = 2;

    public TourSelectAdapter(ArrayList<Tour> tours) {
        this.tours = tours;
    }

    public ArrayList<Tour> getSelectedTours() {
        ArrayList<Tour> result = new ArrayList<>();
        for (int pos : selectedPositions)
            result.add(tours.get(pos));

        return result;
    }

    @NonNull
    @Override
    public TourSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tour_select, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TourSelectAdapter.ViewHolder holder, int position) {
        Tour t = tours.get(position);

        holder.tvName.setText(t.getName());
        holder.tvPrice.setText(String.format("$%.2f", t.getPrice()));

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(selectedPositions.contains(position));

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (selectedPositions.size() >= MAX_SELECTED) {
                    buttonView.setChecked(false);
                    Toast.makeText(buttonView.getContext(),
                            "You can only compare two tours.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    selectedPositions.add(position);
                }
            } else {
                selectedPositions.remove(Integer.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        CheckBox checkbox;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvTourName);
            tvPrice = v.findViewById(R.id.tvTourPrice);
            checkbox = v.findViewById(R.id.cbSelected);
        }
    }
}
