package com.ayros.iftis_mobapp.home;

import android.content.Context;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.DatabaseCallback;
import com.ayros.iftis_mobapp.db.NewsDao;
import com.ayros.iftis_mobapp.model.News;

import java.util.Arrays;
import java.util.List;

public class NewsDataBaseAction implements DataBaseAction {

    protected NewsDao dao;
    private News[] news;
    private DatabaseCallback callback;

    public NewsDataBaseAction(Context context, DatabaseCallback callback){
        dao = Data.getInstance(context).getDb().newsDao();
        this.callback = callback;
    }

    @Override
    public void findData() {
        List<News> list = dao.getNews();
        news = new News[list.size()];
        news = list.toArray(news);
    }

    @Override
    public void finised() {
        //callback.finished();
    }

    public News[] getNews() {
        return news;
    }

    public void setNews(News[] news) {
        this.news = news;
    }
}
