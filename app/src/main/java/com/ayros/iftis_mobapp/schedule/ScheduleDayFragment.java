package com.ayros.iftis_mobapp.schedule;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.ScheduleDao;
import com.ayros.iftis_mobapp.model.Lesson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleDayFragment extends Fragment {

    private static final String ARG_DAY_CODE = "day_code";
    private static final int LESSON_NUMBER = 5;

    private ArrayList<Lesson> lessons;
    private ArrayList<LessonView> lessonViews;
    private TextView weekDay;
    private TextView weekType;

    private Data data;

    private int day_code;

    public ScheduleDayFragment() {
        // Required empty public constructor
    }

    public static ScheduleDayFragment newInstance(int day_code) {
        ScheduleDayFragment fragment = new ScheduleDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_CODE,day_code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day_code = getArguments().getInt(ARG_DAY_CODE);
        }
        lessons = new ArrayList<>(LESSON_NUMBER);
        lessonViews = new ArrayList<>(LESSON_NUMBER);
        data = Data.getInstance(ScheduleDayFragment.this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_day,container,false);
        lessonViews.add((LessonView)v.findViewById(R.id.lesson1));
        lessonViews.add((LessonView)v.findViewById(R.id.lesson2));
        lessonViews.add((LessonView)v.findViewById(R.id.lesson3));
        lessonViews.add((LessonView)v.findViewById(R.id.lesson4));
        lessonViews.add((LessonView)v.findViewById(R.id.lesson5));

        weekDay = v.findViewById(R.id.week_day);
        weekType = v.findViewById(R.id.week_type);


        DataBaseAction action = new ScheduleDayAction();
        data.getData(action);

        initDay();

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void initLessons(){
        for (int i = 0; i < LESSON_NUMBER; i++){
            lessonViews.get(i).setLesson(lessons.get(i));
        }
    }

    private void initDay(){
        Week week = Week.getInstance(getContext());
        weekDay.setText(week.getDay(day_code));
        weekType.setText(week.getType(day_code));
    }

    private class ScheduleDayAction implements DataBaseAction{

        private ScheduleDao scheduleDao;

        ScheduleDayAction(){
            scheduleDao = data.getDb().scheduleDao();
        }

        @Override
        public void findData() {
            lessons.addAll(scheduleDao.getDay(day_code));
        }

        @Override
        public void finished() {
            if(lessons.isEmpty()){
                return;
            }
            initLessons();
        }
    }
}
