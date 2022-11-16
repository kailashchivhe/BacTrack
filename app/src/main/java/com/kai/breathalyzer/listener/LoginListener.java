package com.kai.breathalyzer.listener;


import com.kai.breathalyzer.model.LoginDetails;

public interface LoginListener {
    void loginSuccessfull(LoginDetails loginDetails);
    void loginFailure(String message);
}
