package com.example.cosc341project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class ReceiptActivity extends AppCompatActivity {

    private TextView receiptTitle, lineWinery, lineName, lineParty, lineDateTime,
            lineExperience, lineAmountPaid, lineAmountDue, lineCardUsed;
    private ImageView qrCode;
    private Button addToCalendar, shareReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // Initialize views
        receiptTitle = findViewById(R.id.receiptTitle);
        lineWinery = findViewById(R.id.lineWinery);
        lineName = findViewById(R.id.lineName);
        lineParty = findViewById(R.id.lineParty);
        lineDateTime = findViewById(R.id.lineDateTime);
        lineExperience = findViewById(R.id.lineExperience);
        lineAmountPaid = findViewById(R.id.lineAmountPaid);
        lineAmountDue = findViewById(R.id.lineAmountDue);
        lineCardUsed = findViewById(R.id.lineCardUsed);
        qrCode = findViewById(R.id.qrCode);
        addToCalendar = findViewById(R.id.addToCalendar);
        shareReceipt = findViewById(R.id.shareReceipt);

        // --- Get data from Intent ---
        Intent i = getIntent();
        String wineryName = i.getStringExtra("wineryName");
        String selectedExperience = i.getStringExtra("selectedExperience");
        String name = i.getStringExtra("name");
        String partySize = i.getStringExtra("partySize");
        String date = i.getStringExtra("date");
        String time = i.getStringExtra("time");
        String cardNumber = i.getStringExtra("cardNumber");
        String extractedPrice = extractPrice(selectedExperience);

        String tourName = (selectedExperience != null && selectedExperience.contains("\n"))
                ? selectedExperience.split("\n")[0]
                : (selectedExperience != null ? selectedExperience : "Wine Tour");

        // --- Header ---
        receiptTitle.setText("Your \"" + tourName + "\" experience with \"" + wineryName +
                "\" has been successfully booked!");

        // --- Info fields ---
        lineWinery.setText("Winery: " + wineryName);
        lineName.setText("Name: " + name);
        lineParty.setText("Party: " + partySize);
        lineDateTime.setText("Date and Time: " + date + " " + time);
        lineExperience.setText("Experience: " + tourName);
        lineAmountPaid.setText("Amount Paid: " + (extractedPrice != null ? extractedPrice : "$0.00"));
        lineAmountDue.setText("Amount Due: $0.00");
        lineCardUsed.setText("Card Used: " + (cardNumber != null ? cardNumber : "**** **** **** 4242"));


        // ---- Save this booking as an "upcoming tour" for MyTours ----
        SharedPreferences prefs = getSharedPreferences("MyToursPrefs", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("hasUpcoming", true)
                .putString("up_name", tourName + " at " + wineryName)
                .putString("up_date", date + " " + time)
                .putString("up_details", "Party of " + partySize + " • " + selectedExperience)
                .apply();

        // --- Generate QR Code using ZXing ---
        try {
            String qrData = "Winery: " + wineryName + "\n"
                    + "Experience: " + tourName + "\n"
                    + "Name: " + name + "\n"
                    + "Party: " + partySize + "\n"
                    + "Date: " + date + " " + time + "\n"
                    + "Amount Paid: " + (extractedPrice != null ? extractedPrice : "$0.00");

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 400, 400);
            Bitmap bmp = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bmp.setPixel(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            qrCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }

        // --- Tap QR to enlarge ---
        qrCode.setOnClickListener(v -> {
            int newSize = qrCode.getLayoutParams().width == 400 ? 700 : 400;
            qrCode.getLayoutParams().width = newSize;
            qrCode.getLayoutParams().height = newSize;
            qrCode.requestLayout();
        });

        // --- Add to Calendar ---
        addToCalendar.setOnClickListener(v -> {
            try {
                Intent calIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, tourName + " at " + wineryName)
                        .putExtra(CalendarContract.Events.DESCRIPTION,
                                "Wine tour booked for " + name + " (Party of " + partySize + ")")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, wineryName);
                startActivity(calIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to open calendar.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Share or Export ---
        shareReceipt.setOnClickListener(v -> {
            String receiptText =
                    "🍷 Booking Confirmation 🍷\n\n" +
                            "Winery: " + wineryName + "\n" +
                            "Experience: " + tourName + "\n" +
                            "Date: " + date + " " + time + "\n" +
                            "Name: " + name + "\n" +
                            "Party: " + partySize + "\n" +
                            "Amount Paid: " + (extractedPrice != null ? extractedPrice : "$0.00") + "\n\n" +
                            "Thank you for booking with " + wineryName + "!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wine Tour Booking Confirmation");
            shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText);
            startActivity(Intent.createChooser(shareIntent, "Share or export your receipt"));
        });
    }

    private String extractPrice(String text) {
        if (text == null) return null;
        int index = text.indexOf("Price:");
        if (index == -1) return null;
        return text.substring(index + 6).trim();
    }
    public void onClickGoToProfile(View view){
        Intent intent = new Intent(ReceiptActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void onClickGoToSearch(View view) {
        Intent intent = new Intent(ReceiptActivity.this, SearchActivity.class);
        startActivity(intent);
    }
    public void onClickGoToMyTours(View view) {
        Intent myToursIntent = new Intent(ReceiptActivity.this, MyToursActivity.class);
        startActivity(myToursIntent);
    }
}

