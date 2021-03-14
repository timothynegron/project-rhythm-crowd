package com.example.rhythmcrowd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmcrowd.Model.User;
import com.example.rhythmcrowd.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ConnectionsViewHolder> implements View.OnClickListener {

    private ArrayList<User> connectionsArrayList;
    private OnClickListener mOnClickListener;

    public ConnectionsAdapter(ArrayList<User> connectionsArrayList, OnClickListener mOnClickListener){
        this.connectionsArrayList = connectionsArrayList;
        this.mOnClickListener = mOnClickListener;
    }

    public void setOnClickListener(OnClickListener listener){
        this.mOnClickListener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    public class ConnectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView listViewConnectionsUsername;
        private TextView listViewConnectionsDateStartedConnection;
        private OnClickListener onClickListener;

        public ConnectionsViewHolder(@NonNull View itemView, final OnClickListener onClickListener) {
            super(itemView);
            listViewConnectionsUsername = itemView.findViewById(R.id.listViewConnectionsUsername);
            listViewConnectionsDateStartedConnection = itemView.findViewById
                    (R.id.listViewConnectionsDateStartedConnection);
            this.onClickListener = onClickListener;

            listViewConnectionsUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.OnConnectionClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }


    public interface OnClickListener{
        void OnConnectionClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsAdapter.ConnectionsViewHolder holder, int position) {
        final User user = connectionsArrayList.get(position);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, h:mma",
                Locale.getDefault());
        String dateString = dateFormatter.format(user.getDateStartedConnection());
        holder.listViewConnectionsDateStartedConnection.setText(dateString);
        holder.listViewConnectionsUsername.setText(user.getUsername());
    }

    @NonNull
    @Override
    public ConnectionsAdapter.ConnectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connections_list_view, parent, false);
        return new ConnectionsAdapter.ConnectionsViewHolder(view, mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return connectionsArrayList.size();
    }
}
