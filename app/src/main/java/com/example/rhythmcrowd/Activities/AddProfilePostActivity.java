package com.example.rhythmcrowd.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddProfilePostActivity extends AppCompatActivity {

    private ToggleButton toggleButtonAddProfilePostSong;
    private ToggleButton toggleButtonAddProfilePostSchedule;
    private ToggleButton toggleButtonAddProfilePostMerch;
    private ToggleButton toggleButtonAddProfilePostSocial;
    private EditText editTextAddProfilePostTxt;
    private Button buttonAddProfilePost;
    private String selectedCategory;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_post);
        initializeAttributes();
        setAddPostButton();
        setToggle();
    }

    private void initializeAttributes() {
        editTextAddProfilePostTxt = findViewById(R.id.editTextAddProfilePostTxt);
        toggleButtonAddProfilePostMerch = findViewById(R.id.toggleButtonAddProfilePostMerch);
        toggleButtonAddProfilePostSong = findViewById(R.id.toggleButtonAddProfilePostSong);
        toggleButtonAddProfilePostSchedule = findViewById(R.id.toggleButtonAddProfileProfilePostSchedule);
        toggleButtonAddProfilePostSocial = findViewById(R.id.toggleButtonAddProfilePostSocial);
        buttonAddProfilePost = findViewById(R.id.buttonAddProfilePost);
        selectedCategory = Constants.SONGS;
        mAuth = FirebaseAuth.getInstance();
    }

    private void setAddPostButton() {
        buttonAddProfilePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostClicked();
            }
        });
    }

    private void addPostClicked() {
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                String profilePostTxt = editTextAddProfilePostTxt.getText().toString();

                DocumentReference newProfilePostRef = FirebaseFirestore.getInstance().collection(Constants.USERS_REF)
                        .document(mAuth.getUid())
                        .collection(Constants.PROFILE_POST_REF).document();

                Map<String, Object> data = new HashMap<>();
                data.put(Constants.CATEGORY, selectedCategory);
                data.put(Constants.PROFILE_POST_TXT, profilePostTxt);
                data.put(Constants.TIMESTAMP, FieldValue.serverTimestamp());
                data.put(Constants.USERNAME, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                data.put(Constants.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.put(Constants.NUM_COMMENTS, 0);
                data.put(Constants.NUM_LIKES, 0);
                transaction.set(newProfilePostRef, data);
                return null;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Post successfully added!");
                        Toast.makeText(getApplicationContext(), "Profile Post Successfully Added!", Toast.LENGTH_SHORT).show();
                        editTextAddProfilePostTxt.getText().clear();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Exception", "Could not add profile post: $exception");
                    }
                });
    }


    private void setToggle() {
        toggleButtonAddProfilePostSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSongClicked();
            }
        });

        toggleButtonAddProfilePostSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScheduleClicked();
            }
        });

        toggleButtonAddProfilePostMerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMerchClicked();
            }
        });

        toggleButtonAddProfilePostSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSocialClicked();
            }
        });
    }
    public void addSongClicked()
    {
        if(selectedCategory == Constants.SONGS)
        {
            toggleButtonAddProfilePostSong.setChecked(true);
            return;
        }

        toggleButtonAddProfilePostSchedule.setChecked(false);
        toggleButtonAddProfilePostMerch.setChecked(false);
        toggleButtonAddProfilePostSocial.setChecked(false);
        selectedCategory = Constants.SONGS;
    }

    public void addScheduleClicked()
    {
        if(selectedCategory == Constants.SCHEDULE)
        {
            toggleButtonAddProfilePostSchedule.setChecked(true);
            return;
        }

        toggleButtonAddProfilePostSong.setChecked(false);
        toggleButtonAddProfilePostMerch.setChecked(false);
        toggleButtonAddProfilePostSocial.setChecked(false);
        selectedCategory = Constants.SCHEDULE;
    }

    public void addMerchClicked()
    {
        if(selectedCategory == Constants.MERCH)
        {
            toggleButtonAddProfilePostMerch.setChecked(true);
            return;
        }

        toggleButtonAddProfilePostSong.setChecked(false);
        toggleButtonAddProfilePostSchedule.setChecked(false);
        toggleButtonAddProfilePostSocial.setChecked(false);
        selectedCategory = Constants.MERCH;
    }

    public void addSocialClicked()
    {
        if(selectedCategory == Constants.SOCIAL)
        {
            toggleButtonAddProfilePostSocial.setChecked(true);
            return;
        }

        toggleButtonAddProfilePostSchedule.setChecked(false);
        toggleButtonAddProfilePostSong.setChecked(false);
        toggleButtonAddProfilePostMerch.setChecked(false);
        selectedCategory = Constants.SOCIAL;
    }
}
