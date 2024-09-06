package com.JuanCast.myapplication.listadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
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
import com.JuanCast.myapplication.VotingSpec;
import com.JuanCast.myapplication.models.Poll;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.PollListViewHolder> {

    Context context;
    ArrayList<Poll> pollList;
    FirebaseStorage storage;
    public PollListAdapter(Context context, ArrayList<Poll> pollList)
    {
        this.context = context;
        this.pollList = pollList;

        storage = FirebaseStorage.getInstance();
    }


    @NonNull
    @Override
    public PollListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PollListViewHolder(LayoutInflater.from(context).inflate(R.layout.polllist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PollListViewHolder holder, int position) {
        Poll specPoll = pollList.get(position);

        StorageReference storageRef = storage.getReference();
        storageRef.child("voting_poll_banners").child(specPoll.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        holder.PLI_BannerProgressBar.setVisibility(View.GONE);
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        holder.PLI_BannerProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.PLI_Banner);
            }
        });

        holder.PLI_BannerProgressBar.setVisibility(View.VISIBLE);
        String pollTypeText = specPoll.getPollType() + " Poll";
        if(specPoll.getPollType().equals("Minor"))
        {
            holder.PLI_PollTypeIcon.setBackground(AppCompatResources.getDrawable(context,R.drawable.star));
            holder.PLI_PollType.setText(pollTypeText);
        }
        else if(specPoll.getPollType().equals("Major"))
        {
            holder.PLI_PollTypeIcon.setBackground(AppCompatResources.getDrawable(context,R.drawable.sun_version2));
            holder.PLI_PollType.setText(pollTypeText);
        }

        holder.PLI_Title.setText(specPoll.getTitle());
        String dateRange = Tools.dateToString(specPoll.getDateFrom(),"MMMM d, yyyy") + "-" + Tools.dateToString(specPoll.getDateTo(),"MMMM d, yyyy");
        holder.PLI_DateRange.setText(dateRange);

        String artistCount = specPoll.getArtistIDList().size() + " Artists";
        holder.PLI_ArtistCount.setText(artistCount);

        holder.PLI_Background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVotingSpec(specPoll.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class PollListViewHolder extends RecyclerView.ViewHolder{
        ImageView PLI_Banner;
        TextView PLI_Title;
        TextView PLI_DateRange;
        TextView PLI_ArtistCount;
        ProgressBar PLI_BannerProgressBar;
        ConstraintLayout PLI_Background;
        TextView PLI_PollType;
        TextView PLI_PollTypeIcon;
        public PollListViewHolder(@NonNull View itemView) {
            super(itemView);
            PLI_Banner = itemView.findViewById(R.id.PLI_Banner);
            PLI_Title = itemView.findViewById(R.id.PLI_Title);
            PLI_DateRange = itemView.findViewById(R.id.PLI_DateRange);
            PLI_ArtistCount = itemView.findViewById(R.id.PLI_ArtistCount);
            PLI_BannerProgressBar = itemView.findViewById(R.id.PLI_BannerProgressBar);
            PLI_Background = itemView.findViewById(R.id.PLI_Background);
            PLI_PollType = itemView.findViewById(R.id.PLI_PollType);
            PLI_PollTypeIcon = itemView.findViewById(R.id.PLI_PollTypeIcon);
        }
    }

    public void openVotingSpec(String pollID)
    {
        Intent intent = new Intent(context, VotingSpec.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pollID",pollID);
        context.startActivity(intent);

    }
}
