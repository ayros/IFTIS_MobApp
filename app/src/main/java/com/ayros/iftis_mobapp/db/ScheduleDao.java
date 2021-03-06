package com.ayros.iftis_mobapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ayros.iftis_mobapp.model.Lesson;
import com.ayros.iftis_mobapp.model.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT start,`end`, name, instructor,holl,type,num_lesson FROM schedule WHERE :day_code = day_code ORDER BY num_lesson")
    public List<Lesson> getDay(int day_code);

    @Query("DELETE FROM schedule")
    public void deleteAll();

    @Insert
    public void insert(Schedule... schedule);

    @Delete
    public void delete(Schedule... schedule);
}
