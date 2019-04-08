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


public class ScheduleFragment extends Fragment implements DownloadCallback<String> {

    private final static String url = "/schedule/download";

    private ViewPager mViewPager;
    private NetworkFragment network;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String address = getString(R.string.server_url)+url;
        network = NetworkFragment.getInstance(getFragmentManager(),address);

    }

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
        Student student = (Student)getArguments().getSerializable("user");
        String[] args = {student.getLogin(),student.getPassword()};
        network.setCallback(this);
        network.startDownload(args);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateFromDownload(String result) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Schedule[] schedules = mapper.readValue(result, Schedule[].class);
            ScheduleDataAction action = new ScheduleDataAction(schedules, getActivity());
            Data.getInstance(getActivity()).getData(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mViewPager.setCurrentItem(0);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {

    }

}
