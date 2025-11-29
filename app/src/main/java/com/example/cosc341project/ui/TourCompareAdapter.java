package com.example.cosc341project.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341project.R;
import com.example.cosc341project.model.Tour;

import java.util.ArrayList;

public class TourCompareAdapter extends RecyclerView.Adapter<TourCompareAdapter.ViewHolder> {

    public enum Metric { NONE, PRICE, DURATION, RATING }

    private ArrayList<Tour> tours;
    private Metric currentMetric = Metric.NONE;

    private boolean durationAscending = true;
    private boolean priceAscending = true;
    private boolean ratingAscending = false;

    public TourCompareAdapter(ArrayList<Tour> tours) {
        this.tours = tours;
    }

    public void sortBy(Metric metric, boolean ascending) {
        this.currentMetric = metric;

        if (metric == Metric.DURATION) durationAscending = ascending;
        if (metric == Metric.PRICE) priceAscending = ascending;
        if (metric == Metric.RATING) ratingAscending = ascending;

        tours.sort((a, b) -> {
            switch (metric) {
                case PRICE:
                    return ascending
                            ? Double.compare(a.getPrice(), b.getPrice())
                            : Double.compare(b.getPrice(), a.getPrice());

                case DURATION:
                    return ascending
                            ? Double.compare(a.getDurationHours(), b.getDurationHours())
                            : Double.compare(b.getDurationHours(), a.getDurationHours());

                case RATING:
                    return ascending
                            ? Double.compare(a.getRating(), b.getRating())
                            : Double.compare(b.getRating(), a.getRating());
            }
            return 0;
        });

        notifyDataSetChanged();
    }

    public void setSortMetric(Metric metric) {
        this.currentMetric = metric;
        notifyDataSetChanged();
    }

    public String generateSummary() {
        if (tours.size() < 2) return "";

        Tour a = tours.get(0);
        Tour b = tours.get(1);

        StringBuilder s = new StringBuilder();

        if (a.getRating() > b.getRating())
            s.append(a.getName()).append(" is higher rated.\n");
        else
            s.append(b.getName()).append(" is higher rated.\n");

        if (a.getPrice() < b.getPrice())
            s.append(a.getName()).append(" is cheaper.\n");
        else
            s.append(b.getName()).append(" is cheaper.\n");

        if (a.getDurationHours() < b.getDurationHours())
            s.append(a.getName()).append(" is shorter.\n");
        else
            s.append(b.getName()).append(" is shorter.\n");

        return s.toString().trim();
    }

    public String computeBestTour() {
        if (tours.size() < 2) return "Not enough data.";

        Tour a = tours.get(0);
        Tour b = tours.get(1);

        double scoreA = score(a);
        double scoreB = score(b);

        if (scoreA > scoreB)
            return "We recommend " + a.getName() + " based on overall rating, price, and duration.";
        else
            return "We recommend " + b.getName() + " based on overall rating, price, and duration.";
    }

    private double score(Tour t) {
        double price = t.getPrice();
        double duration = t.getDurationHours();
        double rating = t.getRating();

        double score = 0;
        score += (5 - price / 200.0);
        score += (5 - duration);
        score += rating;

        return score;
    }

    @NonNull
    @Override
    public TourCompareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour_compare, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TourCompareAdapter.ViewHolder h, int pos) {
        Tour t = tours.get(pos);
        Context ctx = h.itemView.getContext();

        String cleanName = t.getImage()
                .replace(".png", "")
                .replace(".jpg", "")
                .replace(".jpeg", "")
                .replace(".webp", "");

        int imgId = ctx.getResources().getIdentifier(cleanName, "drawable", ctx.getPackageName());
        if (imgId != 0) h.ivWinery.setImageResource(imgId);

        h.tvName.setText(t.getName());
        h.tvWineryDesc.setText(t.getWineryDescription());
        h.tvTastingDesc.setText(t.getTastingDescription());

        h.tvAddressPhone.setText(t.getAddress() + "\n" + t.getPhone());
        h.tvDriver.setText("Tour Driver: " + t.getDriverName());

        StringBuilder menu = new StringBuilder();
        for (String m : t.getMenuItems())
            menu.append("• ").append(m).append("\n");
        h.tvMenu.setText(menu.toString().trim());

        h.tvDuration.setText(String.format("%.1f hr", t.getDurationHours()));
        h.tvRating.setText(String.format("%.1f/5 (%d reviews)", t.getRating(), t.getReviewCount()));
        h.tvPrice.setText(String.format("$%.2f", t.getPrice()));

        h.tvPrice.setTextColor(Color.BLACK);
        h.tvRating.setTextColor(Color.BLACK);
        h.tvDuration.setTextColor(Color.BLACK);

        h.icArrow.setVisibility(View.GONE);

        if (currentMetric != Metric.NONE && tours.size() == 2) {
            Tour a = tours.get(0);
            Tour b = tours.get(1);

            Tour better = getBetterTour(a, b);
            Tour worse = (better == a ? b : a);

            if (t == better)
                highlightA(h, better, worse);
            else
                highlightA(h, worse, better);
        }

    }

    private Tour getBetterTour(Tour a, Tour b) {
        switch (currentMetric) {
            case PRICE:
                return a.getPrice() < b.getPrice() ? a : b;

            case DURATION:
                if (durationAscending)
                    return a.getDurationHours() < b.getDurationHours() ? a : b;
                else
                    return a.getDurationHours() > b.getDurationHours() ? a : b;

            case RATING:
                return a.getRating() > b.getRating() ? a : b;
        }
        return a;
    }

    private void highlightA(ViewHolder h, Tour better, Tour worse) {

        h.icArrow.setVisibility(View.VISIBLE);

        switch (currentMetric) {

            case PRICE:
                if (better.getPrice() < worse.getPrice()) {
                    h.tvPrice.setTextColor(Color.parseColor("#088A29"));
                    h.icArrow.setImageResource(R.drawable.ic_arrow_down);
                    h.icArrow.setColorFilter(Color.parseColor("#088A29"));
                } else {
                    h.tvPrice.setTextColor(Color.RED);
                    h.icArrow.setImageResource(R.drawable.ic_arrow_up);
                    h.icArrow.setColorFilter(Color.RED);
                }
                break;

            case DURATION:
                moveArrowTo(h.icArrow, h.tvDuration);

                double dBetter = better.getDurationHours();
                double dWorse = worse.getDurationHours();

                if (durationAscending) {
                    if (dBetter < dWorse) {
                        h.tvDuration.setTextColor(Color.parseColor("#088A29"));
                        h.icArrow.setImageResource(R.drawable.ic_arrow_down);
                        h.icArrow.setColorFilter(Color.parseColor("#088A29"));
                    } else {
                        h.tvDuration.setTextColor(Color.RED);
                        h.icArrow.setImageResource(R.drawable.ic_arrow_up);
                        h.icArrow.setColorFilter(Color.RED);
                    }
                } else {
                    if (dBetter > dWorse) {
                        h.tvDuration.setTextColor(Color.parseColor("#088A29"));
                        h.icArrow.setImageResource(R.drawable.ic_arrow_up);
                        h.icArrow.setColorFilter(Color.parseColor("#088A29"));
                    } else {
                        h.tvDuration.setTextColor(Color.RED);
                        h.icArrow.setImageResource(R.drawable.ic_arrow_down);
                        h.icArrow.setColorFilter(Color.RED);
                    }
                }
                break;

            case RATING:
                moveArrowTo(h.icArrow, h.tvRating);

                if (better.getRating() > worse.getRating()) {
                    h.tvRating.setTextColor(Color.parseColor("#088A29"));
                    h.icArrow.setImageResource(R.drawable.ic_arrow_up);
                    h.icArrow.setColorFilter(Color.parseColor("#088A29"));
                } else {
                    h.tvRating.setTextColor(Color.RED);
                    h.icArrow.setImageResource(R.drawable.ic_arrow_down);
                    h.icArrow.setColorFilter(Color.RED);
                }
                break;
        }
    }

    private void moveArrowTo(ImageView arrow, View target) {
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) arrow.getLayoutParams();

        params.startToEnd = target.getId();
        params.endToStart = ConstraintLayout.LayoutParams.UNSET;

        params.topToTop = target.getId();
        params.bottomToBottom = target.getId();

        arrow.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWinery, icTime, icStar, icArrow;
        TextView tvName, tvWineryDesc, tvTastingDesc, tvAddressPhone, tvDriver,
                tvMenu, tvDuration, tvRating, tvPrice;

        ViewHolder(View v) {
            super(v);
            ivWinery = v.findViewById(R.id.ivWinery);
            icTime = v.findViewById(R.id.icTime);
            icStar = v.findViewById(R.id.icStar);
            icArrow = v.findViewById(R.id.icArrow);
            tvName = v.findViewById(R.id.tvTourNameFull);
            tvWineryDesc = v.findViewById(R.id.tvWineryDescription);
            tvTastingDesc = v.findViewById(R.id.tvTastingDescription);
            tvAddressPhone = v.findViewById(R.id.tvAddressPhone);
            tvDriver = v.findViewById(R.id.tvDriver);
            tvMenu = v.findViewById(R.id.tvMenu);
            tvDuration = v.findViewById(R.id.tvDuration);
            tvRating = v.findViewById(R.id.tvRating);
            tvPrice = v.findViewById(R.id.tvPrice);
        }
    }
}
