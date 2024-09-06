package com.JuanCast.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.JuanCast.myapplication.Receiver.NetworkChangeReceiver;
import com.JuanCast.myapplication.Receiver.PostChangeReceiver;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.JuanCast.myapplication.helper.PollNotification;
import com.JuanCast.myapplication.listadapters.HomePollListAdapter;
import com.JuanCast.myapplication.models.Artist;
import com.JuanCast.myapplication.models.Poll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Homepage extends AppCompatActivity {

    private ImageView notificationIcon;
    private boolean isFirstTime = true;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private DocumentReference userNotificationDoc;

    private TextView welcomeTextView;
    private TextView votingPointsTextView;
    private TextView SunvotingPointsTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    //navvar
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView Store;


    ImageView H_FPProfile;
    ImageView H_SPProfile;
    ImageView H_TPProfile;
    TextView H_FPArtistName;
    TextView H_SPArtistName;
    TextView H_TPArtistName;
    TextView H_FPVoteCount;
    TextView H_SPVoteCount;
    TextView H_TPVoteCount;
    TextView H_LivePollTitle;
    ConstraintLayout H_LivePollBackground;

    RecyclerView H_PollListRecyclerView;
    ArrayList<Poll> pollList;

    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdapter postAdapter;



    private RelativeLayout noInternetLayout;
    private NetworkChangeReceiver networkChangeReceiver;
    // Getter para sa noInternetLayout
    public RelativeLayout getNoInternetLayout() {
        return noInternetLayout;
    }



    public void openVotingSpec(String pollID)
    {
        Intent intent = new Intent(getApplicationContext(), VotingSpec.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pollID",pollID);
        startActivity(intent);

    }

    public boolean pollNotificationServiceRunning()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            if(PollNotification.class.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
    public void startNotification()
    {
        Intent notificationIntent = new Intent(Homepage.this, PollNotification.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!pollNotificationServiceRunning())
            {
                try{
                    startForegroundService(notificationIntent);

                }catch (Exception e)
                {
                    Log.d("DATATAG","Poll Listener Already Started");
                }


            }
            else
            {
            }

        }
    }

    public void initLivePoll()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    2);
        }
        else
        {

        }
        firebaseFirestore.collection("voting_polls").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    dbRef.child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Long serverTime = snapshot.getValue(Long.class);
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

<<<<<<< HEAD
                            ArrayList<Poll> tempRecentPolls = new ArrayList<>();
                            final Poll[] tempPoll = {null};
                            for(DocumentSnapshot document : documents)
                            {
                                Map<String,Object> data = document.getData();

                                if(!Tools.dateTimeEnd(new Date(serverTime),(String)data.get("date_to"),(String)data.get("time_end")) && !((String)data.get("visibility")).equals("hidden"))
                                {
                                    Poll poll = new Poll(
                                            document.getId(),
                                            (String) document.getData().get("poll_title"),
                                            Tools.StringToDate((String) document.getData().get("date_from")),
                                            Tools.StringToDate((String) document.getData().get("date_to")),
                                            Tools.StringToTime((String)document.getData().get("time_end")),
                                            (String) document.getData().get("note"),
                                            (ArrayList<String>) document.getData().get("artists"),
                                            (ArrayList<String>) document.getData().get("tag_list"),
                                            (String) document.getData().get("poll_type"),
                                            (String)document.getData().get("visibility"));


                                    if(tempPoll[0] == null)
                                    {
                                        tempPoll[0] = poll;

=======
                    ArrayList<Poll> tempRecentPolls = new ArrayList<>();
                    Poll tempPoll = null;
                    for(DocumentSnapshot document : documents)
                    {
                        Map<String,Object> data = document.getData();

                        if(!Tools.dateTimeEnd((String)data.get("date_to"),(String)data.get("time_end")) && !((String)data.get("visibility")).equals("hidden"))
                        {
                            Poll poll = new Poll(
                                    document.getId(),
                                    (String) document.getData().get("poll_title"),
                                    Tools.StringToDate((String) document.getData().get("date_from")),
                                    Tools.StringToDate((String) document.getData().get("date_to")),
                                    Tools.StringToTime((String)document.getData().get("time_end")),
                                    (String) document.getData().get("note"),
                                    (ArrayList<String>) document.getData().get("artists"),
                                    (ArrayList<String>) document.getData().get("tag_list"),
                                    (String) document.getData().get("poll_type"),
                                    (String)document.getData().get("visibility"));

                            if(tempPoll == null)
                            {
                                tempPoll = poll;
                            }
                            else
                            {
                                int compareValue = tempPoll.getDateTo().compareTo(poll.getDateTo());
                                if(compareValue < 0)
                                {
                                    tempRecentPolls.clear();
                                    tempPoll = poll;
                                }
                                else if(compareValue == 0)
                                {
                                    tempRecentPolls.add(tempPoll);
                                    tempRecentPolls.add(poll);
                                }
                            }

                        }

                    }

                    if(tempRecentPolls.isEmpty() && tempPoll != null)
                    {
                        tempRecentPolls.add(tempPoll);
                    }

                    if(!tempRecentPolls.isEmpty())
                    {
                        Random random = new Random();
                        int randomNumber = random.nextInt(tempRecentPolls.size());
                        Poll currentPoll = tempRecentPolls.get(randomNumber);
                        H_LivePollBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openVotingSpec(currentPoll.getId());
                            }
                        });
                        H_LivePollTitle.setText(currentPoll.getTitle());
                        String voteType = (currentPoll.getPollType().equals("Major")?"sun_votes":"star_votes");

                        ArrayList<Artist> artistList = new ArrayList<Artist>();
                        firebaseFirestore.collection("artists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                try{
                                    if(task.isSuccessful())
                                    {

                                        for(DocumentSnapshot document : task.getResult())
                                        {
                                            Map<String,Object> data = document.getData();
                                            artistList.add(new Artist(
                                                    document.getId(),
                                                    (String)data.get("artist_name"),
                                                    (ArrayList<String>)data.get("tags")));
                                        }

                                        firebaseFirestore.collection("voting_polls")
                                                .document(currentPoll.getId())
                                                .collection("votes")
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        if(error == null)
                                                        {
                                                            ArrayList<Map<String,Object>> idVotes = new ArrayList<>();

                                                            for(DocumentSnapshot document : value.getDocuments())
                                                            {

                                                                Map<String,Object> data  = document.getData();
                                                                Map<String,Object> tempMap = new HashMap<>();
                                                                tempMap.put("votes",data.get(voteType));
                                                                tempMap.put("artist_id",document.getId());
                                                                idVotes.add(tempMap);
                                                            }

                                                            Collections.sort(idVotes, new Comparator<Map<String, Object>>() {
                                                                @Override
                                                                public int compare(Map<String, Object> stringObjectMap, Map<String, Object> t1) {
                                                                    return ((Long)t1.get("votes")).compareTo((Long)stringObjectMap.get("votes"));
                                                                }
                                                            });

                                                            for(Artist artist : artistList)
                                                            {
                                                                if(idVotes.size() > 0)
                                                                {
                                                                    if(artist.getArtistID().equals(idVotes.get(0).get("artist_id")))
                                                                    {
                                                                        H_FPArtistName.setText(artist.getArtistName());
                                                                        H_FPVoteCount.setText(String.valueOf(idVotes.get(0).get("votes")));

                                                                        storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                Glide.with(getApplicationContext()).load(uri).into(H_FPProfile);
                                                                            }
                                                                        });


                                                                    }
                                                                }
                                                                if(idVotes.size() > 1)
                                                                {
                                                                    if(artist.getArtistID().equals(idVotes.get(1).get("artist_id")))
                                                                    {
                                                                        H_SPArtistName.setText(artist.getArtistName());
                                                                        H_SPVoteCount.setText(String.valueOf(idVotes.get(1).get("votes")));

                                                                        storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                Glide.with(getApplicationContext()).load(uri).into(H_SPProfile);
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                                if(idVotes.size() > 2)
                                                                {
                                                                    if(artist.getArtistID().equals(idVotes.get(2).get("artist_id")))
                                                                    {
                                                                        H_TPArtistName.setText(artist.getArtistName());
                                                                        H_TPVoteCount.setText(String.valueOf(idVotes.get(2).get("votes")));

                                                                        storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                Glide.with(getApplicationContext()).load(uri).into(H_TPProfile);
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                            }




                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                                            Log.d("DATATAG",error.getMessage().toString());
                                                        }
                                                    }
                                                });
>>>>>>> parent of e87830a (Update)
                                    }
                                    else
                                    {
                                        int compareValue = tempPoll[0].getDateTo().compareTo(poll.getDateTo());
                                        if(compareValue < 0)
                                        {
                                            tempRecentPolls.clear();
                                            tempPoll[0] = poll;
                                        }
                                        else if(compareValue == 0)
                                        {
                                            tempRecentPolls.add(tempPoll[0]);
                                            tempRecentPolls.add(poll);
                                        }
                                    }
                                }

                            }

                            Log.d("TESTTAG", tempPoll[0].getTitle());
                            if(tempRecentPolls.isEmpty() && tempPoll[0] != null)
                            {
                                tempRecentPolls.add(tempPoll[0]);

                            }

                            if(!tempRecentPolls.isEmpty())
                            {
                                Random random = new Random();
                                int randomNumber = random.nextInt(tempRecentPolls.size());
                                Poll currentPoll = tempRecentPolls.get(randomNumber);
                                H_LivePollBackground.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openVotingSpec(currentPoll.getId());
                                    }
                                });
                                H_LivePollTitle.setText(currentPoll.getTitle());
                                String voteType = (currentPoll.getPollType().equals("Major")?"sun_votes":"star_votes");

                                ArrayList<Artist> artistList = new ArrayList<Artist>();
                                firebaseFirestore.collection("artists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        try{
                                            if(task.isSuccessful())
                                            {

                                                for(DocumentSnapshot document : task.getResult())
                                                {
                                                    Map<String,Object> data = document.getData();
                                                    artistList.add(new Artist(
                                                            document.getId(),
                                                            (String)data.get("artist_name"),
                                                            (ArrayList<String>)data.get("tags")));
                                                }

                                                firebaseFirestore.collection("voting_polls")
                                                        .document(currentPoll.getId())
                                                        .collection("votes")
                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                if(error == null)
                                                                {
                                                                    ArrayList<Map<String,Object>> idVotes = new ArrayList<>();

                                                                    for(DocumentSnapshot document : value.getDocuments())
                                                                    {

                                                                        Map<String,Object> data  = document.getData();
                                                                        Map<String,Object> tempMap = new HashMap<>();
                                                                        tempMap.put("votes",data.get(voteType));
                                                                        tempMap.put("artist_id",document.getId());
                                                                        idVotes.add(tempMap);
                                                                    }

                                                                    Collections.sort(idVotes, new Comparator<Map<String, Object>>() {
                                                                        @Override
                                                                        public int compare(Map<String, Object> stringObjectMap, Map<String, Object> t1) {
                                                                            return ((Long)t1.get("votes")).compareTo((Long)stringObjectMap.get("votes"));
                                                                        }
                                                                    });

                                                                    for(Artist artist : artistList)
                                                                    {
                                                                        if(idVotes.size() > 0)
                                                                        {
                                                                            if(artist.getArtistID().equals(idVotes.get(0).get("artist_id")))
                                                                            {
                                                                                H_FPArtistName.setText(artist.getArtistName());
                                                                                H_FPVoteCount.setText(String.valueOf(idVotes.get(0).get("votes")));

                                                                                storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        Glide.with(getApplicationContext()).load(uri).into(H_FPProfile);
                                                                                    }
                                                                                });


                                                                            }
                                                                        }
                                                                        if(idVotes.size() > 1)
                                                                        {
                                                                            if(artist.getArtistID().equals(idVotes.get(1).get("artist_id")))
                                                                            {
                                                                                H_SPArtistName.setText(artist.getArtistName());
                                                                                H_SPVoteCount.setText(String.valueOf(idVotes.get(1).get("votes")));

                                                                                storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        Glide.with(getApplicationContext()).load(uri).into(H_SPProfile);
                                                                                    }
                                                                                });

                                                                            }
                                                                        }
                                                                        if(idVotes.size() > 2)
                                                                        {
                                                                            if(artist.getArtistID().equals(idVotes.get(2).get("artist_id")))
                                                                            {
                                                                                H_TPArtistName.setText(artist.getArtistName());
                                                                                H_TPVoteCount.setText(String.valueOf(idVotes.get(2).get("votes")));

                                                                                storage.getReference().child("artists").child(artist.getArtistID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        Glide.with(getApplicationContext()).load(uri).into(H_TPProfile);
                                                                                    }
                                                                                });
                                                                            }
                                                                        }

                                                                    }




                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                                                    Log.d("DATATAG",error.getMessage().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                            else
                                            {
                                                Log.d("DATATAG",task.getException().getMessage().toString());
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d("DATATAG",e.getMessage().toString());
                                        }

                                    }
                                });
                            }
                            else

                            {
                                Log.d("HOMEPAGETAG","INVISIBLE");
                                H_LivePollBackground.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("TOOLSTAG", error.getMessage());
                        }
                    });



                }
            }
        });
    }

    public void initPollList()
    {
        pollList = new ArrayList<>();
        firebaseFirestore.collection("voting_polls")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot document : task.getResult().getDocuments())
                            {

                                Poll poll = new Poll(
                                        document.getId(),
                                        (String) document.getData().get("poll_title"),
                                        Tools.StringToDate((String) document.getData().get("date_from")),
                                        Tools.StringToDate((String) document.getData().get("date_to")),
                                        Tools.StringToTime((String)document.getData().get("time_end")),
                                        (String) document.getData().get("note"),
                                        (ArrayList<String>) document.getData().get("artists"),
                                        (ArrayList<String>) document.getData().get("tag_list"),
                                        (String) document.getData().get("poll_type"),
                                        (String)document.getData().get("visibility"));

                                if(!poll.getVisibility().equals("hidden"))
                                {
                                    pollList.add(poll);
                                }

                            }
                            H_PollListRecyclerView.setAdapter(new HomePollListAdapter(getApplicationContext(),pollList));
                            H_PollListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

                        }
                    }
                });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize views
        welcomeTextView = findViewById(R.id.welcomeTextView);
        SunvotingPointsTextView = findViewById(R.id.SunvotingPointsTextView);
        votingPointsTextView = findViewById(R.id.votingPointsTextView);
        H_FPProfile= findViewById(R.id.H_FPProfile);
        H_SPProfile= findViewById(R.id.H_SPProfile);
        H_TPProfile= findViewById(R.id.H_TPProfile);
        H_FPArtistName= findViewById(R.id.H_FPArtistName);
        H_SPArtistName= findViewById(R.id.H_SPArtistName);
        H_TPArtistName= findViewById(R.id.H_TPArtistName);
        H_FPVoteCount= findViewById(R.id.H_FPVoteCount);
        H_SPVoteCount= findViewById(R.id.H_SPVoteCount);
        H_TPVoteCount= findViewById(R.id.H_TPVoteCount);
        H_LivePollTitle= findViewById(R.id.H_LivePollTitle);
        H_PollListRecyclerView= findViewById(R.id.H_PollListRecyclerView);
        H_LivePollBackground= findViewById(R.id.H_LivePollBackground);



        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.HomepostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList); // Initialize PostAdapter without userProfileImageUrl
        recyclerView.setAdapter(postAdapter);

        loadPosts();



        initLivePoll();
        startNotification();
        initPollList();

        // Get user details from Firestore
        fetchUserDetails();

        // Set up image slider
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();




        firebaseFirestore.collection("SliderImages")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming each document has a field "imageUrl"
                            String imageUrl = document.getString("url");

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                            } else {
                                Log.e("ImageSlider", "Invalid imageUrl: " + imageUrl);
                            }
                        }

                        // Check if the list is empty
                        if (!imageList.isEmpty()) {
                            imageSlider.setImageList(imageList, ScaleTypes.FIT);
                        } else {
                            Toast.makeText(this, "No images found for the slider.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.e("ImageSlider", "Error fetching documents: ", task.getException());
                        Toast.makeText(this, "Error fetching images.", Toast.LENGTH_SHORT).show();
                    }
                });




        //navvar
        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        Store = findViewById(R.id.Store);
        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });







        notificationIcon = findViewById(R.id.notification_icon);

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userNotificationDoc = firebaseFirestore.collection("user_notifications")
                    .document(currentUser.getUid());
        }

        checkNotificationState(); // Initial state check
        setupFirestoreListener(); // Listen for new announcements

        notificationIcon.setOnClickListener(v -> {
            showNotificationDot(false);
            markNotificationAsSeen();

            Intent intent = new Intent(Homepage.this, NotifButton.class);
            startActivity(intent);

            overridePendingTransition(0, 0); // Walang animation
        });

        noInternetLayout = findViewById(R.id.noInternetLayout);
        networkChangeReceiver = new NetworkChangeReceiver();

        // Initial check for internet connectivity
        if (!isConnected()) {
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            noInternetLayout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        checkNotificationState(); // Check notification state every time the activity is resumed

        // Register receiver to listen for network changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister receiver to avoid memory leaks
        unregisterReceiver(networkChangeReceiver);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void loadPosts() {
        firebaseFirestore.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)  // Limit the results to the 5 latest posts
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    } else {
                    }
                    // Hide the refreshing indicator when done
                });
    }

    private void checkNotificationState() {
        if (userNotificationDoc != null) {
            userNotificationDoc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        boolean hasSeenNotification = document.getBoolean("hasSeenNotification");
                        showNotificationDot(!hasSeenNotification);
                    } else {
                        // Document does not exist, consider it as not seen
                        showNotificationDot(true);
                    }
                } else {
                    Log.w("Firestore Error", "Failed to get notification state", task.getException());
                }
            });
        }
    }

    private void setupFirestoreListener() {
        CollectionReference announcementsRef = firebaseFirestore.collection("announcements");

        announcementsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("Firestore Error", error);
                return;
            }

            if (value != null && !value.isEmpty()) {
                // Check if there are new announcements
                if (isFirstTime) {
                    isFirstTime = false;
                } else {
                    // Show the notification for all users
                    showNotificationDot(true);

                    // Update notification state for the logged-in user
                    if (userNotificationDoc != null) {
                        userNotificationDoc.set(new UserNotificationState(false))
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification state updated"))
                                .addOnFailureListener(e -> Log.w("Firestore Error", "Error updating notification state", e));
                    }
                }
            }
        });
    }


    private void showNotificationDot(boolean show) {
        notificationIcon.setImageResource(show ? R.drawable.notificationdot : R.drawable.notification);
    }

    private void markNotificationAsSeen() {
        if (userNotificationDoc != null) {
            userNotificationDoc.set(new UserNotificationState(true))
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notification state updated"))
                    .addOnFailureListener(e -> Log.w("Firestore Error", "Error updating notification state", e));
        }
    }

    private static class UserNotificationState {
        private boolean hasSeenNotification;

        public UserNotificationState(boolean hasSeenNotification) {
            this.hasSeenNotification = hasSeenNotification;
        }

        public boolean getHasSeenNotification() {
            return hasSeenNotification;
        }

        public void setHasSeenNotification(boolean hasSeenNotification) {
            this.hasSeenNotification = hasSeenNotification;
        }
    }


    private void fetchUserDetails() {
        // Fetch username and voting points from Firestore based on current user's UID
        String userId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            Double votingPoints = documentSnapshot.getDouble("votingPoints");
                            Double sunvotingpoints = documentSnapshot.getDouble("sunvotingpoints");

                            // Update UI with fetched details
                            welcomeTextView.setText("Welcome, " + username);
                            // Format voting points to remove .0 if it's an integer
                            String formattedVotingPoints = String.valueOf(votingPoints);
                            if (formattedVotingPoints.endsWith(".0")) {
                                formattedVotingPoints = formattedVotingPoints.substring(0, formattedVotingPoints.length() - 2);
                            }
                            votingPointsTextView.setText("" + formattedVotingPoints);

                            String formattedSunvotingPoints = String.valueOf(sunvotingpoints);
                            if (formattedSunvotingPoints.endsWith(".0")) {
                                formattedSunvotingPoints = formattedSunvotingPoints.substring(0, formattedSunvotingPoints.length() - 2);
                            }
                            SunvotingPointsTextView.setText("" + formattedSunvotingPoints);
                        } else {
                            Toast.makeText(Homepage.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Homepage.this, "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




}
//