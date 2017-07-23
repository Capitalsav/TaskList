package com.example.user1.testtaskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewTaskActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TASK = "task_intent";

    private TextView textViewTitle;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private TextView textViewStageCount;
    private TextView textViewStageDescription;

    private MyTask myTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        textViewTitle = (TextView) findViewById(R.id.tv_task_title_view);
        textViewStartDate = (TextView) findViewById(R.id.tv_start_date_view);
        textViewEndDate = (TextView) findViewById(R.id.tv_end_date_view);
        textViewStageCount = (TextView) findViewById(R.id.tv_stages_count_view);
        textViewStageDescription = (TextView) findViewById(R.id.tv_stages_description);
        Intent intent = getIntent();
        myTask = (MyTask) intent.getSerializableExtra(INTENT_EXTRA_TASK);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        textViewTitle.setText(myTask.getmTaskName());
        textViewStartDate.setText(format.format(myTask.getmStartDate().getTime()));
        textViewEndDate.setText(format.format(myTask.getmEndDate().getTime()));
        setCurrentStage();
    }

    private void setCurrentStage() {
        ArrayList<MyStage> stageArrayList = myTask.getMyStages();
        int allStagesCount = stageArrayList.size();
        int stageDone = 0;
        for (int i = 0; i < stageArrayList.size(); i++) {
            if (stageArrayList.get(i).getIsStageDone() == MyStage.DONE) {
                stageDone++;
            }
            else if (stageArrayList.get(i).getIsStageDone() == MyStage.NOT_DONE) {
                textViewStageDescription.setText(stageArrayList.get(i).getmStageName());
                String stageDescription = stageDone + "/" + allStagesCount;
                textViewStageCount.setText(stageDescription);
                break;
            }
        }
    }
}
