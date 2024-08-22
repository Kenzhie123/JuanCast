package com.JuanCast.myapplication.listadapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.JuanCast.myapplication.R;
import com.JuanCast.myapplication.Tools;
import com.JuanCast.myapplication.models.ArtistVotes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VSArtistVoteListAdapter extends RecyclerView.Adapter<VSArtistVoteListAdapter.VSArtistVoteListViewHolder>{

    Context context;
    ArrayList<ArtistVotes> artistVotesList;

    FirebaseStorage storage;
    String userID;
    String pollID;
    String pollType;
    boolean pollEnded;
    public VSArtistVoteListAdapter(
            Context context,
            ArrayList<ArtistVotes> artistVotesList,
            String userID,
            String pollID,
            String pollType,
            boolean pollEnded)
    {
        this.context = context;
        this.artistVotesList = artistVotesList;
        this.userID = userID;
        this.pollID = pollID;
        this.pollType = pollType;
        this.pollEnded = pollEnded;
        storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public VSArtistVoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VSArtistVoteListViewHolder(LayoutInflater.from(context).inflate(R.layout.vs_artist_vote_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VSArtistVoteListViewHolder holder, int position) {
        ArtistVotes specArtist = artistVotesList.get(position);
        holder.VSAV_ArtistsName.setText(specArtist.getArtistName());
        holder.VSAV_StarVotesCount.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(specArtist.getStarVotes())));
        holder.VSAV_SunVotesCount.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(specArtist.getSunVotes())));
        holder.VSAV_RankNumber.setText(String.valueOf(position + 1));

        Long voteAmount = 0l;
        if(pollType.equals("Major")) {
            holder.VSAV_StarIcon.setVisibility(View.GONE);
            holder.VSAV_StarVotesCount.setVisibility(View.GONE);
            voteAmount += specArtist.getSunVotes();
        }
        else
        {
            holder.VSAV_SunIcon.setVisibility(View.GONE);
            holder.VSAV_SunVotesCount.setVisibility(View.GONE);
            voteAmount += specArtist.getStarVotes();
        }


        storage.getReference().child("artists").child(specArtist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.VSAV_ProfileProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.VSAV_ProfileProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.VSAV_Profile);
            }
        });

        Long finalVoteAmount = voteAmount;
        holder.VSAV_CastVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollEnded)
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("User").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String,Object> data = documentSnapshot.getData();

                            Dialog dialog = new Dialog(context,R.style.CastDialog);
                            dialog.setContentView(R.layout.cast_vote_popup);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);

                            Button CVP_CloseButton = dialog.findViewById(R.id.CVP_CloseButton);
                            TextView CVP_ArtistName = dialog.findViewById(R.id.CVP_ArtistName);
                            ImageView CVP_ArtistProfile = dialog.findViewById(R.id.CVP_ArtistProfile);
                            TextView CVP_ArtistVoteCount = dialog.findViewById(R.id.CVP_ArtistVoteCount);
                            TextView CVP_AvailableSunStarText = dialog.findViewById(R.id.CVP_AvailableSunStarText);
                            TextView CVP_HowManyCastText = dialog.findViewById(R.id.CVP_HowManyCastText);
                            TextView CVP_AvailableSunStarIcon = dialog.findViewById(R.id.CVP_AvailableSunStarIcon);
                            TextView CVP_AvailableSunStarCount = dialog.findViewById(R.id.CVP_AvailableSunStarCount);
                            TextView CVP_VoteCountField = dialog.findViewById(R.id.CVP_VoteCountField);
                            CardView CVP_CastVoteButton = dialog.findViewById(R.id.CVP_CastVoteButton);
                            ProgressBar CVP_ProfileProgressBar = dialog.findViewById(R.id.CVP_ProfileProgressBar);
                            TextView CVP_Overlay = dialog.findViewById(R.id.CVP_Overlay);
                            ProgressBar CVP_ProgressBar = dialog.findViewById(R.id.CVP_ProgressBar);

                            Long sunCount = ((Long)data.get("sunvotingpoints"));
                            Long starCount = ((Long)data.get("votingPoints"));

                            CVP_ArtistName.setText(specArtist.getArtistName());
                            String voteType = (pollType.equals("Minor")? "STARS" : "SUNS");

                            if(pollType.equals("Minor"))
                            {
                                CVP_ArtistVoteCount.setText(String.valueOf(specArtist.getStarVotes()));
                                CVP_AvailableSunStarCount.setText(String.valueOf(starCount));

                                CVP_HowManyCastText.setText(CVP_HowManyCastText.getText().toString().replace("_VOTETYPE_",voteType));
                                CVP_AvailableSunStarText.setText(CVP_AvailableSunStarText.getText().toString().replace("_VOTETYPE_",voteType));
                                CVP_AvailableSunStarIcon.setBackground(context.getResources().getDrawable(R.drawable.star));
                                CVP_VoteCountField.setHint(CVP_VoteCountField.getHint().toString().replace("_VOTETYPE_",voteType));
                            }
                            else if(pollType.equals("Major"))
                            {
                                CVP_ArtistVoteCount.setText(String.valueOf(specArtist.getSunVotes()));
                                CVP_AvailableSunStarCount.setText(String.valueOf(sunCount));

                                CVP_HowManyCastText.setText(CVP_HowManyCastText.getText().toString().replace("_VOTETYPE_",voteType));
                                CVP_AvailableSunStarText.setText(CVP_AvailableSunStarText.getText().toString().replace("_VOTETYPE_",voteType));
                                CVP_AvailableSunStarIcon.setBackground(context.getResources().getDrawable(R.drawable.suns));
                                CVP_VoteCountField.setHint(CVP_VoteCountField.getHint().toString().replace("_VOTETYPE_",voteType));
                            }

                            CVP_CloseButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            storage.getReference().child("artists").child(specArtist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true).addListener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    CVP_ProfileProgressBar.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    CVP_ProfileProgressBar.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            }).into(CVP_ArtistProfile);
                                }
                            });


                            CVP_CastVoteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Long availableCurrency = (Long.parseLong(CVP_AvailableSunStarCount.getText().toString()));
                                    Long currencyToCast = Long.parseLong(CVP_VoteCountField.getText().toString());

                                    CVP_Overlay.setVisibility(View.VISIBLE);
                                    CVP_ProgressBar.setVisibility(View.VISIBLE);
                                    if(currencyToCast > availableCurrency)
                                    {
                                        String toastMessage = "You do not have enough" + voteType.toLowerCase() + "to cast your vote";
                                        Toast.makeText(context,toastMessage,Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        final int[] updateCount = {0};
                                        Map<String,Object> pollUpdate = new HashMap<>();
                                        pollUpdate.put((pollType.equals("Minor")? "star_votes" : "sun_votes"), finalVoteAmount +Long.parseLong(CVP_VoteCountField.getText().toString()));
                                        db.collection("voting_polls")
                                                .document(pollID)
                                                .collection("votes")
                                                .document(specArtist.getArtistID())
                                                .update(pollUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if(updateCount[0] == 2)
                                                        {
                                                            Toast.makeText(context,"You have successfully casted your vote",Toast.LENGTH_LONG).show();
                                                            CVP_Overlay.setVisibility(View.GONE);
                                                            CVP_ProgressBar.setVisibility(View.GONE);
                                                            dialog.dismiss();
                                                        }
                                                        else
                                                        {
                                                            updateCount[0]++;
                                                        }

                                                    }
                                                });


                                        Map<String,Object> userUpdate = new HashMap<>();
                                        userUpdate.put((pollType.equals("Minor")? "votingPoints" : "sunvotingpoints"),
                                                ((Long.parseLong(CVP_AvailableSunStarCount.getText().toString())) - Long.parseLong(CVP_VoteCountField.getText().toString())));
                                        db.collection("User")
                                                .document(userID)
                                                .update(userUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if(updateCount[0] == 2)
                                                        {
                                                            Toast.makeText(context,"You have successfully casted your vote",Toast.LENGTH_LONG).show();
                                                            CVP_Overlay.setVisibility(View.GONE);
                                                            CVP_ProgressBar.setVisibility(View.GONE);
                                                            dialog.dismiss();
                                                        }
                                                        else
                                                        {
                                                            updateCount[0]++;
                                                        }

                                                    }
                                                });

                                        Date dateNow = Calendar.getInstance().getTime();
                                        Map<String,Object> transactHistory = new HashMap<>();
                                        transactHistory.put("user_id",userID);
                                        transactHistory.put("date", Tools.dateToString(dateNow));
                                        transactHistory.put("time", Tools.timeToString(dateNow));
                                        transactHistory.put("reference_number",pollID);
                                        transactHistory.put("timestamp", new com.google.firebase.Timestamp(dateNow));
                                        //Set the currency that is used to -(the amount used)
                                        transactHistory.put((pollType.equals("Minor")? "star" : "sun"),(-Long.parseLong(CVP_VoteCountField.getText().toString())));
                                        //Set the currency that is not used to 0
                                        transactHistory.put((pollType.equals("Minor")? "sun" : "star"),0);
                                        transactHistory.put("transaction_type","vote");
                                        db.collection("transaction_history").add(transactHistory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                if(updateCount[0] == 2)
                                                {
                                                    Toast.makeText(context,"You have successfully casted your vote",Toast.LENGTH_LONG).show();
                                                    CVP_Overlay.setVisibility(View.GONE);
                                                    CVP_ProgressBar.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                }
                                                else
                                                {
                                                    updateCount[0]++;
                                                }
                                            }
                                        });
                                    }

                                }
                            });

                            dialog.show();

                        }
                    });
                }
                else
                {
                    Toast.makeText(context,"Voting has ended",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return artistVotesList.size();
    }

    public class VSArtistVoteListViewHolder extends RecyclerView.ViewHolder{

        ImageView VSAV_Profile;
        TextView VSAV_ArtistsName;
        TextView VSAV_StarVotesCount;
        TextView VSAV_SunVotesCount;
        ProgressBar VSAV_ProfileProgressBar;
        CardView VSAV_Background;
        TextView VSAV_StarIcon;
        TextView VSAV_SunIcon;
        TextView VSAV_RankNumber;
        Button VSAV_CastVoteButton;
        public VSArtistVoteListViewHolder(@NonNull View itemView) {
            super(itemView);
            VSAV_Profile = itemView.findViewById(R.id.VSAV_Profile);
            VSAV_ArtistsName = itemView.findViewById(R.id.VSAV_ArtistsName);
            VSAV_ProfileProgressBar = itemView.findViewById(R.id.VSAV_ProfileProgressBar);
            VSAV_Background = itemView.findViewById(R.id.VSAV_Background);
            VSAV_StarVotesCount = itemView.findViewById(R.id.VSAV_StarVotesCount);
            VSAV_SunVotesCount = itemView.findViewById(R.id.VSAV_SunVotesCount);
            VSAV_StarIcon = itemView.findViewById(R.id.VSAV_StarIcon);
            VSAV_SunIcon = itemView.findViewById(R.id.VSAV_SunIcon);
            VSAV_RankNumber = itemView.findViewById(R.id.VSAV_RankNumber);
            VSAV_CastVoteButton = itemView.findViewById(R.id.VSAV_CastVoteButton);
        }
    }
}
