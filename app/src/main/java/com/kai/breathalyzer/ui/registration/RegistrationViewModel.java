package com.kai.breathalyzer.ui.registration;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.RegistrationListener;
import com.kai.breathalyzer.util.APIHelper;


public class RegistrationViewModel extends AndroidViewModel implements RegistrationListener {

    MutableLiveData<String> messageMutableLiveData;
    MutableLiveData<Boolean> booleanMutableLiveData;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        messageMutableLiveData = new MutableLiveData<>();
        booleanMutableLiveData = new MutableLiveData<>();
    }
    // TODO: Implement the ViewModel
    public void registration(String email,String password, String firstName, String lastName){
        //API register called
        APIHelper.register(email,password,firstName,lastName,this);
    }

    @Override
    public void registerationSuccessful() {
        booleanMutableLiveData.postValue(true);
    }

    @Override
    public void registerationFailure(String message) {
        messageMutableLiveData.postValue(message);
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        return booleanMutableLiveData;
    }
}