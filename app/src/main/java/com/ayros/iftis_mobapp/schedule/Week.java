package com.ayros.iftis_mobapp.schedule;

import android.content.Context;

import com.ayros.iftis_mobapp.R;

import java.util.ArrayList;

class Week {
    private final static int WEEK_LENGTH = 5;
    private final static int WEEK_COUNT = 2;
    private static Week ourInstance;

    private ArrayList<String> weekDay;
    private ArrayList<String> weekType;

    static Week getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new Week(context);
        }

        return ourInstance;
    }

    private Week(Context context) {
        weekDay = new ArrayList<>(WEEK_LENGTH);
        weekType = new ArrayList<>(WEEK_COUNT);

        weekDay.add(context.getString(R.string.monday));
        weekDay.add(context.getString(R.string.tuesday));
        weekDay.add(context.getString(R.string.wednesday));
        weekDay.add(context.getString(R.string.thursday));
        weekDay.add(context.getString(R.string.friday));

        weekType.add(context.getString(R.string.first_week));
        weekType.add(context.getString(R.string.second_week));
    }

    public String getDay(int day_code){
        return weekDay.get(day_code%WEEK_LENGTH);
    }

    public String getType(int day_code){
        return weekType.get(day_code/WEEK_LENGTH);
    }
}
