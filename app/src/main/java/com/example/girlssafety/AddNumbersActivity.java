package com.example.girlssafety;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNumbersActivity extends AppCompatActivity {

    private ListView contactListView;
    private FloatingActionButton fabAddNumber;
    private ArrayAdapter<String> adapter;
    private List<String> contactNumbers;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers);

        // Initialize views and Firebase
        contactListView = findViewById(R.id.contactList);
        fabAddNumber = findViewById(R.id.fab_add_number);
        db = FirebaseFirestore.getInstance();

        // Initialize contact numbers list and adapter
        contactNumbers = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNumbers);
        contactListView.setAdapter(adapter);

        // Load existing contacts from Firestore
        loadContactsFromFirestore();

        // Floating action button click to open dialog
        fabAddNumber.setOnClickListener(v -> showAddContactDialog());
    }

    private void showAddContactDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        EditText edtContactNumber = dialogView.findViewById(R.id.contactNumber);
        Button btnSave = dialogView.findViewById(R.id.save);
        Button btnCancel = dialogView.findViewById(R.id.cancel);

        // Build and display the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // Handle Save button click
        btnSave.setOnClickListener(view -> {
            String contactNumber = edtContactNumber.getText().toString().trim();
            if (contactNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a contact number", Toast.LENGTH_SHORT).show();
            } else {
                // Add the contact to the list
                contactNumbers.add(contactNumber);

                // Save the updated list to Firestore
                saveContactsToFirestore();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Handle Cancel button click
        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void saveContactsToFirestore() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a map to store the list of contacts
        Map<String, Object> data = new HashMap<>();
        data.put("contacts", contactNumbers);

        // Save the list to Firestore
        db.collection("emergencyContacts")
                .document(userId)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Contacts saved successfully!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged(); // Update the ListView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving contacts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadContactsFromFirestore() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch the document containing the contact list
        db.collection("emergencyContacts")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the contact list
                        List<String> loadedContacts = (List<String>) documentSnapshot.get("contacts");
                        if (loadedContacts != null) {
                            contactNumbers.clear();
                            contactNumbers.addAll(loadedContacts);
                            adapter.notifyDataSetChanged(); // Update the ListView
                        }
                    } else {
                        Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading contacts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
