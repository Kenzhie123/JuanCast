package com.JuanCast.myapplication.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class Poll {

    private String id;
    private String title;
    private Date dateFrom;
    private Date dateTo;
    private Date timeEnd;
    private String note;
    private ArrayList<String> artistIDList;
    private ArrayList<String> tagList;
    private ArrayList<Vote> votes;
    private String pollType;
    private String visibility;

    public Poll(String id,
                String title,
                Date dateFrom,
                Date dateTo,
                Date timeEnd,
                String note,
                ArrayList<String> artistIDList,
                ArrayList<String> tagList,
                String pollType,
                String visibility) {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeEnd = timeEnd;
        this.note = note;
        this.artistIDList = artistIDList;
        this.tagList = tagList;
        this.pollType = pollType;
        this.visibility = visibility;
    }
    public Poll(String id, String title, Date dateFrom, Date dateTo, Date timeEnd, String note, ArrayList<String> artistIDList, ArrayList<String> tagList, String pollType, String visibility, ArrayList<Vote> votes) {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.note = note;
        this.artistIDList = artistIDList;
        this.tagList = tagList;
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public String getNote() {
        return note;
    }

    public ArrayList<String> getArtistIDList() {
        return artistIDList;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public String getPollType() {
        return pollType;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setArtistIDList(ArrayList<String> artistIDList) {
        this.artistIDList = artistIDList;
    }

    public void setTagList(ArrayList<String> tagList) {
        this.tagList = tagList;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return getId().equals(((Poll)obj).getId());
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", note='" + note + '\'' +
                ", artistIDList=" + artistIDList +
                ", tagList=" + tagList +
                '}';
    }
}
