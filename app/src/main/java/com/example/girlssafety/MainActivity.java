package com.example.girlssafety;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG = "MainActivity";

    private ArrayList<String> emergencyNumbers = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;

    // Volume button click variables
    private int volumeButtonClickCount = 0;
    private long lastClickTime = 0;
    private static final long TIME_INTERVAL = 2000; // 2 seconds window to press 4 times

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase and Location Services
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        Button btnSendMessage = findViewById(R.id.btn_send_message);
        Button btnEditNumbers = findViewById(R.id.btn_edit_numbers);

        // Emergency Button Trigger
        btnSendMessage.setOnClickListener(v -> checkPermissionsAndProceed());

        btnEditNumbers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNumbersActivity.class));
        });

        // Check if activity was started by Accessibility Service
        if (getIntent().getBooleanExtra("TRIGGER_SOS", false)) {
            checkPermissionsAndProceed();
        }

        // Show Accessibility Disclosure for Volume Button Trigger
        showAccessibilityDisclosure();
    }

    private boolean isAccessibilityServiceEnabled() {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + SafetyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting accessibility_enabled", e);
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showAccessibilityDisclosure() {
        if (!isAccessibilityServiceEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("🛡️ Safety Feature: Volume Button Trigger")
                    .setMessage("To send an emergency SOS using your Volume Up button (4 quick presses) even when the app is closed, this app requires the 'Accessibility Service' permission.\n\n" +
                            "How we use this:\n" +
                            "• We ONLY listen for the Volume Up button presses.\n" +
                            "• We DO NOT collect or share any other personal data.\n" +
                            "• This works even when your screen is off.")
                    .setPositiveButton("Enable in Settings", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("Not Now", null)
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getBooleanExtra("TRIGGER_SOS", false)) {
            checkPermissionsAndProceed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            long currentTime = System.currentTimeMillis();

            // If the interval between presses is too long, reset the count
            if (currentTime - lastClickTime > TIME_INTERVAL) {
                volumeButtonClickCount = 1;
            } else {
                volumeButtonClickCount++;
            }

            lastClickTime = currentTime;

            if (volumeButtonClickCount == 4) {
                volumeButtonClickCount = 0; // Reset after trigger
                Toast.makeText(this, "Panic Mode Activated!", Toast.LENGTH_SHORT).show();
                checkPermissionsAndProceed(); // Trigger existing SOS logic
            }
            return true; // Consume the event
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkPermissionsAndProceed() {
        // Request SMS and Location permissions at once
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS);
        } else {
            fetchLocationAndContacts();
        }
    }

    private void fetchLocationAndContacts() {
        // Step 1: Get Real-time Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                String locationLink = (location != null)
                        ? "\nMy Location: https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude()
                        : "\n(Location unavailable)";

                String finalMessage = "HELP! I am in an emergency." + locationLink;

                // Step 2: Fetch Contacts from Firestore and Send SMS
                loadContactsFromFirestore(finalMessage);
            });
        }
    }

    private void loadContactsFromFirestore(String messageToSend) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("emergencyContacts").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("contacts")) {
                        List<String> loadedContacts = (List<String>) documentSnapshot.get("contacts");
                        if (loadedContacts != null && !loadedContacts.isEmpty()) {
                            sendSMS(loadedContacts, messageToSend);
                        } else {
                            Toast.makeText(this, "No emergency contacts found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendSMS(List<String> numbers, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : numbers) {
                if (number != null && !number.trim().isEmpty()) {
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Log.d(TAG, "SMS sent to: " + number);
                }
            }
            Toast.makeText(this, "Emergency alerts sent with location!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "SMS failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationAndContacts();
            } else {
                Toast.makeText(this, "Permissions required for safety features!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}