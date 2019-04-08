package com.ayros.iftis_mobapp.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.model.Lesson;

public class LessonView extends GridLayout {

    private TextView mStart;
    private TextView mEnd;
    private TextView mName;
    private TextView mInstructor;
    private TextView mType;
    private TextView mHoll;
    private Lesson lesson;


    public LessonView(Context context) {
        super(context);
        init();
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LessonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LessonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(),R.layout.lesson_view,this);
        mStart = this.findViewById(R.id.startView);
        mEnd = this.findViewById(R.id.endView);
        mName = this.findViewById(R.id.nameView);
        mInstructor = this.findViewById(R.id.instructorView);
        mType = this.findViewById(R.id.typeView);
        mHoll = this.findViewById(R.id.hollView);
    }

    public void setLesson(Lesson lesson){
        this.lesson = lesson;
        mStart.setText(lesson.getStart());
        mEnd.setText(lesson.getEnd());
        mName.setText(lesson.getName());
        mInstructor.setText(lesson.getInstructor());
        mType.setText(lesson.getType());
        mHoll.setText(lesson.getHoll());
    }

}
