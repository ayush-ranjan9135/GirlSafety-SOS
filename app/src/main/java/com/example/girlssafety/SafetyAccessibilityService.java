package com.example.girlssafety;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SafetyAccessibilityService extends AccessibilityService {

    private static final String TAG = "SafetyAccessibility";
    private int count = 0;
    private long lastPressTime = 0;
    private static final long THRESHOLD = 2000; // 2 seconds window

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GirlSafety:SOS_WakeLock");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && action == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastPressTime < THRESHOLD) {
                count++;
            } else {
                count = 1;
            }

            lastPressTime = currentTime;
            Log.d(TAG, "Volume Up pressed, count: " + count);

            if (count == 4) {
                count = 0;
                triggerSOS();
            }
        }
        return super.onKeyEvent(event);
    }

    private void triggerSOS() {
        // Acquire wake lock to ensure the CPU stays on while sending SOS
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire(10000); // 10 seconds
        }

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(this, "🚨 SOS TRIGGERED! Sending alerts...", Toast.LENGTH_LONG).show();
            checkPermissionsAndProceed();
        });
    }

    private void checkPermissionsAndProceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissions not granted for SOS in background");
            // We can't request permissions from a Service. 
            // They must be granted beforehand in MainActivity.
            return;
        }
        fetchLocationAndContacts();
    }

    private void fetchLocationAndContacts() {
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        String locationLink = (location != null)
                                ? "\nMy Location: https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude()
                                : "\n(Fetching location...)";

                        if (location == null) {
                            fusedLocationClient.getLastLocation().addOnSuccessListener(lastLoc -> {
                                String fallback = (lastLoc != null)
                                        ? "\nLast Known Location: https://www.google.com/maps?q=" + lastLoc.getLatitude() + "," + lastLoc.getLongitude()
                                        : "\n(Location unavailable)";
                                loadContactsAndSend("HELP! I am in an emergency." + fallback);
                            });
                        } else {
                            loadContactsAndSend("HELP! I am in an emergency." + locationLink);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Location error: " + e.getMessage());
                        loadContactsAndSend("HELP! I am in an emergency. (Location error)");
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "Security Exception: " + e.getMessage());
        }
    }

    private void loadContactsAndSend(String messageToSend) {
        if (mAuth.getCurrentUser() == null) {
            Log.e(TAG, "User not logged in");
            return;
        }
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("emergencyContacts").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> loadedContacts = (List<String>) documentSnapshot.get("contacts");
                        if (loadedContacts != null && !loadedContacts.isEmpty()) {
                            sendSMS(loadedContacts, messageToSend);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Firestore Error: " + e.getMessage()));
    }

    private void sendSMS(List<String> numbers, String message) {
        try {
            SmsManager smsManager;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                smsManager = this.getSystemService(SmsManager.class);
            } else {
                smsManager = SmsManager.getDefault();
            }

            for (String number : numbers) {
                if (number != null && !number.trim().isEmpty()) {
                    ArrayList<String> parts = smsManager.divideMessage(message);
                    smsManager.sendMultipartTextMessage(number, null, parts, null, null);
                    Log.d(TAG, "Background SMS sent to: " + number);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "SMS Error: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
