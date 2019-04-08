package com.ayros.iftis_mobapp.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int day_code;
    public int num_lesson;
    @Embedded
    public Lesson lesson;

    public int getDay_code() {
        return day_code;
    }

    public void setDay_code(int day_code) {
        this.day_code = day_code;
    }

    public int getNum_lesson() {
        return num_lesson;
    }

    public void setNum_lesson(int num_lesson) {
        this.num_lesson = num_lesson;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
