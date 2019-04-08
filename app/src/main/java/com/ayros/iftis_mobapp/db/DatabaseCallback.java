package com.ayros.iftis_mobapp.db;

public interface DatabaseCallback<T> {

    public void finished(T... result);

}
