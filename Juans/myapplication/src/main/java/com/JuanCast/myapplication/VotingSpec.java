package com.JuanCast.myapplication;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.JuanCast.myapplication.models.Artist;
import com.JuanCast.myapplication.models.ServerTime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.JuanCast.myapplication.listadapters.VSArtistVoteListAdapter;
import com.JuanCast.myapplication.models.ArtistVotes;
import com.JuanCast.myapplication.models.Poll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VotingSpec extends AppCompatActivity {

    Button VS_BackButton;
    ImageView VS_BannerImage;
    TextView VS_TitleText;
    TextView VS_DateRangeText;
    TextView AP_PollEndedStatus;
    TextView VS_NoteText;
    TextView VS_UserWelcomeText;
    TextView VS_StarCount;
    TextView VS_SunCount;
    RecyclerView VS_ArtistsRecyclerView;
    String pollID;
    ProgressBar VS_BannerProgressBar;
    ProgressBar VS_ArtistListProgressBar;
    SwipeRefreshLayout VS_RefreshLayout;

    FirebaseFirestore db;
    FirebaseStorage storage;



    ArrayList<ArtistVotes> artistList;

    boolean pollEnded = false;
    public void showWinnerPopup(ArtistVotes artist)
    {
        Dialog dialog = new Dialog(VotingSpec.this,R.style.CastDialog);
        dialog.setContentView(R.layout.vs_winner_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView VSWP_ArtistProfile = dialog.findViewById(R.id.VSWP_ArtistProfile);
        TextView VSWP_ArtistName = dialog.findViewById(R.id.VSWP_ArtistName);
        TextView VSWP_VoteCount = dialog.findViewById(R.id.VSWP_VoteCount);
        Button VSWP_CloseButton = dialog.findViewById(R.id.VSWP_CloseButton);
        ProgressBar VSWP_ProgressBar = dialog.findViewById(R.id.VSWP_ProgressBar);
        VSWP_ArtistName.setText(artist.getArtistName());
        VSWP_VoteCount.setText(String.valueOf(artist.getStarVotes() + artist.getSunVotes()));

        storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                VSWP_ProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                VSWP_ProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(VSWP_ArtistProfile);
            }
        });

        VSWP_CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void setPollInfo()
    {
        artistList = new ArrayList<>();
        db.collection("voting_polls").document(pollID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    new ServerTime().addOnSuccessListener(new ServerTime.ServerTimeSuccessListener<Date>() {
                        @Override
                        public void onSuccess(Date serverTime) {
                            Map<String, Object> data = task.getResult().getData();

                            Poll specPoll = new Poll(task.getResult().getId(),
                                    (String)data.get("poll_title"),
                                    Tools.StringToDate((String)data.get("date_from")),
                                    Tools.StringToDate((String)data.get("date_to")),
                                    Tools.StringToTime((String)data.get("time_end")),
                                    (String) data.get("note"),
                                    (ArrayList<String>)data.get("artists"),
                                    (ArrayList<String>)data.get("tag_list"),
                                    (String)data.get("poll_type"),
                                    (String)data.get("visibility"));

                            String dateRange =
                                    Tools.dateToString(specPoll.getDateFrom(),"MMMM d, yyyy") +
                                            "-" + Tools.dateToString(specPoll.getDateTo(),"MMMM d, yyyy")
                                            + " " + Tools.timeToString(specPoll.getTimeEnd());
                            VS_TitleText.setText(specPoll.getTitle());
                            VS_DateRangeText.setText(dateRange);
                            VS_NoteText.setText(specPoll.getNote());

                            if((Tools.dateTimeEnd(serverTime,Tools.dateToString(specPoll.getDateTo()),Tools.timeToString(specPoll.getTimeEnd()))))
                            {
                                AP_PollEndedStatus.setVisibility(View.VISIBLE);
                                pollEnded = true;
                            }
                            else
                            {
                                AP_PollEndedStatus.setVisibility(View.GONE);
                            }

                            storage.getReference().child("voting_poll_banners").child(pollID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true).addListener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                                    VS_BannerProgressBar.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                                    VS_BannerProgressBar.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            }).into(VS_BannerImage);

                                }
                            });



                            db.collection("artists").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot artistQuerySnapShot) {

                                    if(!artistQuerySnapShot.isEmpty())
                                    {
                                        VS_RefreshLayout.setRefreshing(true);
                                        db.collection("voting_polls").document(pollID).collection("votes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot votingPollQuerySnapshot) {

                                                for(DocumentSnapshot artistDoc : artistQuerySnapShot.getDocuments())
                                                {
                                                    Map<String,Object> data = artistDoc.getData();

                                                    if(specPoll.getArtistIDList().contains(artistDoc.getId()))
                                                    {
                                                        for(DocumentSnapshot votingPollDoc: votingPollQuerySnapshot.getDocuments())
                                                        {
                                                            if(votingPollDoc.getId().equals(artistDoc.getId()))
                                                            {
                                                                artistList.add(new ArtistVotes(artistDoc.getId(),
                                                                        (String)data.get("artist_name"),
                                                                        (ArrayList<String>)data.get("tags"),
                                                                        (long)votingPollDoc.getData().get("sun_votes"),
                                                                        (long)votingPollDoc.getData().get("star_votes")));
                                                            }
                                                        }


                                                    }
                                                }

                                                Collections.sort(artistList,Collections.reverseOrder());

                                                if(pollEnded)
                                                {
                                                    showWinnerPopup(artistList.get(0));
                                                }
                                                VS_RefreshLayout.setRefreshing(false);
                                                VS_ArtistsRecyclerView.setAdapter(
                                                        new VSArtistVoteListAdapter(
                                                                VotingSpec.this,
                                                                artistList,
                                                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                                pollID,specPoll.getPollType(),
                                                                (Tools.dateTimeEnd(serverTime,Tools.dateToString(specPoll.getDateTo()),Tools.timeToString(specPoll.getTimeEnd())))));
                                                VS_ArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                VS_ArtistListProgressBar.setVisibility(View.GONE);
                                            }
                                        });



                                    }



                                }
                            });
                        }
                    });

                }
            }
        });
    }


    public void setUserInfo()
    {
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> data = documentSnapshot.getData();

                String welcomeText = "Welcome, " + ((String)data.get("fullName"));
                VS_UserWelcomeText.setText(welcomeText);
                VS_StarCount.setText(String.valueOf((Long) data.get("votingPoints")));
                VS_SunCount.setText(String.valueOf((Long) data.get("sunvotingpoints")));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voting_spec);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        VS_BackButton = findViewById(R.id.VS_BackButton);
        VS_BannerImage = findViewById(R.id.VS_BannerImage);
        VS_TitleText = findViewById(R.id.VS_TitleText);
        VS_DateRangeText = findViewById(R.id.VS_DateRangeText);
        AP_PollEndedStatus = findViewById(R.id.AP_PollEndedStatus);
        VS_NoteText = findViewById(R.id.VS_NoteText);
        VS_ArtistsRecyclerView = findViewById(R.id.VS_ArtistsRecyclerView);
        VS_BannerProgressBar = findViewById(R.id.VS_BannerProgressBar);
        VS_ArtistListProgressBar = findViewById(R.id.VS_ArtistListProgressBar);
        VS_RefreshLayout = findViewById(R.id.VS_RefreshLayout);
        VS_UserWelcomeText = findViewById(R.id.VS_UserWelcomeText);
        VS_StarCount = findViewById(R.id.VS_StarCount);
        VS_SunCount = findViewById(R.id.VS_SunCount);

        try{
            pollID = getIntent().getExtras().getString("pollID");
            setPollInfo();
            setUserInfo();
        }catch (Exception e)
        {

        }

        VS_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        VS_RefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setPollInfo();
            }
        });


    }
}