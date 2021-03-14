package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.User;
import com.example.rhythmcrowd.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import static android.content.ContentValues.TAG;


public class SearchByNameActivity extends AppCompatActivity {

    // Attributes
    private Button buttonSearchByName;
    private EditText editTextArtistOrSong;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        setContentView(R.layout.activity_search_by_name);
        initializeAttributes();
        setButtons();
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
        buttonSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });
    }

    //**********************************************************************************************
    // Method: searchButtonClicked()
    //
    // Purpose: Access cloud data collection and searches for artist by username. Then goes to
    // the Artist profile.
    //
    //
    //**********************************************************************************************
    private void searchButtonClicked(){

        db.collection(Constants.USERS_REF)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        user = null;

                        for (DocumentSnapshot document : snapshots) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                                if (document.get(Constants.USERNAME)
                                        .equals(editTextArtistOrSong.getText().toString())){
                                    Log.d(TAG, "onSuccess: Artist successfully Found!");
                                    Toast.makeText(getApplicationContext(), "Artist successfully Found!", Toast.LENGTH_SHORT).show();
                                    String username = document.get(Constants.USERNAME).toString();
                                    String userId = document.get(Constants.USER_ID).toString();
                                    String bio = document.get(Constants.BIO).toString();
                                    int numFollowers = document.getLong(Constants.NUM_FOLLOWERS).intValue();
                                    int numFollowing = document.getLong(Constants.NUM_FOLLOWING).intValue();
                                    String city = document.get(Constants.CITY).toString();
                                    String state = document.get(Constants.STATE).toString();

                                    user = new User(username, userId, bio, city, state, numFollowers, numFollowing);
                                }
                        }
                        editTextArtistOrSong.getText().clear();
                        goToProfile();
                    }
                });

    }

    //**********************************************************************************************
    // Method: goToProfile()
    //
    // Purpose: Opens activity with user id if found.
    //
    //
    //**********************************************************************************************
    private void goToProfile(){
        if (user != null){
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            intent.putExtra(Constants.DOCUMENT_KEY, user.getUserId());
            startActivity(intent);
        }

    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    private void initializeAttributes() {
        buttonSearchByName = findViewById(R.id.buttonSearchByName);
        editTextArtistOrSong = findViewById(R.id.editTextArtistOrSong);
    }
}
