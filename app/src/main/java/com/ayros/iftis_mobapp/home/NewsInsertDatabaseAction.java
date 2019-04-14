package com.ayros.iftis_mobapp.home;

import android.content.Context;

import com.ayros.iftis_mobapp.db.DatabaseCallback;
import com.ayros.iftis_mobapp.model.News;

import java.util.List;

public class NewsInsertDatabaseAction extends NewsDataBaseAction {


    public NewsInsertDatabaseAction(Context context, DatabaseCallback callback,List<News> news) {
        super(context, callback);
        this.news = news;
    }

    public void findData(){
        dao.insertAll(news);
    }
}
