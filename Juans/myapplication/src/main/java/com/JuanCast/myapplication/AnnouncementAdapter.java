package com.JuanCast.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
    private List<AnnouncementList> announcementList;

    public AnnouncementAdapter(List<AnnouncementList> announcementList) {
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        AnnouncementList announcement = announcementList.get(position);
        holder.announcementText.setText(announcement.getText());
        holder.announcementDate.setText(announcement.getDate());
        holder.announcementTime.setText(announcement.getTime());

    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView announcementText;
        TextView announcementDate;
        TextView announcementTime;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementText = itemView.findViewById(R.id.announcement_text);
            announcementDate = itemView.findViewById(R.id.announcement_date);
            announcementTime = itemView.findViewById(R.id.announcement_time);

        }
    }
}
