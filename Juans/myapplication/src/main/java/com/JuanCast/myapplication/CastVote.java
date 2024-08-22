package com.JuanCast.myapplication;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class CastVote extends AppCompatActivity {

    Button CV_BackButton;
    ImageView CV_ArtistProfile;
    TextView CV_ArtistName;
    TextView CV_AvailableSunsCount;
    TextView CV_AvailableStarsCount;
    RadioButton CV_SunsRadioButton;
    RadioButton CV_StarsRadioButton;
    EditText CV_VoteAmountField;
    Button CV_CastVoteButton;
    ProgressBar CV_ProfileProgressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    long sunCount = 0;
    long starCount = 0;

    public void setInfo(String artistID)
    {
        String id = mAuth.getCurrentUser().getUid();
        db.collection("User").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> data = documentSnapshot.getData();

                sunCount = ((Long)data.get("sunvotingpoints"));
                starCount = ((Long)data.get("votingPoints"));
                CV_AvailableStarsCount.setText(String.valueOf(starCount));
                CV_AvailableSunsCount.setText(String.valueOf(sunCount));

            }
        });


        db.collection("artists").document(artistID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> data  = documentSnapshot.getData();

                storage.getReference().child("artists").child(artistID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        CV_ProfileProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        CV_ProfileProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(CV_ArtistProfile);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                CV_ArtistName.setText((String)data.get("artist_name"));
            }
        });

    }

    public void setRadioButtonSelected(RadioButton radioButton)
    {
        CV_SunsRadioButton.setChecked(false);
        CV_StarsRadioButton.setChecked(false);

        radioButton.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cast_vote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CV_BackButton = findViewById(R.id.CV_BackButton);
        CV_ArtistProfile = findViewById(R.id.CV_ArtistProfile);
        CV_ArtistName = findViewById(R.id.CV_ArtistName);
        CV_AvailableSunsCount = findViewById(R.id.CV_AvailableSunsCount);
        CV_AvailableStarsCount = findViewById(R.id.CV_AvailableStarsCount);
        CV_SunsRadioButton = findViewById(R.id.CV_SunsRadioButton);
        CV_StarsRadioButton = findViewById(R.id.CV_StarsRadioButton);
        CV_VoteAmountField = findViewById(R.id.CV_VoteAmountField);
        CV_CastVoteButton = findViewById(R.id.CV_CastVoteButton);
        CV_ProfileProgressBar = findViewById(R.id.CV_ProfileProgressBar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        try{
            setInfo(getIntent().getExtras().getString("artistID"));
        }catch (Exception e)
        {

        }



        CV_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CV_StarsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioButtonSelected(CV_StarsRadioButton);
            }
        });
        CV_SunsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioButtonSelected(CV_SunsRadioButton);
            }
        });

        CV_CastVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}