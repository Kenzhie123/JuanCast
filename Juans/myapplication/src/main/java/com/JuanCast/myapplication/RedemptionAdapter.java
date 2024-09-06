package com.JuanCast.myapplication;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RedemptionAdapter extends FirestoreRecyclerAdapter<Redemption, RedemptionAdapter.RedemptionHolder> {

    public RedemptionAdapter(@NonNull FirestoreRecyclerOptions<Redemption> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RedemptionHolder holder, int position, @NonNull Redemption model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public RedemptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_redemption, parent, false);
        return new RedemptionHolder(view);
    }

    static class RedemptionHolder extends RecyclerView.ViewHolder {

        private final TextView promoCodeTextView;
        private final TextView pointsAwardedTextView;
        private final TextView dateTextView;

        public RedemptionHolder(@NonNull View itemView) {
            super(itemView);

            promoCodeTextView = itemView.findViewById(R.id.promoCodeTextView);
            pointsAwardedTextView = itemView.findViewById(R.id.pointsAwardedTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }


        public void bind(Redemption redemption) {
            // Format and set text with bold labels

            promoCodeTextView.setText(getBoldLabelText("Promo Code: ", redemption.getPromoCode()));
            pointsAwardedTextView.setText(getBoldLabelText("Points Awarded: ", "+" + redemption.getPointsAwarded()));

            // Format the timestamp
            Timestamp timestamp = redemption.getDate();
            if (timestamp != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
                String formattedDate = sdf.format(timestamp.toDate());
                dateTextView.setText(getBoldLabelText("Date: ", formattedDate));
            } else {
                dateTextView.setText(getBoldLabelText("Date: ", "N/A"));
            }
        }

        private SpannableString getBoldLabelText(String label, String value) {
            SpannableString spannableString = new SpannableString(label + value);
            int labelEnd = label.length();
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, labelEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }
}
