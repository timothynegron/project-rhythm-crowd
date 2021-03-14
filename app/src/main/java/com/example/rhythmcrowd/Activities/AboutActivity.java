package com.example.rhythmcrowd.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmcrowd.R;


public class AboutActivity extends AppCompatActivity {

    //**********************************************************************************************
    //Method: onCreate(@Nullable Bundle savedInstanceState)
    //
    //Purpose: Part of Android OS Activity LifeCycle. Overriding method to display about activity.
    //
    //
    //**********************************************************************************************
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
