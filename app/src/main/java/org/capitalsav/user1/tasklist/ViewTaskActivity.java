package org.capitalsav.user1.tasklist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user1.tasklist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewTaskActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TASK = "task_intent";

    private TextView mTextViewTitle;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private TextView mTextViewStageCount;
    private TextView mTextViewStageDescription;
    private MyTask mMyTask;
    private TaskManagerDbHelper mTaskManagerDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        mTextViewTitle = (TextView) findViewById(R.id.tv_task_title_view);
        mTextViewStartDate = (TextView) findViewById(R.id.tv_start_date_view);
        mTextViewEndDate = (TextView) findViewById(R.id.tv_end_date_view);
        mTextViewStageCount = (TextView) findViewById(R.id.tv_stages_count_view);
        mTextViewStageDescription = (TextView) findViewById(R.id.tv_stages_description);

        Intent intent = getIntent();
        mMyTask = (MyTask) intent.getSerializableExtra(INTENT_EXTRA_TASK);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mTextViewTitle.setText(mMyTask.getTaskName());
        mTextViewStartDate.setText(format.format(mMyTask.getStartDate().getTime()));
        mTextViewEndDate.setText(format.format(mMyTask.getEndDate().getTime()));
        mTaskManagerDbHelper = new TaskManagerDbHelper(this);

        setCurrentStage();
    }

    private void setCurrentStage() {
        ArrayList<MyStage> stageArrayList = mMyTask.getMyStages();
        int allStagesCount = stageArrayList.size();
        int stageDone = 0;
        for (int i = 0; i < stageArrayList.size(); i++) {
            if (stageArrayList.get(i).getIsStageDone() == MyStage.DONE) {
                stageDone++;
            }
            else if (stageArrayList.get(i).getIsStageDone() == MyStage.NOT_DONE) {
                mTextViewStageDescription.setText(stageArrayList.get(i).getStageName());
                String stageDescription = stageDone + "/" + allStagesCount;
                mTextViewStageCount.setText(stageDescription);
                break;
            }
        }
        checkStagesDone();
    }

    public void onClickStageDone(View view) {
        ArrayList<MyStage> arrayList = mMyTask.getMyStages();
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

    private int setStageDoneDb(int stageId) {
        SQLiteDatabase database = mTaskManagerDbHelper.getWritableDatabase();
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

    private void checkStagesDone() {
        ArrayList<MyStage> arrayList = mMyTask.getMyStages();
        int count = 0;
        for (MyStage stage : arrayList){
            if (stage.getIsStageDone() == MyStage.DONE) {
                count++;
            }
        }
        if (arrayList.size() == count){
            deleteTask();
        }
    }

    private void deleteTask() {
        if (deleteTaskFromDatabase(mMyTask.getTaskId()) >= 0) {
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

    public void onClickDeleteSingleTask(View view) {
        if (deleteTaskFromDatabase(mMyTask.getTaskId()) >= 0) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
        }
    }

    private int deleteTaskFromDatabase(int taskId) {
        SQLiteDatabase database = mTaskManagerDbHelper.getWritableDatabase();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
