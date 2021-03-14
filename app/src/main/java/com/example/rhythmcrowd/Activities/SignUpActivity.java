package com.example.rhythmcrowd.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    // Attributes
    private Button buttonCreateAccount;
    private Button buttonCancel;
    private EditText editTextSignUpEmail;
    private EditText editTextSignUpUsername;
    private EditText editTextSignUpPassword;
    private EditText editTextSignUpFirstName;
    private EditText editTextSignUpLastName;
    private EditText editTextSignUpCity;
    private EditText editTextSignUpState;
    private FirebaseAuth mAuth;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String bio;
    private FirebaseFirestore db;

    //**********************************************************************************************
    // Method: onCreate(Bundle savedInstanceState)
    //
    // Purpose: Apart of Android OS Activity Lifecycle. Overriding the method to display
    // activity comments xml file. Initializes attributes and sets button functionality.
    //
    //
    //**********************************************************************************************
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeAttributes();
        setButtons();
    }

    //**********************************************************************************************
    // Method: onStart()
    //
    // Purpose: // Related to popping up when activity starts I think??
    //
    //
    //**********************************************************************************************
    @Override
    protected void onStart() {
        super.onStart();
    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    private void initializeAttributes() {
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpUsername = findViewById(R.id.editTextSignUpUsername);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextSignUpFirstName = findViewById(R.id.editTextSignUpFirstName);
        editTextSignUpLastName = findViewById(R.id.editTextSignUpLastName);
        editTextSignUpCity = findViewById(R.id.editTextSignUpCity);
        editTextSignUpState = findViewById(R.id.editTextSignUpState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    //**********************************************************************************************
    // Method: setButtons()
    //
    // Purpose: To give the buttons in the activity functionality.
    //
    //
    //**********************************************************************************************
    private void setButtons() {
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                email = editTextSignUpEmail.getText().toString();
                username = editTextSignUpUsername.getText().toString();
                password = editTextSignUpPassword.getText().toString();
                firstName = editTextSignUpFirstName.getText().toString();
                lastName = editTextSignUpLastName.getText().toString();
                city = editTextSignUpCity.getText().toString();
                state = editTextSignUpState.getText().toString();
                bio = "I love this app!";

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                authResult.getUser().updateProfile(profileUpdates)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Exception", "Could not update display name: $exception");
                                            }
                                        });

                                Map<String, Object> data = new HashMap<>();
                                data.put(Constants.USERNAME, username);
                                data.put(Constants.FIRSTNAME, firstName);
                                data.put(Constants.LASTNAME, lastName);
                                data.put(Constants.EMAIL, email);
                                data.put(Constants.CITY, city);
                                data.put(Constants.STATE, state);
                                data.put(Constants.NUM_FOLLOWING, 0);
                                data.put(Constants.NUM_FOLLOWERS, 0);
                                data.put(Constants.BIO, bio);
                                data.put(Constants.DATE_CREATED, FieldValue.serverTimestamp());
                                data.put(Constants.USER_ID, mAuth.getUid());
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                                db.collection(Constants.USERS_REF).document(authResult.getUser().getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: User successfully created!");
                                                Toast.makeText(getApplicationContext(), "User successfully created!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Exception", "Could not add user document: $exception");
                                            }
                                        });
                            }
                        });


            }

        });

        // Cancel Button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
