package com.example.girlssafety;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SMS_PERMISSION = 1;
    private static final String TAG = "MainActivity";
    private ArrayList<String> emergencyNumbers = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If the user is not logged in, redirect to LoginActivity
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return; // Stop further execution if not logged in
        }

        Button btnSendMessage = findViewById(R.id.btn_send_message);
        Button btnEditNumbers = findViewById(R.id.btn_edit_numbers);

        // Set up button listeners
        btnSendMessage.setOnClickListener(v -> checkAndSendEmergencyMessage());
        btnEditNumbers.setOnClickListener(v -> {
            // Navigate back to AddNumbersActivity to edit numbers
            Intent editIntent = new Intent(MainActivity.this, AddNumbersActivity.class);
            startActivity(editIntent);
        });
    }

    private void checkAndSendEmergencyMessage() {
        // Check if SMS permission is granted before fetching contacts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request SMS permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            // Permission already granted, proceed to send messages
            loadContactsFromFirestoreAndSendMessage();
        }
    }

    private void loadContactsFromFirestoreAndSendMessage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = user.getUid();

        db.collection("emergencyContacts")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> processFirestoreContacts(documentSnapshot))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading contacts: ", e);
                    Toast.makeText(this, "Error loading contacts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void processFirestoreContacts(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists() && documentSnapshot.contains("contacts")) {
            Object contactsObj = documentSnapshot.get("contacts");

            if (contactsObj instanceof List<?>) {
                List<?> loadedContacts = (List<?>) contactsObj;
                emergencyNumbers.clear();

                for (Object contact : loadedContacts) {
                    if (contact instanceof String) {
                        emergencyNumbers.add((String) contact);
                    }
                }

                if (!emergencyNumbers.isEmpty()) {
                    Log.d(TAG, "Fetched numbers: " + emergencyNumbers);
                    sendEmergencyMessage(); // Send messages after loading contacts
                } else {
                    Toast.makeText(this, "No valid contacts found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid contact list format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No contacts found in Firestore", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmergencyMessage() {
        String message = "Help! I am in an emergency. Please contact me.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : emergencyNumbers) {
                if (number != null && number.matches("^[+]?[0-9]{10,15}$")) { // Allow international format
                    Log.d(TAG, "Sending SMS to: " + number);
                    smsManager.sendTextMessage(number, null, message, null, null);
                } else {
                    Log.e(TAG, "Invalid number: " + number);
                }
            }
            Toast.makeText(this, "Emergency message sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Failed to send message: ", e);
            Toast.makeText(this, "Failed to send message, please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load contacts and send the SMS
                loadContactsFromFirestoreAndSendMessage();
            } else {
                Toast.makeText(this, "SMS permission is required to send messages", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
