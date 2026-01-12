package com.example.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    public interface OnMusicClickListener {
        void onClick(String filename);
    }

    private ArrayList<String> list;
    private OnMusicClickListener listener;

    public MusicListAdapter(ArrayList<String> list, OnMusicClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filename = list.get(position);
        holder.txtName.setText(filename);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(filename);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
