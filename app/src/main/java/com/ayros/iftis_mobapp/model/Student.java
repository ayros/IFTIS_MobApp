package com.ayros.iftis_mobapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@Entity(primaryKeys = {"login"})
@JsonIgnoreProperties({"schedule_version"})
public class Student implements Serializable {

    @NonNull
    private String login;
    private String password;
    private int schedule_version;

    public int getSchedule_version() {
        return schedule_version;
    }

    public void setSchedule_version(int schedule_version) {
        this.schedule_version = schedule_version;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
