package com.ayros.iftis_mobapp.db;

import java.util.List;

public interface DatabaseCallback<T> {

    public void finished(List<T> result);

}
