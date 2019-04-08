package com.ayros.iftis_mobapp;

import android.net.NetworkInfo;

import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;

import org.junit.Test;

public class DownloadTest implements DownloadCallback {


    @Override
    public void updateFromDownload(Object result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return null;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {

    }

    @Test
    public void downloadTest(){
        Student student = new Student();
        student.setLogin("test_login");
        student.setPassword("test_password");


    }

}
