package com.kai.breathalyzer.ui.profile;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.ProfileRetrivalListener;
import com.kai.breathalyzer.listener.ProfileUpdateListener;
import com.kai.breathalyzer.model.User;
import com.kai.breathalyzer.util.APIHelper;


public class ProfileViewModel extends AndroidViewModel implements ProfileUpdateListener, ProfileRetrivalListener {
    MutableLiveData<String> messageMutableLiveData;
    MutableLiveData<Boolean> booleanMutableLiveData;
    MutableLiveData<User> userMutableLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        messageMutableLiveData = new MutableLiveData<>();
        booleanMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }
    // TODO: Implement the ViewModel
    public void updateProfile(String firstName, String lastName,String id,String jwtToken){
        //API helper update profile
        User user = new User(firstName,lastName,id,jwtToken);
        APIHelper.profileUpdate(user,this);

    }
    public void retriveProfile(String id,String jwtToken){
        //API helper retrive profile
        APIHelper.profileRetrival(id,jwtToken,this);
    }

    @Override
    public void profileUpdateSuccessful() {
        booleanMutableLiveData.postValue(true);
    }

    @Override
    public void profileUpdateFailure(String message) {
        messageMutableLiveData.postValue(message);
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        return booleanMutableLiveData;
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
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
}