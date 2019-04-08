package com.ayros.iftis_mobapp.model;

import android.arch.persistence.room.Embedded;

import java.io.Serializable;

public class Lesson implements Serializable {

    private String start;
    private String end;
    private String name;
    private String instructor;
    private String type;
    private String holl;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getHoll() {
        return holl;
    }

    public void setHoll(String room) {
        this.holl = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
