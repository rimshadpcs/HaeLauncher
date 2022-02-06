package com.rimapps.hae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AppDrawer extends AppCompatActivity {
    RecyclerView rvDrawer;
    List<AppInfo> appsList = new ArrayList();
    RAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_drawer);

        rvDrawer = findViewById(R.id.rv_drawer);
        rAdapter = new RAdapter(appsList, getApplicationContext());
        rvDrawer.setAdapter(rAdapter);
        rvDrawer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvDrawer.setAdapter(rAdapter);

    }

}