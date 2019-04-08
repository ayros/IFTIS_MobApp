package com.ayros.iftis_mobapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.UserDao;
import com.ayros.iftis_mobapp.model.Schedule;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;
import com.ayros.iftis_mobapp.schedule.ScheduleDataAction;
import com.ayros.iftis_mobapp.schedule.ScheduleFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private Student user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle args = new Bundle();
            args.putSerializable("user",user);
            switch (item.getItemId()){
                case R.id.navigation_home:
                    navController.navigate(R.id.action_global_homeFragment,args);
                    break;
                case  R.id.navigation_dashboard:
                    navController.navigate(R.id.action_global_scheduleFragment,args);
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        DataBaseAction action = new DataBaseAction() {
            private Student student;
            @Override
            public void findData() {
                UserDao dao = Data.getInstance(getApplicationContext()).getUserDao();
                student = dao.getFirst();
            }

            @Override
            public void finised() {
                dataBaseResult(student);
            }
        };

        Data.getInstance(getApplicationContext()).getData(action);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(user == null){
            user = Data.getInstance(this).getStudent();
        }
    }

    private void dataBaseResult(Student student){
        if(student == null){
            navController.navigate(R.id.action_homeFragment_to_loginActivity);
        }
        else {
            this.user = student;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
