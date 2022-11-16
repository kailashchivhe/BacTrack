package com.kai.breathalyzer.listener;


import com.kai.breathalyzer.model.User;

public interface ProfileRetrivalListener {
    void profileRetrivalSuccessful(User user);
    void profileRetrivalFailure(String message);
}
