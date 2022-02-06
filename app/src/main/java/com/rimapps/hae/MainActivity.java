package com.rimapps.hae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int CITY_STATE_BERLIN = 0;
    private static final int CITY_STATE_LONDON = 1;
    private static final int CITY_STATE_EDINBURGH = 2;
    private static final int CITY_STATE_CARDIFF = 3;
    private static final int CITY_STATE_BEIJING = 4;
    private static final int CITY_STATE_NOTTINGHAM = 5;

    private int CURRENT_STATE = 0;

    TextView tvBatteryLevel, tvTemperature, tvCity, tvCountry, tvDesc;
    Button btLaunchApps;

    private BroadcastReceiver mBatteryLevelReciver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {

            int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawLevel >= 0 && scale > 0) {
                level = (rawLevel * 100) / scale;
            }
            tvBatteryLevel.setText(level + "%");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBatteryLevel = findViewById(R.id.tv_battery_level);
        fetchingJSON();
        btLaunchApps = findViewById(R.id.bt_launch_apps);

        btLaunchApps.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AppDrawer.class);
            startActivity(intent);
        });
        batteryLevel();

        findViewById(R.id.rl_cardWeather).setOnClickListener(view -> {
            weatherSwitch();
        });

    }


    private void weatherSwitch() {
        CardView cardWeather = findViewById(R.id.card_weather);
        switch (CURRENT_STATE) {
            case CITY_STATE_BERLIN:
                getWeatherData("london");
                CURRENT_STATE = CITY_STATE_LONDON;

                break;
            case CITY_STATE_LONDON:
                getWeatherData("edinburgh");
                CURRENT_STATE = CITY_STATE_EDINBURGH;
                break;
            case CITY_STATE_EDINBURGH:
                getWeatherData("cardiff");
                CURRENT_STATE = CITY_STATE_CARDIFF;
                break;
            case CITY_STATE_CARDIFF:
                getWeatherData("beijing");
                CURRENT_STATE = CITY_STATE_BEIJING;
                break;
            case CITY_STATE_BEIJING:
                getWeatherData("nottingham");
                CURRENT_STATE = CITY_STATE_NOTTINGHAM;
                break;
            case CITY_STATE_NOTTINGHAM:
            default:
                getWeatherData("berlin");
                CURRENT_STATE = CITY_STATE_BERLIN;
                break;
        }
    }

    private void fetchingJSON() {


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                weatherSwitch();
            }
        }, 0, 10000);


    }

    private void getWeatherData(String endPoint) {

        String base_url = "http://weather.bfsah.com/" + endPoint;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, base_url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String temperature = obj.getString("temperature");
                        String city = obj.getString("city");
                        String country = obj.getString("country");
                        String description = obj.getString("description");


                        //Toast.makeText(MainActivity.this, city, Toast.LENGTH_SHORT).show();
                        String weather = temperature + "°C";
                        tvTemperature = findViewById(R.id.tv_weather);
                        tvCity = findViewById(R.id.tv_city);
                        tvCountry = findViewById(R.id.tv_country);
                        tvDesc = findViewById(R.id.tv_desc);

                        tvTemperature.setText(weather);
                        tvCity.setText(city);
                        tvCountry.setText(country);
                        tvDesc.setText(description);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        tvTemperature.setText("0 C");
                        tvCity.setText("NA");
                        tvCountry.setText("NA");
                        tvDesc.setText("NA");

                    }

                },
                error -> {
                    error.printStackTrace();
                    tvTemperature.setText("0");
                    tvCity.setText("NA");
                    tvCountry.setText("NA");
                    tvDesc.setText("NA");
                }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void batteryLevel() {
        IntentFilter batteryLevelFliter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryLevelReciver, batteryLevelFliter);
    }
}
