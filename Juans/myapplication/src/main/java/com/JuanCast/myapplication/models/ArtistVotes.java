package com.JuanCast.myapplication.models;

import java.util.ArrayList;

public class ArtistVotes extends Artist implements Comparable<ArtistVotes>{
    private long sunVotes;
    private long starVotes;

    public ArtistVotes(String artistID, String artistName, ArrayList<String> tags, long sunVotes, long starVotes) {
        super(artistID, artistName, tags);
        this.sunVotes = sunVotes;
        this.starVotes = starVotes;
    }

    public long getSunVotes() {
        return sunVotes;
    }

    public long getStarVotes() {
        return starVotes;
    }

    public void setSunVotes(long sunVotes) {
        this.sunVotes = sunVotes;
    }

    public void setStarVotes(long starVotes) {
        this.starVotes = starVotes;
    }


    @Override
    public int compareTo(ArtistVotes artistVotes) {
        return ((Long)(sunVotes+starVotes)).compareTo((Long)(artistVotes.sunVotes + artistVotes.starVotes));
    }
}

