package com.example.rhythmcrowd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Model.Comment;
import com.example.rhythmcrowd.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private ArrayList<Comment> commentArrayList;

    public CommentsAdapter(ArrayList<Comment> commentArrayList)
    {
        this.commentArrayList = commentArrayList;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        private TextView commentListUsername;
        private TextView commentListComment;
        private TextView commentListDate;
        private ImageView commentListOptions;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            commentListComment = itemView.findViewById(R.id.textViewCommentListComment);
            commentListUsername = itemView.findViewById(R.id.textViewCommentListUsername);
            commentListDate = itemView.findViewById(R.id.textViewCommentListTimeStamp);
            commentListOptions = itemView.findViewById(R.id.imageViewCommentOptions);
        }
    }

    @NonNull
    @Override
    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_view, parent, false);
        return new CommentsAdapter.CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentsViewHolder holder, int position) {
        final Comment comment = commentArrayList.get(position);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, h:mma",
                Locale.getDefault());
        String dateString = dateFormatter.format(comment.getTimestamp());
        holder.commentListDate.setText(dateString);
        holder.commentListUsername.setText(comment.getUsername());
        holder.commentListComment.setText(comment.getCommentTxt());
        holder.commentListOptions.setVisibility(View.GONE);

        /*
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() == comment.getUserId()){
            holder.commentListOptions.setVisibility(View.VISIBLE);
        };
        */
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }
}
