package com.example.user1.testtaskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "=================TAG: ";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<MyTask> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //=====================================================
        test();
        test_GerFromDb();
        //=====================================================

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        /*use linear layout manager*/
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    void test() {
        MyStage myStage1 = new MyStage(1, "myFirstStage");
        ArrayList<MyStage> arrayList = new ArrayList<>();
        arrayList.add(myStage1);

        Calendar calendar = new GregorianCalendar();
        calendar.set(2017, 6, 8, 12, 12, 12);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.set(2018, 6, 8, 12, 12, 12);
        MyTask myTask = new MyTask("TestTask1", arrayList, calendar, calendar2);
        myTask.save();
    }

    void test_GerFromDb() {
        List<MyTask> list = MyTask.listAll(MyTask.class);
        mDataset = list;
    }


    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }
}
