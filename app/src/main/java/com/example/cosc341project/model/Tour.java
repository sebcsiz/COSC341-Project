package com.example.cosc341project.model;

import java.io.Serializable;
import java.util.List;

public class Tour implements Serializable {
    private String name,wineryDescription, tastingDescription, address, phone, driverName, image;
    private double durationHours, rating, price;
    private int reviewCount;
    private List<String> menuItems;

    public Tour(String name, String wineryDescription, String tastingDescription, String address,
                String phone, String driverName, double durationHours, double rating,
                int reviewCount, double price, String image, List<String> menuItems) {
        this.name = name;
        this.wineryDescription = wineryDescription;
        this.tastingDescription = tastingDescription;
        this.address = address;
        this.phone = phone;
        this.driverName = driverName;
        this.durationHours = durationHours;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.price = price;
        this.image = image;
        this.menuItems = menuItems;
    }

    public String getName() { return name; }
    public String getWineryDescription() { return wineryDescription; }
    public String getTastingDescription() { return tastingDescription; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getDriverName() { return driverName; }
    public double getDurationHours() { return durationHours; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public List<String> getMenuItems() { return menuItems; }
}
