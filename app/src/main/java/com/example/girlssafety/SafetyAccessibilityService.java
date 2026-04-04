package com.example.girlssafety;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

public class SafetyAccessibilityService extends AccessibilityService {

    private int count = 0;
    private long lastPressTime = 0;
    private static final long THRESHOLD = 2000; // 2 seconds window

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Not needed for button interception
    }

    @Override
    public void onInterrupt() {
        // Handle service interruption
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();

        // Listen for Volume Up button press
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && action == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastPressTime < THRESHOLD) {
                count++;
            } else {
                count = 1;
            }

            lastPressTime = currentTime;

            if (count == 4) {
                count = 0;
                triggerSOS();
            }
        }

        // Return false to allow the volume to actually change
        // Or return true if you want to block the volume change during SOS
        return super.onKeyEvent(event);
    }

    private void triggerSOS() {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(this, "SOS TRIGGERED via Volume Button!", Toast.LENGTH_LONG).show();
            
            // Start the MainActivity to handle the SMS/Location logic 
            // OR we can start a Service. Since your logic is in MainActivity, 
            // we will launch a hidden intent or a specific SafetyService.
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("TRIGGER_SOS", true);
            startActivity(intent);
        });
    }
}
