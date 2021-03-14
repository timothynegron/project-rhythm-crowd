package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Adapters.ProfilePostAdapter;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.ProfilePost;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewProfileUsername;
    private TextView textViewProfileCity;
    private TextView textViewProfileState;
    private TextView textViewProfileNumFollowers;
    private TextView textViewProfileNumFollowing;
    private TextView textViewProfileBio;
    private TextView textViewProfileFirstName;
    private TextView textViewProfileLastName;
    private ToggleButton toggleButtonProfileActivity;
    private ToggleButton toggleButtonProfileSongs;
    private ToggleButton toggleButtonProfileSchedule;
    private ToggleButton toggleButtonProfileSocial;
    private ToggleButton toggleButtonProfileMerch;
    private Button buttonProfileFollow;
    private Button buttonProfileMessage;
    private String selectedCategory;
    private FloatingActionButton fab;
    private DocumentReference userRef;
    private String userDocumentId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private ProfilePostAdapter profilePostAdapter;
    private LinearLayoutManager profilePostsLayoutManager;
    private ArrayList<ProfilePost> profilePostArrayList = new ArrayList<>();
    private RecyclerView rV;
    private ListenerRegistration profilePostsListenerRegistration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeAttributes();
        setFab();
        setButtons();
        updateUI();
        followUI();
        setProfilePostsListener();
    }

    private void initializeAttributes() {
        textViewProfileUsername = findViewById(R.id.textViewProfileUsername);
        textViewProfileCity = findViewById(R.id.textViewProfileCity);
        textViewProfileState = findViewById(R.id.textViewProfileState);
        textViewProfileBio = findViewById(R.id.textViewProfileBio);
        textViewProfileFirstName = findViewById(R.id.textViewProfileFirstName);
        textViewProfileLastName = findViewById(R.id.textViewProfileLastName);
        textViewProfileNumFollowers = findViewById(R.id.textViewProfileNumFollowers);
        textViewProfileNumFollowing = findViewById(R.id.textViewProfileNumFollowing);
        toggleButtonProfileActivity = findViewById(R.id.toggleButtonProfileActivity);
        toggleButtonProfileSongs = findViewById(R.id.toggleButtonProfileSong);
        toggleButtonProfileSchedule = findViewById(R.id.toggleButtonProfileSchedule);
        toggleButtonProfileSocial = findViewById(R.id.toggleButtonProfileSocial);
        toggleButtonProfileMerch = findViewById(R.id.toggleButtonProfileMerch);
        buttonProfileFollow = findViewById(R.id.buttonProfileFollow);
        buttonProfileMessage = findViewById(R.id.buttonProfileMessage);
        profilePostAdapter = new ProfilePostAdapter(profilePostArrayList);
        profilePostsLayoutManager = new LinearLayoutManager(this);
        rV = findViewById(R.id.listViewProfilePost);
        rV.setLayoutManager(profilePostsLayoutManager);
        rV.setItemAnimator(new DefaultItemAnimator());
        rV.setAdapter(profilePostAdapter);
        selectedCategory = Constants.ACTIVITY;
        fab = findViewById(R.id.fabAddProfilePost);
        mAuth = FirebaseAuth.getInstance();
        userDocumentId = getIntent().getStringExtra(Constants.DOCUMENT_KEY);
        userRef = FirebaseFirestore.getInstance().collection(Constants.USERS_REF).document(userDocumentId);
    }

    private void followUI(){
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (userRef.getId().equals(mAuth.getUid())){
                    setButtonToFollowList();
                }else{
                    DocumentReference authUserRef = FirebaseFirestore.getInstance().collection(Constants.USERS_REF).document(mAuth.getUid());
                    authUserRef.collection(Constants.FOLLOWING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        System.err.println("Listen failed: " + e);
                                        return;
                                    }

                                    for (DocumentSnapshot document : querySnapshots) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        if (document.get(Constants.USER_ID).toString().equals(userRef.getId())){
                                            setButtonToYouAreFollowing();
                                            return;
                                        }
                                    }
                                }
                            });
                    buttonProfileFollow.setText("Follow");
                    setButtonToFollow();
                }
            }
        });
    }

    private void setButtonToFollowList(){
        buttonProfileFollow.setText("Connections");
        buttonProfileFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ConnectionsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setButtonToFollow() {
        buttonProfileFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                        DocumentReference newFollowing = FirebaseFirestore.getInstance().collection(Constants.USERS_REF)
                                .document(mAuth.getUid())
                                .collection(Constants.FOLLOWING).document();
                        DocumentReference user = FirebaseFirestore.getInstance().collection(Constants.USERS_REF)
                                .document(mAuth.getUid());
                        DocumentSnapshot u = transaction.get(user);

                        DocumentReference newFollower = FirebaseFirestore.getInstance().collection(Constants.USERS_REF)
                                .document(userRef.getId()).collection(Constants.FOLLOWERS).document();
                        DocumentReference profileUser = FirebaseFirestore.getInstance().collection(Constants.USERS_REF)
                                .document(userRef.getId());
                        DocumentSnapshot pU = transaction.get(profileUser);


                        // Put following data in current user
                        Map<String, Object> dataFollowing = new HashMap<>();
                        dataFollowing.put(Constants.DATE_CONNECTION_STARTED, FieldValue.serverTimestamp());
                        dataFollowing.put(Constants.USERNAME, pU.get(Constants.USERNAME));
                        dataFollowing.put(Constants.USER_ID, pU.getId());
                        transaction.set(newFollowing, dataFollowing);

                        int numFollowing = u.getLong(Constants.NUM_FOLLOWING).intValue() + 1;
                        transaction.update(user, Constants.NUM_FOLLOWING, numFollowing);

                        // Put follower data in profile user
                        Map<String, Object> dataFollower = new HashMap<>();
                        dataFollower.put(Constants.DATE_CONNECTION_STARTED, FieldValue.serverTimestamp());
                        dataFollower.put(Constants.USERNAME, mAuth.getCurrentUser().getDisplayName());
                        dataFollower.put(Constants.USER_ID, mAuth.getUid());
                        transaction.set(newFollower, dataFollower);

                        int numFollowers = pU.getLong(Constants.NUM_FOLLOWERS).intValue() + 1;
                        transaction.update(profileUser, Constants.NUM_FOLLOWERS, numFollowers);



                        return null;
                    }
                })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: User successfully added to following collection!");
                                Toast.makeText(getApplicationContext(), "User Successfully Added to Following!", Toast.LENGTH_SHORT).show();
                                setButtonToYouAreFollowing();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Exception", "Could not follow user: $exception");
                            }
                        });
            }
        });
    }

    private void setButtonToYouAreFollowing(){
        buttonProfileFollow.setText("You are following this user");
        buttonProfileFollow.setOnClickListener(null);
    }

    private void updateUI() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                textViewProfileUsername.setText(document.get(Constants.USERNAME).toString());
                textViewProfileCity.setText(document.get(Constants.CITY).toString() + ",");
                textViewProfileState.setText(document.get(Constants.STATE).toString());
                textViewProfileBio.setText(document.get(Constants.BIO).toString());
                textViewProfileFirstName.setText(document.get(Constants.FIRSTNAME).toString());
                textViewProfileLastName.setText(document.get(Constants.LASTNAME).toString());
                textViewProfileNumFollowers.setText(document.get(Constants.NUM_FOLLOWERS).toString());
                textViewProfileNumFollowing.setText(document.get(Constants.NUM_FOLLOWING).toString());
            }
        });
    }

    private void setProfilePostsListener() {
        if (selectedCategory == Constants.SONGS) {
            profilePostsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(userDocumentId)
                    .collection(Constants.PROFILE_POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(Constants.CATEGORY, selectedCategory)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            profilePostArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String profilePostTxt = (String) document.get(Constants.PROFILE_POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);
                                String category = (String) document.get(Constants.CATEGORY);

                                ProfilePost newProfilePost = new ProfilePost(category, username, timestamp,
                                        profilePostTxt, numLikes, numComments, documentId, userId);

                                profilePostArrayList.add(newProfilePost);
                            }

                            profilePostAdapter.notifyDataSetChanged();
                        }
                    });
        } else if (selectedCategory == Constants.SCHEDULE) {
            profilePostsListenerRegistration = db.collection(Constants.USERS_REF).document(userDocumentId).collection(Constants.PROFILE_POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(Constants.CATEGORY, selectedCategory)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            profilePostArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String profilePostTxt = (String) document.get(Constants.PROFILE_POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);
                                String category = (String) document.get(Constants.CATEGORY);

                                ProfilePost newProfilePost = new ProfilePost(category, username, timestamp,
                                        profilePostTxt, numLikes, numComments, documentId, userId);

                                profilePostArrayList.add(newProfilePost);
                            }
                            profilePostAdapter.notifyDataSetChanged();
                        }
                    });
        } else if (selectedCategory == Constants.SOCIAL) {
            profilePostsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(userDocumentId)
                    .collection(Constants.PROFILE_POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(Constants.CATEGORY, selectedCategory)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            profilePostArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String profilePostTxt = (String) document.get(Constants.PROFILE_POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);
                                String category = (String) document.get(Constants.CATEGORY);

                                ProfilePost newProfilePost = new ProfilePost(category, username, timestamp,
                                        profilePostTxt, numLikes, numComments, documentId, userId);

                                profilePostArrayList.add(newProfilePost);
                            }
                            profilePostAdapter.notifyDataSetChanged();
                        }
                    });
        } else if (selectedCategory == Constants.MERCH) {
            profilePostsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(userDocumentId)
                    .collection(Constants.PROFILE_POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(Constants.CATEGORY, selectedCategory)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            profilePostArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String profilePostTxt = (String) document.get(Constants.PROFILE_POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);
                                String category = (String) document.get(Constants.CATEGORY);

                                ProfilePost newProfilePost = new ProfilePost(category, username, timestamp,
                                        profilePostTxt, numLikes, numComments, documentId, userId);

                                profilePostArrayList.add(newProfilePost);
                            }
                            profilePostAdapter.notifyDataSetChanged();
                        }
                    });
        } else{
            profilePostsListenerRegistration = db.collection(Constants.USERS_REF)
                    .document(userDocumentId)
                    .collection(Constants.PROFILE_POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            profilePostArrayList.clear();
                            for (DocumentSnapshot document : snapshots) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String profilePostTxt = (String) document.get(Constants.PROFILE_POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);
                                String category = (String) document.get(Constants.CATEGORY);

                                ProfilePost newProfilePost = new ProfilePost(category, username, timestamp,
                                        profilePostTxt, numLikes, numComments, documentId, userId);

                                profilePostArrayList.add(newProfilePost);
                            }
                            profilePostAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void setButtons() {
        toggleButtonProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClicked();
            }
        });
        toggleButtonProfileSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsClicked();
            }
        });
        toggleButtonProfileSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleClicked();
            }
        });
        toggleButtonProfileSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialClicked();
            }
        });
        toggleButtonProfileMerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchClicked();
            }
        });
    }

    public void resetListener() {
        profilePostsListenerRegistration.remove();
        setProfilePostsListener();
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddProfilePostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void activityClicked() {
        if (selectedCategory == Constants.ACTIVITY) {
            toggleButtonProfileActivity.setChecked(true);
            return;
        }
        toggleButtonProfileSongs.setChecked(false);
        toggleButtonProfileSchedule.setChecked(false);
        toggleButtonProfileSocial.setChecked(false);
        toggleButtonProfileMerch.setChecked(false);
        selectedCategory = Constants.ACTIVITY;
        resetListener();
    }

    private void songsClicked() {
        if (selectedCategory == Constants.SONGS) {
            toggleButtonProfileSongs.setChecked(true);
            return;
        }
        toggleButtonProfileActivity.setChecked(false);
        toggleButtonProfileSchedule.setChecked(false);
        toggleButtonProfileSocial.setChecked(false);
        toggleButtonProfileMerch.setChecked(false);
        selectedCategory = Constants.SONGS;
        resetListener();
    }

    private void scheduleClicked() {
        if (selectedCategory == Constants.SCHEDULE) {
            toggleButtonProfileSchedule.setChecked(true);
            return;
        }
        toggleButtonProfileSongs.setChecked(false);
        toggleButtonProfileActivity.setChecked(false);
        toggleButtonProfileSocial.setChecked(false);
        toggleButtonProfileMerch.setChecked(false);
        selectedCategory = Constants.SCHEDULE;
        resetListener();
    }

    private void socialClicked() {
        if (selectedCategory == Constants.SOCIAL) {
            toggleButtonProfileSocial.setChecked(true);
            return;
        }
        toggleButtonProfileSongs.setChecked(false);
        toggleButtonProfileSchedule.setChecked(false);
        toggleButtonProfileActivity.setChecked(false);
        toggleButtonProfileMerch.setChecked(false);
        selectedCategory = Constants.SOCIAL;
        resetListener();
    }

    private void merchClicked() {
        if (selectedCategory == Constants.MERCH) {
            toggleButtonProfileMerch.setChecked(true);
            return;
        }
        toggleButtonProfileSongs.setChecked(false);
        toggleButtonProfileSchedule.setChecked(false);
        toggleButtonProfileSocial.setChecked(false);
        toggleButtonProfileActivity.setChecked(false);
        selectedCategory = Constants.MERCH;
        resetListener();
    }
}
