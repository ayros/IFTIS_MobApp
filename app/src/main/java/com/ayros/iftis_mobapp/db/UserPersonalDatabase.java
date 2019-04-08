package com.ayros.iftis_mobapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.ayros.iftis_mobapp.model.News;
import com.ayros.iftis_mobapp.model.Schedule;
import com.ayros.iftis_mobapp.model.Student;

@Database(entities = {Student.class, Schedule.class, News.class},version = 1)
@TypeConverters({Converters.class})
public abstract class UserPersonalDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract ScheduleDao scheduleDao();

    public abstract NewsDao newsDao();
}
