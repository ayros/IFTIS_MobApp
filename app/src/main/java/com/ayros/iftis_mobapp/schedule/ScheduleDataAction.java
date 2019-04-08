package com.ayros.iftis_mobapp.schedule;

import android.arch.persistence.room.Database;
import android.content.Context;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.ScheduleDao;
import com.ayros.iftis_mobapp.db.UserPersonalDatabase;
import com.ayros.iftis_mobapp.model.Schedule;

public class ScheduleDataAction implements DataBaseAction {

    private Schedule[] schedules;
    private Data data;
    private UserPersonalDatabase db;
    private ScheduleDao dao;
    public ScheduleDataAction(Schedule[] schedules, Context context){
        this.schedules = schedules;
        data = Data.getInstance(context);
        db = data.getDb();
        dao = db.scheduleDao();
    }
    @Override
    public void findData() {
        dao.insert(schedules);
    }

    @Override
    public void finised() {

    }
}
