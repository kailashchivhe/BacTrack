package com.kai.breathalyzer.ui.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.ConnectionListener;
import com.kai.breathalyzer.util.BacTrackSingleton;

public class HomeViewModel extends AndroidViewModel implements ConnectionListener {
    private BacTrackSingleton bacTrackSingleton;
    private MutableLiveData<Boolean> connectionLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        bacTrackSingleton = BacTrackSingleton.getInstance( application );
        connectionLiveData = new MutableLiveData<>();
    }

    public void connectToNearestBreathalyzer(){
        bacTrackSingleton.connectToNearestBreathalyzer(this);
    }

    @Override
    public void connectionSuccess() {
        connectionLiveData.postValue(true);
    }

    @Override
    public void connectionFailure(String message) {
        connectionLiveData.postValue(false);
    }

    public MutableLiveData<Boolean> getConnectionLiveData() {
        return connectionLiveData;
    }

    public boolean getBackFlag() {
        return bacTrackSingleton.getHomeBackFlag();
    }

    public void setBackFlag(boolean b) {
        bacTrackSingleton.setHomeBackFlag( b );
    }
}
