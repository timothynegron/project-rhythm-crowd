package com.example.rhythmcrowd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.Model.Post;
import com.example.rhythmcrowd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements View.OnClickListener {

    private ArrayList<Post> postArrayList;
    private OnPostListener mOnPostListener;
    private FirebaseAuth mAuth;

    public PostAdapter(ArrayList<Post> postArrayList, OnPostListener mOnPostListener)
    {
        this.postArrayList = postArrayList;
        this.mOnPostListener = mOnPostListener;
    }

    @Override
    public void onClick(View v) {

    }

    public void setOnClickListener(OnPostListener listener){
        this.mOnPostListener = listener;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView listViewUsername;
        private TextView listViewPostTxt;
        private TextView listViewDate;
        private TextView listViewNumLikesLabel;
        private TextView listViewNumCommentsLabel;
        private ImageView listViewLikesImage;
        private ImageView listViewNumCommentsImage;
        private ImageView listViewOptions;

        // Step 3: comments
        private OnPostListener onPostListener;

        public PostViewHolder(@NonNull View itemView, final OnPostListener onPostListener) {
            super(itemView);
            // Initialize attributes of class here
            listViewUsername = itemView.findViewById(R.id.textViewUsername);
            listViewPostTxt = itemView.findViewById(R.id.textViewPost);
            listViewDate = itemView.findViewById(R.id.textViewDate);
            listViewNumLikesLabel = itemView.findViewById(R.id.textViewPostNumLike);
            listViewNumCommentsLabel = itemView.findViewById(R.id.textViewPostNumComments);
            listViewLikesImage = itemView.findViewById(R.id.imageViewPostLike);
            listViewNumCommentsImage = itemView.findViewById(R.id.imageViewPostComment);
            listViewOptions = itemView.findViewById(R.id.imageViewPostOptions);
            this.onPostListener = onPostListener;

            itemView.setOnClickListener(this);

            // Step 3: comments
            listViewUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPostListener.OnUsernameClick(getAdapterPosition());
                }
            });

            listViewPostTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPostListener.OnPostTxtClick(getAdapterPosition());
                }
            });

            listViewNumCommentsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPostListener.OnPostImageClick(getAdapterPosition());
                }
            });

        }


        @Override
        public void onClick(View v) {
            //onPostListener.OnPostClick(getAdapterPosition());
        }
    }

    // Step 1: comments
    public interface OnPostListener{
        void OnPostTxtClick(int position);
        void OnUsernameClick(int position);
        void OnPostImageClick(int position);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_view, parent, false);
        return new PostViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final Post post = postArrayList.get(position);
        holder.listViewUsername.setText(post.getUsername());
        holder.listViewPostTxt.setText(post.getPostTxt());
        holder.listViewNumLikesLabel.setText(Integer.toString(post.getNumLikes()));
        holder.listViewNumCommentsLabel.setText(Integer.toString(post.getNumComments()));
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, h:mma",
                Locale.getDefault());
        String dateString = dateFormatter.format(post.getTimestamp());
        holder.listViewDate.setText(dateString);
        holder.listViewLikesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection(Constants.POST_REF)
                        .document(post.getDocumentId())
                        .update(Constants.NUM_LIKES, post.getNumLikes() + 1);
            }
        });


        holder.listViewOptions.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
}
