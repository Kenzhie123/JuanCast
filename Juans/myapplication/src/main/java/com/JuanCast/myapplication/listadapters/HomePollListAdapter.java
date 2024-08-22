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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.JuanCast.myapplication.R;
import com.JuanCast.myapplication.VotingSpec;
import com.JuanCast.myapplication.models.Poll;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class HomePollListAdapter extends RecyclerView.Adapter<HomePollListAdapter.HomePollListViewHolder>{

    Context context;
    ArrayList<Poll> pollList;
    FirebaseStorage storage;
    public HomePollListAdapter(Context context, ArrayList<Poll> pollList)
    {
        this.context = context;
        this.pollList = pollList;
        storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public HomePollListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePollListViewHolder(LayoutInflater.from(context).inflate(R.layout.h_poll_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePollListViewHolder holder, int position) {
        Poll currentPoll = pollList.get(position);
        storage.getReference().child("voting_poll_banners").child(currentPoll.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.HPLI_BannerProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.HPLI_BannerProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.HPLI_PollBanner);
            }
        });

        holder.HPLI_PollTitle.setText(currentPoll.getTitle());
        holder.HPLI_Background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVotingSpec(currentPoll.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class HomePollListViewHolder extends RecyclerView.ViewHolder{

        ImageView HPLI_PollBanner;
        ConstraintLayout HPLI_Background;
        TextView HPLI_PollTitle;
        ProgressBar HPLI_BannerProgressBar;
        public HomePollListViewHolder(@NonNull View itemView) {
            super(itemView);
            HPLI_PollBanner = itemView.findViewById(R.id.HPLI_PollBanner);
            HPLI_Background = itemView.findViewById(R.id.HPLI_Background);
            HPLI_PollTitle = itemView.findViewById(R.id.HPLI_PollTitle);
            HPLI_BannerProgressBar = itemView.findViewById(R.id.HPLI_BannerProgressBar);
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
