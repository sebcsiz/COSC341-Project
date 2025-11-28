package com.example.cosc341project.data;

import android.content.Context;

import com.example.cosc341project.R;
import com.example.cosc341project.model.Tour;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.reflect.Type;

public class TourLoader {
    public static ArrayList<Tour> loadTours(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.tours);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                builder.append(line);

            Gson gson = new Gson();
            Type list = new TypeToken<ArrayList<Tour>>() {}.getType();

            return gson.fromJson(builder.toString(), list);

        } catch (Exception e) {
            System.out.println("Could not load tours.");
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}