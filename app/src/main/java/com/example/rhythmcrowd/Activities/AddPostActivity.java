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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddPostActivity extends AppCompatActivity {

    // Attributes
    private Button buttonAddPost;
    private EditText editTextPost;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

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
        setContentView(R.layout.activity_add_post);
        initializeAttributes();
        setPostButton();
    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    public void initializeAttributes() {
        buttonAddPost = findViewById(R.id.buttonAddPost);
        editTextPost = findViewById(R.id.editTextPostTxt);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    //**********************************************************************************************
    // Method: setButtons()
    //
    // Purpose: To give the buttons in the activity functionality.
    //
    //
    //**********************************************************************************************
    public void setPostButton() {
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put(Constants.NUM_LIKES, 0);
                data.put(Constants.NUM_COMMENTS, 0);
                data.put(Constants.POST_TXT, editTextPost.getText().toString());
                data.put(Constants.TIMESTAMP, FieldValue.serverTimestamp());
                data.put(Constants.USER_ID, mAuth.getCurrentUser().getUid());
                data.put(Constants.USERNAME, mAuth.getCurrentUser().getDisplayName());
                db.collection(Constants.POST_REF)
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "onSuccess: Post successfully added!");
                                Toast.makeText(getApplicationContext(), "Post Successfully Added!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Exception", "Could not add post: $exception");
                            }
                        });
            }
        });
    }
}
