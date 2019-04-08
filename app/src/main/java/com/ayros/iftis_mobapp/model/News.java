package com.ayros.iftis_mobapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class News {


    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String title;
    private Date time;
    private String description;

    public News(){
        //time = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "News{" +
                "mId=" + mId +
                ", title=" + title +
                ", time=" + time +
                ", description=" + description +
                '}';
    }
}
