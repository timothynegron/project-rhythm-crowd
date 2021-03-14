package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    // Attributes
    private Button buttonLogin;
    private Button buttonSignUp;
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private FirebaseAuth mAuth;
    private String email;
    private String password;

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
        setContentView(R.layout.activity_login);
        initializeAttributes();
        setButtons();
    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    private void initializeAttributes() {
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    //**********************************************************************************************
    // Method: setButtons()
    //
    // Purpose: To give the buttons in the activity functionality. Set's the buttons
    // OnClickListener.
    //
    //
    //**********************************************************************************************
    private void setButtons() {

        // Log In Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextLoginEmail.getText().toString();
                password = editTextLoginPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Authentication Successful!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Log.e("exception", String.valueOf(task.getException()));

                                    Toast.makeText(SignInActivity.this, "Authentication Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Sign Up Button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
