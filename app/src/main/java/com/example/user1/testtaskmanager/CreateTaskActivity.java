package com.example.user1.testtaskmanager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {

    private final static String TAG = "=================TAG";
    private final static int DIALOG_START_DATE = 1;
    private final static int DIALOG_END_DATE = 2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<MyStage> myStageList;

    private String mTaskTitle;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

    private Calendar currentDate;

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

        currentDate = Calendar.getInstance();
    }

    public void onClickSaveTask(View view) {

    }

    public void onClickStartDate(View view) {
        new DatePickerDialog(CreateTaskActivity.this, startDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickEndDate(View view) {
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    };
}
