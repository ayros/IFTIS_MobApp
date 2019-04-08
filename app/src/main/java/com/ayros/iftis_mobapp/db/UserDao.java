package com.ayros.iftis_mobapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ayros.iftis_mobapp.model.Student;

@Dao
public interface UserDao {

    @Query("SELECT * FROM student")
    public Student getFirst();

    @Insert
    public void insertAll(Student... students);

    @Delete
    public void delete(Student student);
}
