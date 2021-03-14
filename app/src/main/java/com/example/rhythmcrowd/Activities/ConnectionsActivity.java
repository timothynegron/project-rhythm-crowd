package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Adapters.ConnectionsAdapter;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.User;
import com.example.rhythmcrowd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ConnectionsActivity extends AppCompatActivity implements ConnectionsAdapter.OnClickListener {

    private ToggleButton toggleButtonFollowing;
    private ToggleButton toggleButtonFollowers;
    private String selectedCategory;
    private ListenerRegistration connectionsListenerRegistration;
    private FirebaseFirestore db;
    private ArrayList<User> connectionsArrayList = new ArrayList<>();
    private ConnectionsAdapter connectionsAdapter;
    private FirebaseAuth mAuth;
    private LinearLayoutManager connectionsLayoutManager;
    private RecyclerView rV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connetions_list);
        initializeAttributes();
        setClickListeners();
        setConnectionsListenerRegistration();
    }

    private void initializeAttributes() {
        toggleButtonFollowing = findViewById(R.id.toggleButtonConnectionsFollowing);
        toggleButtonFollowers = findViewById(R.id.toggleButtonConnectionsFollowers);
        connectionsAdapter = new ConnectionsAdapter(connectionsArrayList, this);
        mAuth = FirebaseAuth.getInstance();
        connectionsLayoutManager = new LinearLayoutManager(this);
        rV = findViewById(R.id.listViewConnections);
        rV.setLayoutManager(connectionsLayoutManager);
        rV.setItemAnimator(new DefaultItemAnimator());
        rV.setAdapter(connectionsAdapter);
        selectedCategory = Constants.FOLLOWING;
        db = FirebaseFirestore.getInstance();
    }

    private void setConnectionsListenerRegistration() {
        if (selectedCategory == Constants.FOLLOWING) {
            connectionsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(mAuth.getUid())
                    .collection(Constants.FOLLOWING)
                    .orderBy(Constants.USERNAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }

                            connectionsArrayList.clear();

                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = document.get(Constants.USERNAME).toString();
                                String userId = document.get(Constants.USER_ID).toString();
                                Date dateConnectionStarted = document.getDate(Constants.DATE_CONNECTION_STARTED);

                                User newUser = new User(username, userId, dateConnectionStarted);

                                connectionsArrayList.add(newUser);
                            }
                            connectionsAdapter.notifyDataSetChanged();
                        }
                    });
        }
        else if (selectedCategory == Constants.FOLLOWERS){
            connectionsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(mAuth.getUid())
                    .collection(Constants.FOLLOWERS)
                    .orderBy(Constants.USERNAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }

                            connectionsArrayList.clear();

                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = document.get(Constants.USERNAME).toString();
                                String userId = document.get(Constants.USER_ID).toString();
                                Date dateConnectionStarted = document.getDate(Constants.DATE_CONNECTION_STARTED);

                                User newConnection = new User(username, userId, dateConnectionStarted);

                                connectionsArrayList.add(newConnection);
                            }
                            connectionsAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void OnConnectionClick(int position) {
            User connection = connectionsArrayList.get(position);
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            intent.putExtra(Constants.DOCUMENT_KEY, connection.getUserId());
            startActivity(intent);

    }

    public void resetListener() {
        connectionsListenerRegistration.remove();
        setConnectionsListenerRegistration();
    }

    private void setClickListeners(){
        toggleButtonFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersToggleClicked();
            }
        });
        toggleButtonFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingToggleClicked();
            }
        });
    }


    private void followingToggleClicked() {
        if(selectedCategory == Constants.FOLLOWING)
        {
            toggleButtonFollowing.setChecked(true);
            return;
        }

        toggleButtonFollowers.setChecked(false);
        selectedCategory = Constants.FOLLOWING;
        resetListener();
    }

    private void followersToggleClicked(){
        if(selectedCategory == Constants.FOLLOWERS)
        {
            toggleButtonFollowers.setChecked(true);
            return;
        }

        toggleButtonFollowing.setChecked(false);
        selectedCategory = Constants.FOLLOWERS;
        resetListener();
    }
}
