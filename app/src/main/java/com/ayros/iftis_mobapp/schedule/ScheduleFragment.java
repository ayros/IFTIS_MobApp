package com.ayros.iftis_mobapp.schedule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.model.Lesson;
import com.ayros.iftis_mobapp.model.Schedule;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class ScheduleFragment extends Fragment{

    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mViewPager = view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(this.getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return ScheduleDayFragment.newInstance(i);
            }

            @Override
            public int getCount() {
                return 10;
            }
        });
        return view;
    }

}
