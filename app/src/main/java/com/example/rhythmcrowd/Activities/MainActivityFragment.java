package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.Post;
import com.example.rhythmcrowd.Adapters.PostAdapter;
import com.example.rhythmcrowd.R;
import com.google.firebase.FirebaseApp;
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

public class MainActivityFragment extends Fragment implements PostAdapter.OnPostListener {

    private ToggleButton toggleButtonLatest;
    private ToggleButton toggleButtonPopular;
    private ToggleButton toggleButtonSearch;
    private ToggleButton toggleButtonTrending;
    private String selectedCategory;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PostAdapter postAdapter;
    private LinearLayoutManager postsLayoutManager;
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private RecyclerView rV;
    private ListenerRegistration postsListenerRegistration;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);
        initializeAttributes(view);
        setToggle();
        setRV(view);
        setPostsListener();

        return view;
    }

    public void setPostsListener() {
        if(selectedCategory == Constants.POPULAR)
        {
            postsListenerRegistration = db.collection(Constants.POST_REF)
                    .orderBy(Constants.NUM_LIKES, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e)
                        {
                            if(e != null) {
                                System.err.println("Listen failed: " +  e);
                                return;
                            }
                            postArrayList.clear();
                            for (DocumentSnapshot document : snapshots){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String postTxt = (String) document.get(Constants.POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);

                                Post newPost = new Post(username, timestamp,
                                        postTxt, numLikes, numComments, documentId, userId);

                                postArrayList.add(newPost);
                            }
                            postAdapter.notifyDataSetChanged();
                        }
                    });
        }
        else if(selectedCategory == Constants.TRENDING){
            postsListenerRegistration = db.collection(Constants.POST_REF)
                    .orderBy(Constants.NUM_COMMENTS, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e)
                        {
                            if(e != null) {
                                System.err.println("Listen failed: " +  e);
                                return;
                            }
                            postArrayList.clear();
                            for (DocumentSnapshot document : snapshots){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String postTxt = (String) document.get(Constants.POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);

                                Post newPost = new Post(username, timestamp,
                                        postTxt, numLikes, numComments, documentId, userId);

                                postArrayList.add(newPost);
                            }
                            postAdapter.notifyDataSetChanged();
                        }
                    });
        }
        else{
            postsListenerRegistration = db.collection(Constants.POST_REF)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e)
                        {
                            if(e != null) {
                                System.err.println("Listen failed: " +  e);
                                return;
                            }
                            postArrayList.clear();
                            for (DocumentSnapshot document : snapshots){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String username = (String) document.get(Constants.USERNAME);
                                String postTxt = (String) document.get(Constants.POST_TXT);
                                int numLikes = document.getLong(Constants.NUM_LIKES).intValue();
                                int numComments = document.getLong(Constants.NUM_COMMENTS).intValue();
                                Date timestamp = document.getDate(Constants.TIMESTAMP);
                                String documentId = document.getId();
                                String userId = (String) document.get(Constants.USER_ID);

                                Post newPost = new Post(username, timestamp,
                                        postTxt, numLikes, numComments, documentId, userId);

                                postArrayList.add(newPost);
                            }
                            postAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public void setRV(View view) {
        rV = view.findViewById(R.id.listViewPost);
        rV.setLayoutManager(postsLayoutManager);
        rV.setItemAnimator(new DefaultItemAnimator());
        rV.setAdapter(postAdapter);
    }

    public void initializeAttributes(View view) {
        selectedCategory = Constants.LATEST;
        toggleButtonLatest = view.findViewById(R.id.toggleButtonLatest);
        toggleButtonPopular = view.findViewById(R.id.toggleButtonPopular);
        toggleButtonSearch = view.findViewById(R.id.toggleButtonSearch);
        toggleButtonTrending = view.findViewById(R.id.toggleButtonTrending);
        postAdapter = new PostAdapter(postArrayList, this);
        postsLayoutManager = new LinearLayoutManager(getActivity());
        FirebaseApp.initializeApp(getActivity());
    }

    public void setToggle() {
        toggleButtonLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestToggleClicked();
            }
        });

        toggleButtonTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trendingToggleClicked();
            }
        });

        toggleButtonPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularToggleClicked();
            }
        });

        toggleButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchToggleClicked();
            }
        });
    }

    public void latestToggleClicked() {
        if(selectedCategory == Constants.LATEST)
        {
            toggleButtonLatest.setChecked(true);
            return;
        }

        toggleButtonPopular.setChecked(false);
        toggleButtonSearch.setChecked(false);
        toggleButtonTrending.setChecked(false);
        selectedCategory = Constants.LATEST;
        resetListener();
    }

    public  void trendingToggleClicked(){
        if(selectedCategory == Constants.TRENDING)
        {
            toggleButtonTrending.setChecked(true);
            return;
        }

        toggleButtonPopular.setChecked(false);
        toggleButtonSearch.setChecked(false);
        toggleButtonLatest.setChecked(false);
        selectedCategory = Constants.TRENDING;
        resetListener();
    }

    public void popularToggleClicked() {
        if(selectedCategory == Constants.POPULAR)
        {
            toggleButtonPopular.setChecked(true);
            return;
        }

        toggleButtonLatest.setChecked(false);
        toggleButtonSearch.setChecked(false);
        toggleButtonTrending.setChecked(false);
        selectedCategory = Constants.POPULAR;
        resetListener();
    }

    public void searchToggleClicked() {
        if(selectedCategory == Constants.SEARCH)
        {
            toggleButtonSearch.setChecked(true);
            return;
        }

        toggleButtonLatest.setChecked(false);
        toggleButtonPopular.setChecked(false);
        toggleButtonTrending.setChecked(false);
        selectedCategory = Constants.SEARCH;

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    public void resetListener() {
        postsListenerRegistration.remove();
        setPostsListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        latestToggleClicked();
        toggleButtonLatest.setChecked(true);
    }

    @Override
    public void OnPostTxtClick(int position) {
        Post post = postArrayList.get(position);
        Intent intent = new Intent(getActivity(), AddCommentsActivity.class);
        intent.putExtra(Constants.DOCUMENT_KEY, post.getDocumentId());
        startActivity(intent);
    }

    @Override
    public void OnUsernameClick(int position) {
        Post post = postArrayList.get(position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(Constants.DOCUMENT_KEY, post.getUserId());
        startActivity(intent);
    }

    @Override
    public void OnPostImageClick(int position) {
        Post post = postArrayList.get(position);
        Intent intent = new Intent(getActivity(), AddCommentsActivity.class);
        intent.putExtra(Constants.DOCUMENT_KEY, post.getDocumentId());
        startActivity(intent);
    }
}
