package com.example.rhythmcrowd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Model.ProfilePost;
import com.example.rhythmcrowd.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProfilePostAdapter extends RecyclerView.Adapter <ProfilePostAdapter.ProfilePostViewHolder> {
    private ArrayList<ProfilePost> profilePostArrayList;

    public ProfilePostAdapter(ArrayList<ProfilePost> profilePostArrayList){
        this.profilePostArrayList = profilePostArrayList;
    }

    @NonNull
    @Override
    public ProfilePostAdapter.ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_post_list_view, parent, false);
        return new ProfilePostAdapter.ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostAdapter.ProfilePostViewHolder holder, int position) {
        final ProfilePost profilePost = profilePostArrayList.get(position);
        holder.listViewPostTxt.setText(profilePost.getProfilePostTxt());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, h:mma",
                Locale.getDefault());
        String dateString = dateFormatter.format(profilePost.getTimestamp());
        holder.listViewDate.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return profilePostArrayList.size();
    }

    public class ProfilePostViewHolder extends RecyclerView.ViewHolder{
        private TextView listViewPostTxt;
        private TextView listViewDate;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);
            listViewDate = itemView.findViewById(R.id.listViewProfilePostDate);
            listViewPostTxt = itemView.findViewById(R.id.listViewProfilePostTxt);
        }
    }
}
