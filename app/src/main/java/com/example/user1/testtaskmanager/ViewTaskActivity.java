package com.example.user1.testtaskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewTaskActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TASK = "task_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
    }
}
