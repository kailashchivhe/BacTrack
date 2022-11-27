package com.kai.breathalyzer.ui.results;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.HistoryRetrivalListener;
import com.kai.breathalyzer.model.UserHistory;
import com.kai.breathalyzer.util.APIHelper;

import java.util.List;

public class GraphViewModel extends AndroidViewModel implements HistoryRetrivalListener {
    MutableLiveData<List<UserHistory>> userHistoryMutableLiveData;

    public GraphViewModel(@NonNull Application application) {
        super(application);
        userHistoryMutableLiveData = new MutableLiveData<>();
    }

    public void getData(String jwtToken){
        APIHelper.historyRetrival(jwtToken, this);
    }

    @Override
    public void historyRetrivalSuccessfull(List<UserHistory> userHistoryList) {
        userHistoryMutableLiveData.postValue( userHistoryList );
    }

    @Override
    public void historyRetrivalFailure(String message) {
        userHistoryMutableLiveData.postValue( null );
    }

    public MutableLiveData<List<UserHistory>> getUserHistoryMutableLiveData() {
        return userHistoryMutableLiveData;
    }
}
