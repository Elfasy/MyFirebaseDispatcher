package com.example.myfirebasedispatcher;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MyJobService extends JobService {
    public static final String TAG = MyJobService.class.getSimpleName();
    final String APP_ID = "d5569f52336a5ca84f5e4cad0c18726c";
    public static String EXTRAS_CITY = "extras_city";

    @Override
    public boolean onStartJob(JobParameters job) {
        getCurrentWeather(job);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        AjatHelper.toast(getApplicationContext(),"Job berhenti");
        return true;
    }

    private void getCurrentWeather(final JobParameters jobParameters) {
        Bundle extras = jobParameters.getExtras();

        if (extras == null) {
            jobFinished(jobParameters, false);
            return;
        } else if (extras.isEmpty()) {
            jobFinished(jobParameters, false);
            return;
        }

        String city = extras.getString(EXTRAS_CITY);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APP_ID;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String currentWeather = responseObject.getJSONArray("weather").getJSONObject(0).getString("main");
                    String description = responseObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    double tempInKelvin = responseObject.getJSONObject("main").getDouble("temp");

                    double tempInCelcius = tempInKelvin - 273;
                    String temperature = new DecimalFormat("##.##").format(tempInCelcius);

                    String title = "Current Weather";
                    String message = currentWeather + ", " + description + " with " + temperature + " celcius";
                    int notifId = 100;
                    AjatHelper.showNotificationAjat(getApplicationContext(), title, message, notifId, "Channel_1", "Weather Channer", R.drawable.ic_cake_black);

                    jobFinished(jobParameters, false);
                } catch (Exception e) {
                    jobFinished(jobParameters, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                jobFinished(jobParameters,true);
            }
        });
    }
}
