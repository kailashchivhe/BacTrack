package com.kai.breathalyzer.listener;

import com.kai.breathalyzer.model.UserHistory;

import java.util.List;

public interface HistoryRetrivalListener {
    void historyRetrivalSuccessfull(List<UserHistory> userHistoryList);
    void historyRetrivalFailure(String message);
}
