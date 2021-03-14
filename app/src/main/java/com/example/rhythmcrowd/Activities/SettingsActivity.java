package com.example.rhythmcrowd.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Button buttonSettingsUpdateProfile;
    private EditText editTextSettingsCity;
    private EditText editTextSettingsState;
    private EditText editTextSettingsBio;
    private EditText editTextSettingsFirstName;
    private EditText editTextSettingsLastName;
    private EditText editTextSettingsDateCreated;
    private EditText editTextSettingsEmail;
    private EditText editTextSettingsUsername;
    private EditText editTextSettingsFollowers;
    private EditText editTextSettingsFollowing;
    private String userDocumentId;
    private DocumentReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeAttributes();
        setButtons();
        updateUI();
    }

    private void setButtons() {
        buttonSettingsUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirebase();
                finish();
            }
        });
    }

    private void initializeAttributes() {
        buttonSettingsUpdateProfile = findViewById(R.id.buttonUpdateSettings);
        editTextSettingsBio = findViewById(R.id.editTextSettingsBio);
        editTextSettingsCity = findViewById(R.id.editTextSettingsCity);
        editTextSettingsState = findViewById(R.id.editTextSettingsState);
        editTextSettingsFirstName = findViewById(R.id.editTextSettingsFirstName);
        editTextSettingsLastName = findViewById(R.id.editTextSettingsLastName);
        editTextSettingsDateCreated = findViewById(R.id.editTextSettingsDateCreated);
        editTextSettingsEmail = findViewById(R.id.editTextSettingsEmail);
        editTextSettingsUsername = findViewById(R.id.editTextSettingsUsername);
        editTextSettingsFollowers = findViewById(R.id.editTextSettingsFollowers);
        editTextSettingsFollowing = findViewById(R.id.editTextSettignsFollowing);
        userDocumentId = getIntent().getStringExtra(Constants.DOCUMENT_KEY);
        userRef = FirebaseFirestore.getInstance().collection(Constants.USERS_REF).document(userDocumentId);
    }

    private void updateUI() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, h:mma",
                        Locale.getDefault());
                String dateString = dateFormatter.format(document.getDate(Constants.DATE_CREATED));

                editTextSettingsUsername.setText(document.get(Constants.USERNAME).toString());
                editTextSettingsDateCreated.setText(dateString);
                editTextSettingsEmail.setText(document.get(Constants.EMAIL).toString());
                editTextSettingsFollowers.setText(document.get(Constants.NUM_FOLLOWERS).toString());
                editTextSettingsFollowing.setText(document.get(Constants.NUM_FOLLOWING).toString());
                //
                editTextSettingsFirstName.setText(document.get(Constants.FIRSTNAME).toString());
                editTextSettingsLastName.setText(document.get(Constants.LASTNAME).toString());
                editTextSettingsCity.setText(document.get(Constants.CITY).toString());
                editTextSettingsState.setText(document.get(Constants.STATE).toString());
                editTextSettingsBio.setText(document.get(Constants.BIO).toString());
            }
        });
    }

    private void updateFirebase() {
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference userRef = FirebaseFirestore.getInstance().collection(Constants.USERS_REF).document(userDocumentId);
                transaction.update(userRef, Constants.BIO, editTextSettingsBio.getText().toString());
                transaction.update(userRef, Constants.CITY, editTextSettingsCity.getText().toString());
                transaction.update(userRef, Constants.STATE, editTextSettingsState.getText().toString());
                transaction.update(userRef, Constants.FIRSTNAME, editTextSettingsFirstName.getText().toString());
                transaction.update(userRef, Constants.LASTNAME, editTextSettingsLastName.getText().toString());
                return null;
            }
        });
    }
}
