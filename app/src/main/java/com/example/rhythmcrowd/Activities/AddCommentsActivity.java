package com.example.rhythmcrowd.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.rhythmcrowd.Adapters.CommentsAdapter;
import com.example.rhythmcrowd.Model.Comment;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddCommentsActivity extends AppCompatActivity {

    // Attributes
    private EditText editTextWriteComment;
    private ImageButton imageButtonAddComment;
    private String postDocumentId;
    private ArrayList<Comment> commentsArrayList = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private LinearLayoutManager commentsLayoutManager;
    private RecyclerView rV;

    //**********************************************************************************************
    // Method: onCreate(Bundle savedInstanceState)
    //
    // Purpose: Apart of Android OS Activity Lifecycle. Overriding the method to display
    // activity comments xml file. Initializes attributes and sets button functionality.
    //
    //
    //**********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initializeAttributes();
        setButtons();

        FirebaseFirestore.getInstance().collection(Constants.POST_REF).document(postDocumentId)
                .collection(Constants.COMMENTS_REF)
                .orderBy(Constants.TIMESTAMP, Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    System.err.println("Listen failed: " +  e);
                    return;
                }

                if(querySnapshot != null){
                    commentsArrayList.clear();
                    for (DocumentSnapshot document : querySnapshot){
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        String username = (String) document.get(Constants.USERNAME);
                        String commentTxt = (String) document.get(Constants.COMMENT_TXT);
                        Date timestamp = document.getDate(Constants.TIMESTAMP);
                        String documentId = document.getId();
                        String userId = document.getString(Constants.USER_ID);

                        Comment newComment = new Comment(username, timestamp, commentTxt, documentId, userId);

                        commentsArrayList.add(newComment);
                    }
                    commentsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    private void initializeAttributes() {
        editTextWriteComment = findViewById(R.id.editTextWriteComment);
        imageButtonAddComment = findViewById(R.id.imageButtonAddComment);
        commentsAdapter = new CommentsAdapter(commentsArrayList);
        commentsLayoutManager = new LinearLayoutManager(this);
        rV = findViewById(R.id.commentListView);
        rV.setLayoutManager(commentsLayoutManager);
        rV.setItemAnimator(new DefaultItemAnimator());
        rV.setAdapter(commentsAdapter);
        postDocumentId = getIntent().getStringExtra(Constants.DOCUMENT_KEY);
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
        imageButtonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentClicked();
            }
        });
    }

    //**********************************************************************************************
    //Method: addCommentClicked()
    //
    //Purpose: To add a comment to Cloud Firestore when clicked.
    //
    //
    //**********************************************************************************************
    public void addCommentClicked(){
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                String commentTxt = editTextWriteComment.getText().toString();
                DocumentReference postRef = FirebaseFirestore.getInstance().collection(Constants.POST_REF).document(postDocumentId);
                DocumentSnapshot post = transaction.get(postRef);
                int numComments = post.getLong(Constants.NUM_COMMENTS).intValue() + 1;
                transaction.update(postRef, Constants.NUM_COMMENTS, numComments);

                DocumentReference newCommentRef = FirebaseFirestore.getInstance().collection(Constants.POST_REF)
                        .document(postDocumentId)
                        .collection(Constants.COMMENTS_REF).document();

                Map<String, Object> data = new HashMap<>();
                data.put(Constants.COMMENT_TXT, commentTxt);
                data.put(Constants.TIMESTAMP, FieldValue.serverTimestamp());
                data.put(Constants.USERNAME, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                data.put(Constants.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());

                transaction.set(newCommentRef, data);

                return null;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideKeyboard();
                    }
                });
    }

    //**********************************************************************************************
    //Method: hideKeyboard()
    //
    //Purpose: To hide the keyboard after adding a comment.
    //
    //
    //**********************************************************************************************
    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        editTextWriteComment.getText().clear();
    }

}
