package com.JuanCast.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private final List<FAQItem> faqList;
    private int expandedPosition = -1;

    public FAQAdapter(List<FAQItem> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faqItem = faqList.get(position);
        holder.questionTextView.setText(faqItem.getQuestion());
        holder.answerTextView.setText(faqItem.getAnswer());

        // Determine if the current position is expanded
        boolean isExpanded = position == expandedPosition;
        holder.answerTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Set click listener for questionTextView to toggle expansion
        holder.questionTextView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (expandedPosition == currentPosition) {
                    // Collapse the currently expanded item if it's clicked again
                    expandedPosition = -1;
                } else {
                    // Expand the new item and collapse the previously expanded one if any
                    int previousExpandedPosition = expandedPosition;
                    expandedPosition = currentPosition;
                    if (previousExpandedPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                }
                notifyItemChanged(currentPosition);
            }
        });

        // Set click listeners for other views if necessary (like about4, about5)
        holder.about4.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (expandedPosition == currentPosition) {
                    expandedPosition = -1; // Collapse if currently expanded
                } else {
                    int previousExpandedPosition = expandedPosition;
                    expandedPosition = currentPosition;
                    if (previousExpandedPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                }
                notifyItemChanged(currentPosition);
            }
        });

        holder.about5.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (expandedPosition == currentPosition) {
                    expandedPosition = -1; // Collapse if currently expanded
                } else {
                    int previousExpandedPosition = expandedPosition;
                    expandedPosition = currentPosition;
                    if (previousExpandedPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousExpandedPosition);
                    }
                }
                notifyItemChanged(currentPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        final TextView questionTextView;
        final TextView answerTextView;
        private LinearLayout about4;
        private ImageView about5;

        FAQViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.textViewQuestion);
            answerTextView = itemView.findViewById(R.id.textViewAnswer);
            about4 = itemView.findViewById(R.id.about4);
            about5 = itemView.findViewById(R.id.about5);
        }
    }
}
