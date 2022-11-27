package com.kai.breathalyzer.util;


import android.support.annotation.NonNull;

import com.kai.breathalyzer.listener.HistoryRetrivalListener;
import com.kai.breathalyzer.listener.LoginListener;
import com.kai.breathalyzer.listener.MeasurementSaveListener;
import com.kai.breathalyzer.listener.ProfileRetrivalListener;
import com.kai.breathalyzer.listener.ProfileUpdateListener;
import com.kai.breathalyzer.listener.RegistrationListener;
import com.kai.breathalyzer.model.LoginDetails;
import com.kai.breathalyzer.model.User;
import com.kai.breathalyzer.model.UserHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIHelper {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "APIHelper";

    private static final String DOMAIN_NAME = "https://bac-track-breathalyzer.herokuapp.com/";

    private static final String LOGIN_URL= DOMAIN_NAME + "api/auth/login";
    private static final String REGISTRATION_URL = DOMAIN_NAME + "/api/auth/signup";
    private static final String PROFILE_GET_URL = DOMAIN_NAME + "/api/auth/profile";
    private static final String PROFILE_POST_URL = DOMAIN_NAME + "/api/auth/profile";
    private static final String HISTORY_URL = DOMAIN_NAME + "/history";
    private static final String MEASUREMENT_URL = DOMAIN_NAME + "/newMeasurement";

    public APIHelper() {
    }

    public static void login(String email, String password, LoginListener loginListener) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        String id = res.getString("id");
                        String jwtToken = res.getString("token");
                        String customerId = res.getString("customerId");

                        loginListener.loginSuccessfull(new LoginDetails(id, jwtToken, customerId));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject loginFailure = new JSONObject(response.body().string());
                        loginListener.loginFailure(loginFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void register(String email, String password, String firstName, String lastName, RegistrationListener registerationListener) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .build();

        Request request = new Request.Builder()
                .url(REGISTRATION_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    JSONObject jsonMessage = null;
                    try {
                        jsonMessage = new JSONObject(response.body().string());
                        registerationListener.registerationSuccessful();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject jsonErrorMessage = null;
                    try {
                        jsonErrorMessage = new JSONObject(response.body().string());
                        registerationListener.registerationFailure(jsonErrorMessage.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public static void profileRetrival(String id, String jwtToken, ProfileRetrivalListener profileRetrivalListener){

        HttpUrl url = HttpUrl.parse(PROFILE_GET_URL).newBuilder()
                .addQueryParameter("token",jwtToken)
                .addQueryParameter("userId",id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        JSONObject user = res.getJSONObject("data");
                        String email = user.getString("email");
                        String firstname = user.getString("firstName");
                        String lastname = user.getString("lastName");

                        profileRetrivalListener.profileRetrivalSuccessful(new User(firstname,lastname,email));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject profileRetrivalFailure = new JSONObject(response.body().toString());
                        profileRetrivalListener.profileRetrivalFailure(profileRetrivalFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void profileUpdate(User user, ProfileUpdateListener profileUpdateListener){

        FormBody formBody = new FormBody.Builder()
                .add("id", user.getId())
                .add("firstName", user.getFirstName())
                .add("lastName", user.getLastName())
                .build();

        HttpUrl url = HttpUrl.parse(PROFILE_POST_URL).newBuilder()
                .addQueryParameter("token",user.getJwtToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    profileUpdateListener.profileUpdateSuccessful();
                } else {
                    try {
                        JSONObject updateFailure = new JSONObject(response.body().string());
                        profileUpdateListener.profileUpdateFailure(updateFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void saveMeasurement(String measurement, String jwtToken, MeasurementSaveListener measurementSaveListener){
        FormBody formBody = new FormBody.Builder()
                .add("measurement", measurement)
                .build();

        HttpUrl url = HttpUrl.parse(MEASUREMENT_URL).newBuilder()
                .addQueryParameter("token",jwtToken)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    measurementSaveListener.measurementSaveSuccessfull();
                } else {
                    try {
                        JSONObject updateFailure = new JSONObject(response.body().string());
                        measurementSaveListener.measurementSaveFailure(updateFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void historyRetrival(String jwtToken, HistoryRetrivalListener historyRetrivalListener){
        HttpUrl url = HttpUrl.parse(HISTORY_URL).newBuilder()
                .addQueryParameter("token",jwtToken)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
//                        JSONObject res = new JSONObject(response.body().string());
                        List<UserHistory> userHistoryList = new ArrayList<>();
                        JSONArray array = new JSONArray(response.body().string());
                        int n = array.length();
                        for(int i=0;i<n;i++){
                            JSONObject obj = array.getJSONObject(i).getJSONObject("history");
                            String measurement = obj.getString("measurement");
                            String date = obj.getString("date");
                            userHistoryList.add(new UserHistory(measurement,date));
                        }
                        historyRetrivalListener.historyRetrivalSuccessfull(userHistoryList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject historyRetrivalFailure = new JSONObject(response.body().toString());
                        historyRetrivalListener.historyRetrivalFailure(historyRetrivalFailure.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
