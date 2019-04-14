package com.ayros.iftis_mobapp.autorization;

import android.content.Context;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.db.UserDao;
import com.ayros.iftis_mobapp.model.Student;

public class UserInsertAction implements DataBaseAction {

    private Data data;
    private UserDao dao;
    private Student student;

    UserInsertAction(Context context, Student student){
        data = Data.getInstance(context);
        dao = data.getUserDao();
        this.student = student;
    }
    @Override
    public void findData() {
        Student previous = dao.getFirst();
        if(previous != null){
            dao.delete(previous);
        }
        dao.insertAll(student);
    }

    @Override
    public void finished() {

    }
}
