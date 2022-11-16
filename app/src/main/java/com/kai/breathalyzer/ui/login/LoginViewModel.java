package com.kai.breathalyzer.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;



import com.kai.breathalyzer.listener.LoginListener;
import com.kai.breathalyzer.model.LoginDetails;
import com.kai.breathalyzer.util.APIHelper;


public class LoginViewModel extends AndroidViewModel implements LoginListener {

    MutableLiveData<String> messageMutableLiveData;
    MutableLiveData<LoginDetails> loginDetailsMutableLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        messageMutableLiveData = new MutableLiveData<>();
        loginDetailsMutableLiveData = new MutableLiveData<>();
    }

    public void login(String email, String password){
        //API helper function called
        APIHelper.login(email,password,this);
    }

    @Override
    public void loginSuccessfull(LoginDetails loginDetails) {
        loginDetailsMutableLiveData.postValue(loginDetails);
    }

    @Override
    public void loginFailure(String message) {
        messageMutableLiveData.postValue(message);
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }

    public MutableLiveData<LoginDetails> getLoginDetailsMutableLiveData() {
        return loginDetailsMutableLiveData;
    }
}