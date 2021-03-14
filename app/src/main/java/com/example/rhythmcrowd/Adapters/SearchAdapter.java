package com.example.rhythmcrowd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Model.User;
import com.example.rhythmcrowd.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<User> usersArrayList;

    public SearchAdapter(ArrayList<User> usersArrayList){
        this.usersArrayList = usersArrayList;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private TextView listViewSearchUsername;
        private TextView listViewSearchBio;
        private TextView listViewSearchCity;
        private TextView listViewSearchState;
        private TextView listViewSearchNumFollowers;
        private TextView listViewSearchNumFollowing;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            listViewSearchUsername = itemView.findViewById(R.id.listViewSearchUsername);
            listViewSearchBio = itemView.findViewById(R.id.listViewSearchBio);
            listViewSearchCity = itemView.findViewById(R.id.listViewSearchCity);
            listViewSearchState = itemView.findViewById(R.id.listViewSearchState);
            listViewSearchNumFollowers = itemView.findViewById(R.id.listViewSearchNumFollowers);
            listViewSearchNumFollowing = itemView.findViewById(R.id.listViewSearchNumFollowing);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        final User user = usersArrayList.get(position);
        holder.listViewSearchUsername.setText(user.getUsername());
        holder.listViewSearchBio.setText(user.getBio());
        holder.listViewSearchCity.setText(user.getCity() + ",");
        holder.listViewSearchState.setText(user.getState());
        holder.listViewSearchNumFollowing.setText(Integer.toString(user.getNumFollowing()));
        holder.listViewSearchNumFollowers.setText(Integer.toString(user.getNumFollowers()));
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_view, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }


}
