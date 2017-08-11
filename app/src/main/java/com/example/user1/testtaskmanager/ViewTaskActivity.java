package com.example.user1.testtaskmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TaskManagerDbHelper taskManagerDbHelper;


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
        taskManagerDbHelper = new TaskManagerDbHelper(this);
        setCurrentStage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void onClickStageDone(View view) {
        ArrayList<MyStage> arrayList = myTask.getMyStages();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getIsStageDone() == MyStage.NOT_DONE){
                if (setStageDoneDb(arrayList.get(i).getStageId()) >= 0) {
                    arrayList.get(i).setIsStageDone(MyStage.DONE);
                    setCurrentStage();
                }
                else {
                    Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void onClickDeleteSingleTask(View view) {
        if (deleteTask(myTask.getTaskId()) >= 0) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickBackFromViewTask(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
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
        checkStagesDone();
    }

    private int setStageDoneDb(int stageId) {
        SQLiteDatabase database = taskManagerDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskManagerContract.StageInDb.COLUMN_STAGE_IS_DONE, MyStage.DONE);
        String[] whereValues = {String.valueOf(stageId)};
        int rows = -1;
        try {
           rows = database.update(TaskManagerContract.StageInDb.TABLE_NAME, contentValues,
                    TaskManagerContract.StageInDb._ID + "= ?", whereValues );

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            database.close();
        }
        return rows;
    }

    private int deleteTask(int taskId) {
        SQLiteDatabase database = taskManagerDbHelper.getWritableDatabase();
        int rows = -1;
        try {
            rows = database.delete(TaskManagerContract.TaskInDb.TABLE_NAME,
                    TaskManagerContract.TaskInDb._ID + "= ?", new String[]{String.valueOf(taskId)});
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            database.close();
        }
        return rows;

    }

    private void checkStagesDone() {
        ArrayList<MyStage> arrayList = myTask.getMyStages();
        int count = 0;
        for (MyStage stage : arrayList){
            if (stage.getIsStageDone() == MyStage.DONE) {
                count++;
            }
        }
        if (arrayList.size() == count){
            if (deleteTask(myTask.getTaskId()) >= 0) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
