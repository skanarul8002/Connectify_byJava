package com.anarul.skwhatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anarul.skwhatsapp.Models.Status;
import com.anarul.skwhatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private ArrayList<Status> statusList;
    private Context context;
    private StatusDeleteListener statusDeleteListener;

    public interface StatusDeleteListener {
        void onDeleteStatus(Status status);
    }

    public StatusAdapter(ArrayList<Status> statusList, Context context, StatusDeleteListener statusDeleteListener) {
        this.statusList = statusList;
        this.context = context;
        this.statusDeleteListener = statusDeleteListener;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = statusList.get(position);
        Picasso.get().load(status.getImageUrl()).into(holder.statusImageView);
        holder.deleteButton.setOnClickListener(v -> statusDeleteListener.onDeleteStatus(status));
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImageView;
        ImageView deleteButton;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImageView = itemView.findViewById(R.id.status_image);
            deleteButton = itemView.findViewById(R.id.btn_delete_status);
        }
    }
}
