package com.ayros.iftis_mobapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayros.iftis_mobapp.autorization.LoginFragment;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.DatabaseCallback;
import com.ayros.iftis_mobapp.db.UserDao;
import com.ayros.iftis_mobapp.home.NewsDataBaseAction;
import com.ayros.iftis_mobapp.home.NewsInsertDatabaseAction;
import com.ayros.iftis_mobapp.model.News;
import com.ayros.iftis_mobapp.model.Schedule;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;
import com.ayros.iftis_mobapp.schedule.ScheduleDataAction;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class FillerHomeFragment extends Fragment {

    private static final String URL_UPDATE = "/news/get_update";
    private static final String URL_DOWNLOAD = "/news/create";
    private final static String URL_SCHEDULE = "/schedule/download";

    private static final String DIALOG_LOGIN = "DialogLogin";
    private static final int REQUEST_LOGIN = 0;

    private Data data;
    private NetworkFragment network;
    private NavController navController;
    private NewsDownload newsDownload;
    private NewsDatabaseGet newsDatabaseGet;
    private NewsDataBaseInsert newsDataBaseInsert;
    private ScheduleDownload scheduleDownload;
    private Student user;

    public FillerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsDownload = new NewsDownload();
        newsDatabaseGet = new NewsDatabaseGet();
        newsDataBaseInsert = new NewsDataBaseInsert();
        scheduleDownload = new ScheduleDownload();
    }

    @Override
    public void onResume(){
        super.onResume();
        data = Data.getInstance(getContext());
        startStudent();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filler_home, container, false);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        return v;
    }

    private void startStudent(){
        DataBaseAction action = new DataBaseAction() {
            private Student student;
            @Override
            public void findData() {
                UserDao dao = data.getUserDao();
                student = dao.getFirst();
            }

            @Override
            public void finished() {
                if(student == null){
                    FragmentManager manager =  getFragmentManager();
                    LoginFragment dialog = new LoginFragment();
                    dialog.setCancelable(false);
                    dialog.setTargetFragment(FillerHomeFragment.this,REQUEST_LOGIN);
                    dialog.show(manager, DIALOG_LOGIN);
                }
                else {
                    user = student;
                    startNewsUpdate();
                    startScheduleUpdate();
                }
            }
        };
        data.getData(action);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_LOGIN){
            user = (Student) data.getSerializableExtra(LoginFragment.EXTRA_STUDENT);
            startNewsUpdate();
            startScheduleUpdate();
        }
    }

    private void startNewsUpdate(){
        NewsDataBaseAction action = new NewsDataBaseAction(getContext(),newsDatabaseGet);
        data.getData(action);
    }

    private void startScheduleUpdate(){
        network = NetworkFragment.getInstance(getFragmentManager(),getString(R.string.server_url)+URL_SCHEDULE);
        network.setCallback(scheduleDownload);
        String[] msg = new String[2];
        msg[0] = user.getLogin();
        msg[1] = user.getPassword();
        network.startDownload(msg);
    }

    private void updateFinished(){
        if((scheduleDownload==null)&&(newsDataBaseInsert==null)&&(newsDownload==null)&&(newsDatabaseGet==null) ){
            navController.navigate(R.id.action_fillerHomeFragment_to_homeFragment);
        }
    }

    private class NewsDownload implements DownloadCallback<String> {

        @Override
        public void updateFromDownload(String result) {
            NewsInsertDatabaseAction action = new NewsInsertDatabaseAction(getContext(),
                    newsDataBaseInsert,deserializeNews(result));
            data.getData(action);
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
            newsDownload = null;
            updateFinished();

        }

        private List<News> deserializeNews(String str){
            ObjectMapper mapper = new ObjectMapper();
            List<News> news;
            try{
                news = Arrays.asList(mapper.readValue(str, News[].class));
            } catch (IOException e) {
                e.printStackTrace();
                news = new ArrayList<>();
            }
            return news;
        }
    }

    private class NewsDatabaseGet implements DatabaseCallback<News>{

        private List<News> newsList;

        @Override
        public void finished(List<News> result) {
            newsList = result;
            newsDatabaseGet = null;
            updateFinished();
            updateNetwork();
        }

        private void updateNetwork(){
            if(newsList.isEmpty()){
                network = NetworkFragment.getInstance(getFragmentManager(), getString(R.string.server_url)+URL_DOWNLOAD);
                network.setCallback(newsDownload);
                network.startDownload("");
            }
            else {
                network = NetworkFragment.getInstance(getFragmentManager(), getString(R.string.server_url)+URL_UPDATE);
                network.setCallback(newsDownload);
                network.startDownload(newsList.get(newsList.size()-1).getId());
            }
        }
    }

    private class NewsDataBaseInsert implements DatabaseCallback<News>{
        @Override
        public void finished(List<News> result) {
            newsDataBaseInsert = null;
            updateFinished();
        }
    }

    private class ScheduleDownload implements DownloadCallback<String>{

        @Override
        public void updateFromDownload(String result) {
            Schedule[] schedules = deserializeSchedule(result);
            if (schedules.length >1){
                ScheduleDataAction action = new ScheduleDataAction(schedules,getContext());
                data.getData(action);
                network = NetworkFragment.getInstance(getFragmentManager(),getString(R.string.server_url));
            }else {

            }

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
            scheduleDownload = null;
            updateFinished();
        }

        private Schedule[] deserializeSchedule(String result){
            ObjectMapper mapper = new ObjectMapper();
            Schedule[] list;
            if(result == null){
                return new Schedule[1];
            }
            try {
                list = mapper.readValue(result,Schedule[].class);
            } catch (IOException e) {
                e.printStackTrace();
                list = new Schedule[1];
            }
            return list;
        }
    }
}
