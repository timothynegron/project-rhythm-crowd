package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Adapters.SearchAdapter;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.User;
import com.example.rhythmcrowd.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {
    private String selectedCategory;
    private ToggleButton toggleButtonArtist;
    private ToggleButton toggleButtonRecommended;
    //private ToggleButton toggleButtonGenre;
    private ToggleButton toggleButtonSongs;
    private ListenerRegistration searchListenerRegistration;
    private FirebaseFirestore db;
    private ArrayList<User> usersArrayList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private RecyclerView rV;


    private LinearLayoutManager searchLayoutManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeAttributes();
        setButtons();
        setSearchListener();
        setRv();
        setFab();
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchByNameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRv() {
        searchLayoutManager = new LinearLayoutManager(this);
        rV = findViewById(R.id.listViewSearch);
        rV.setLayoutManager(searchLayoutManager);
        rV.setItemAnimator(new DefaultItemAnimator());
        rV.setAdapter(searchAdapter);
    }

    private void initializeAttributes() {
        fab = findViewById(R.id.fabSearchArtistByName);
        toggleButtonArtist = findViewById(R.id.toggleButtonSearchByName);
        //toggleButtonGenre = findViewById(R.id.toggleButtonSearchByGenre);
        toggleButtonSongs = findViewById(R.id.toggleButtonSearchBySongs);
        toggleButtonRecommended = findViewById(R.id.toggleButtonSearchByRecommendations);
        searchAdapter = new SearchAdapter(usersArrayList);
        selectedCategory = Constants.RECOMMENDED;
        db = FirebaseFirestore.getInstance();
    }

    private void setSearchListener() {

        if (selectedCategory == Constants.NAME) {
            searchListenerRegistration = db.collection(Constants.USERS_REF)
                    .orderBy(Constants.USERNAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            usersArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = document.get(Constants.USERNAME).toString();
                                String userId = document.get(Constants.USER_ID).toString();
                                String bio = document.get(Constants.BIO).toString();
                                String city = document.get(Constants.CITY).toString();
                                String state = document.get(Constants.STATE).toString();
                                int numFollowers = document.getLong(Constants.NUM_FOLLOWERS).intValue();
                                int numFollowing = document.getLong(Constants.NUM_FOLLOWING).intValue();

                                User newUser = new User(username, userId, bio, city, state, numFollowers, numFollowing);

                                usersArrayList.add(newUser);
                            }
                            searchAdapter.notifyDataSetChanged();
                        }
                    });
        }else if (selectedCategory == Constants.RECOMMENDED){
            searchListenerRegistration = db.collection(Constants.USERS_REF)
                    .orderBy(Constants.NUM_FOLLOWERS, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            usersArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = document.get(Constants.USERNAME).toString();
                                String userId = document.get(Constants.USER_ID).toString();
                                String bio = document.get(Constants.BIO).toString();
                                String city = document.get(Constants.CITY).toString();
                                String state = document.get(Constants.STATE).toString();
                                int numFollowers = document.getLong(Constants.NUM_FOLLOWERS).intValue();
                                int numFollowing = document.getLong(Constants.NUM_FOLLOWING).intValue();

                                User newUser = new User(username, userId, bio, city, state, numFollowers, numFollowing);

                                usersArrayList.add(newUser);
                            }
                            searchAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }




    public void resetListener() {
        searchListenerRegistration.remove();
        setSearchListener();
    }


    // Buttons & Toggles

    private void setButtons() {
        toggleButtonRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { recommendedToggleClicked();
            }
        });
        /*
        toggleButtonGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genreToggleClicked();
            }
        });
        
         */
        toggleButtonArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistToggleClicked();
            }
        });
        toggleButtonSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { songsToggleClicked();
            }
        });
    }

    private void artistToggleClicked() {
        if(selectedCategory == Constants.NAME)
        {
            toggleButtonArtist.setChecked(true);
            return;
        }

        toggleButtonSongs.setChecked(false);
        toggleButtonRecommended.setChecked(false);
        //toggleButtonGenre.setChecked(false);
        selectedCategory = Constants.NAME;
        resetListener();
    }

    private void recommendedToggleClicked(){
        if(selectedCategory == Constants.RECOMMENDED)
        {
            toggleButtonRecommended.setChecked(true);
            return;
        }

        toggleButtonSongs.setChecked(false);
        toggleButtonArtist.setChecked(false);
        //toggleButtonGenre.setChecked(false);
        selectedCategory = Constants.RECOMMENDED;
        resetListener();
    }

    private void songsToggleClicked(){
        if(selectedCategory == Constants.SONGS){
            toggleButtonSongs.setChecked(true);
            return;
        }

        //toggleButtonGenre.setChecked(false);
        toggleButtonArtist.setChecked(false);
        toggleButtonRecommended.setChecked(false);
        selectedCategory = Constants.SONGS;

        Intent intent = new Intent(getBaseContext(), SongsActivity.class);
        startActivity(intent);
    }

/*
    private void genreToggleClicked() {
        if(selectedCategory == Constants.GENRE)
        {
            toggleButtonGenre.setChecked(true);
            return;
        }

        toggleButtonSongs.setChecked(false);
        toggleButtonArtist.setChecked(false);
        toggleButtonRecommended.setChecked(false);
        selectedCategory = Constants.GENRE;
        resetListener();

    }

 */

    @Override
    public void onResume() {
        super.onResume();
        recommendedToggleClicked();
        toggleButtonRecommended.setChecked(true);
    }
}
