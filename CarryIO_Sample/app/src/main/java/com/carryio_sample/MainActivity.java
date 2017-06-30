package com.carryio_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import carry.android.sdk.view.CarryMonitor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        CarryMonitor.onViewClick(view);
    }
}
