package com.ayros.iftis_mobapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ayros.iftis_mobapp.model.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    public List<News> getNews();

    @Insert
    public void insertAll(News... news);

}
