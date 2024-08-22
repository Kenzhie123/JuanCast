package com.JuanCast.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<Reward> rewardList;

    public RewardAdapter(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewardList.get(position);
        holder.dateTextView.setText("Date: "+reward.getDate());
        holder.pointsTextView.setText(reward.getFormattedRewardPoints());
        holder.timeTextView.setText("Time: "+reward.getTime());
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView pointsTextView;
        TextView timeTextView;


        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            pointsTextView = itemView.findViewById(R.id.pointsTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);

        }
    }
}
