package com.kai.breathalyzer.ui.results;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class GraphViewModel extends AndroidViewModel {
    public GraphViewModel(@NonNull Application application) {
        super(application);
    }

    public void getData(){
        //TODO make API call
    }
}
