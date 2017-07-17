package com.example.user1.testtaskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {

    private final static String TAG = "=================TAG";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<MyStage> myStageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stages);

        mRecyclerView.setHasFixedSize(true);

        /*user linear layout manager*/
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewStageAdapter(myStageList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
