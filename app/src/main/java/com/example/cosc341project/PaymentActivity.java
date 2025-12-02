package com.example.cosc341project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cosc341project.model.Tour;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class PaymentActivity extends AppCompatActivity {

    private TextView paymentTitle;
    private EditText cardNumber, cardName, expDate, cvv, country, state, city, zip, address;
    private CheckBox saveToAccount;
    private Button backPayment, completeBooking, cardVisa, cardMaster, cardAmex;

    private Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        // Initialize views
        paymentTitle = findViewById(R.id.paymentTitle);
        cardNumber = findViewById(R.id.cardNumber);
        cardName = findViewById(R.id.cardName);
        expDate = findViewById(R.id.expDate);
        cvv = findViewById(R.id.cvv);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        zip = findViewById(R.id.zip);
        address = findViewById(R.id.address);
        saveToAccount = findViewById(R.id.saveToAccount);
        backPayment = findViewById(R.id.backPayment);
        completeBooking = findViewById(R.id.completeBooking);
        cardVisa = findViewById(R.id.cardVisa);
        cardMaster = findViewById(R.id.cardMaster);
        cardAmex = findViewById(R.id.cardAmex);

        // --- Receive info from BookingActivity ---
        Intent intent = getIntent();

        // Fallback-compatible winery name extraction
        String wineryName = null;
        if (intent.hasExtra("wineryName")) {
            wineryName = intent.getStringExtra("wineryName");
        }

        tour = (Tour) intent.getSerializableExtra("tour");
        if (wineryName == null && tour != null) {
            wineryName = tour.getName();
        }

        String selectedExperience = intent.getStringExtra("selectedExperience");
        double price = intent.getDoubleExtra("price", 0);

        // --- Set top message ---
        paymentTitle.setText(
                "This winery requires a payment of $" +
                        String.format("%.2f", price) +
                        " to book the \"" + wineryName + "\" experience."
        );

        // Autofill sample cards
        cardVisa.setOnClickListener(v ->
                fillCard("4111 1111 1111 4242", "John Doe", "12/27", "123"));

        cardMaster.setOnClickListener(v ->
                fillCard("5555 5555 5555 8888", "Jane Smith", "08/26", "456"));

        cardAmex.setOnClickListener(v ->
                fillCard("3782 8222 9463 1005", "Alex Johnson", "05/28", "789"));

        // Back button
        backPayment.setOnClickListener(v -> onBackPressed());

        // Complete booking button
        completeBooking.setOnClickListener(v -> validateAndConfirm());

        // Autofill dialog (address info)
        new AlertDialog.Builder(this)
                .setTitle("Autofill Payment Information")
                .setMessage("Would you like to autofill your payment and address details based on your saved profile?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    country.setText("Some Country");
                    state.setText("Some State");
                    city.setText("Some City");
                    zip.setText("12345");
                    address.setText("123 Some Street, Apartment 4B");
                    Toast.makeText(this, "Payment info autofilled from profile!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void fillCard(String number, String name, String exp, String cvvValue) {
        cardNumber.setText(number);
        cardName.setText(name);
        expDate.setText(exp);
        cvv.setText(cvvValue);
        Toast.makeText(this, "Card autofilled!", Toast.LENGTH_SHORT).show();
    }

    // Step 1: Validate inputs, then ask for confirmation
    private void validateAndConfirm() {
        String number = cardNumber.getText().toString().trim();
        String name = cardName.getText().toString().trim();
        String exp = expDate.getText().toString().trim();
        String cvvValue = cvv.getText().toString().trim();
        String countryValue = country.getText().toString().trim();
        String stateValue = state.getText().toString().trim();
        String cityValue = city.getText().toString().trim();
        String zipValue = zip.getText().toString().trim();
        String addressValue = address.getText().toString().trim();

        // --- Validate card number (simple regex) ---
        if (!Pattern.matches("^(\\d{4}[\\s-]?){3,4}\\d{3,4}$", number)) {
            Toast.makeText(this, "Please check your card number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate name ---
        if (name.isEmpty() || !name.matches("^[a-zA-Z\\s]+$")) {
            Toast.makeText(this, "Please enter the cardholder's name.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate expiration date ---
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);
        try {
            java.util.Date expParsed = sdf.parse(exp);
            if (expParsed.before(new java.util.Date())) {
                Toast.makeText(this, "Your card has expired.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid expiration date format. Use MM/YY.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate CVV ---
        if (!Pattern.matches("^\\d{3,4}$", cvvValue)) {
            Toast.makeText(this, "Please check your CVV.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Validate address ---
        if (countryValue.isEmpty() || stateValue.isEmpty() || cityValue.isEmpty()
                || zipValue.isEmpty() || addressValue.isEmpty()) {
            Toast.makeText(this, "Please fill all address fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // All good, ask for confirmation
        new AlertDialog.Builder(this)
                .setTitle("Confirm Pre-Payment")
                .setMessage("Are you sure you wish to proceed with pre-payment?")
                .setPositiveButton("Yes", (dialog, which) -> validateAndProceed())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Step 2: Send everything to ReceiptActivity
    private void validateAndProceed() {

        Intent prevIntent = getIntent();

        // Ensure wineryName always works
        String wineryName = prevIntent.getStringExtra("wineryName");
        if (wineryName == null) {
            Tour t = (Tour) prevIntent.getSerializableExtra("tour");
            if (t != null) wineryName = t.getName();
        }

        String selectedExperience = prevIntent.getStringExtra("selectedExperience");
        String name = prevIntent.getStringExtra("name");
        String partySize = prevIntent.getStringExtra("partySize");
        String date = prevIntent.getStringExtra("date");
        String time = prevIntent.getStringExtra("time");
        double price = prevIntent.getDoubleExtra("price", 0);

        Intent next = new Intent(PaymentActivity.this, ReceiptActivity.class);

        next.putExtra("wineryName", wineryName);
        next.putExtra("selectedExperience", selectedExperience);
        next.putExtra("name", name);
        next.putExtra("partySize", partySize);
        next.putExtra("date", date);
        next.putExtra("time", time);
        next.putExtra("price", price);

        // Payment info
        next.putExtra("cardNumber", cardNumber.getText().toString());
        next.putExtra("cardName", cardName.getText().toString());
        next.putExtra("expDate", expDate.getText().toString());
        next.putExtra("cvv", cvv.getText().toString());
        next.putExtra("country", country.getText().toString());
        next.putExtra("state", state.getText().toString());
        next.putExtra("city", city.getText().toString());
        next.putExtra("zip", zip.getText().toString());
        next.putExtra("address", address.getText().toString());
        next.putExtra("saveToAccount", saveToAccount.isChecked());

        Toast.makeText(this, "Proceeding to receipt...", Toast.LENGTH_SHORT).show();
        startActivity(next);
    }

    public void onClickGoToProfile(View view) {
        Intent intent = new Intent(PaymentActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onClickGoToSearch(View view) {
        Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void onClickGoToMyTours(View view) {
        Intent myToursIntent = new Intent(PaymentActivity.this, MyToursActivity.class);
        startActivity(myToursIntent);
    }
}
