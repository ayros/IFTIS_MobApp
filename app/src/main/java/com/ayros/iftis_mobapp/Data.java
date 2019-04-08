package com.ayros.iftis_mobapp;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.ScheduleDao;
import com.ayros.iftis_mobapp.db.UserDao;
import com.ayros.iftis_mobapp.db.UserPersonalDatabase;
import com.ayros.iftis_mobapp.model.Student;

public class Data {
    private static Data ourInstance;

    private DbTask task;
    private UserPersonalDatabase db;
    private UserDao userDao;
    private ScheduleDao scheduleDao;
    private Student student;

    public static Data getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new Data(context);
        }
        return ourInstance;
    }

    private Data(Context context) {
        db = Room.databaseBuilder(context, UserPersonalDatabase.class, "iftis").build();
        userDao = db.userDao();
        scheduleDao = db.scheduleDao();
    }

    public void getData(DataBaseAction action){
        task = new DbTask(action);
        task.execute((Void)null);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ScheduleDao getScheduleDao() {
        return scheduleDao;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public UserPersonalDatabase getDb() {
        return db;
    }

    private class DbTask extends AsyncTask<Void,Void,Void> {

        private DataBaseAction action;

        public DbTask(DataBaseAction action) {
            this.action = action;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            action.findData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            action.finised();
        }
    }
}
