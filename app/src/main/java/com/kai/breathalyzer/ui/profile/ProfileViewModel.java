package com.kai.breathalyzer.ui.profile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.ProfileRetrivalListener;
import com.kai.breathalyzer.model.User;
import com.kai.breathalyzer.util.APIHelper;


public class ProfileViewModel extends AndroidViewModel implements ProfileRetrivalListener {

    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<String> messageMutableLiveData;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        messageMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }
    // TODO: Implement the ViewModel
    public void retriveProfile(String id,String jwtToken){
        //API helper retrive profile
        APIHelper.profileRetrival(id,jwtToken,this);
    }

    @Override
    public void profileRetrivalSuccessful(User user) {
        userMutableLiveData.postValue(user);
    }

    @Override
    public void profileRetrivalFailure(String message) {
        messageMutableLiveData.postValue(message);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }
}