package com.example.user1.testtaskmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private TaskManagerDbHelper mTaskManagerDbHelper;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<MyTask> taskArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskManagerDbHelper = new TaskManagerDbHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        /*use linear layout manager*/
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(taskArrayList);
        mRecyclerView.setAdapter(mAdapter);
        selectAllTasks();
    }


    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }

    private void selectAllTasks() {
        SQLiteDatabase database = mTaskManagerDbHelper.getReadableDatabase();
        String[] selection = {
                TaskManagerContract.TaskInDb._ID,
                TaskManagerContract.TaskInDb.COLUMN_TASK_NAME,
                TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE,
                TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE
        };

        Cursor cursor = database.query(
                TaskManagerContract.TaskInDb.TABLE_NAME,
                selection,
                null,
                null,
                null,
                null,
                null
        );

        try{
            int idColumnIndex = cursor.getColumnIndex(TaskManagerContract.TaskInDb._ID);
            int nameColumnIndex = cursor.getColumnIndex(TaskManagerContract.TaskInDb.COLUMN_TASK_NAME);
            int startDateColumnIndex = cursor.getColumnIndex(TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE);
            int endDateColumnIndex = cursor.getColumnIndex(TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String taskName = cursor.getString(nameColumnIndex);
                ArrayList<MyStage> stageArrayList = new ArrayList<>();

                String startDateString = cursor.getString(startDateColumnIndex);
                String endDateString = cursor.getString(endDateColumnIndex);

                String [] selectionStage = {
                        TaskManagerContract.StageInDb.COLUMN_STAGE_NAME,
                        TaskManagerContract.StageInDb.COLUMN_STAGE_IS_DONE,
                        TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID
                };

                String selectionWhere = TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID + "=?";
                String[] selectionWhereArgs = {String.valueOf(currentId)};

                Cursor cursorStage = database.query(
                        TaskManagerContract.StageInDb.TABLE_NAME,
                        selectionStage,
                        selectionWhere,
                        selectionWhereArgs,
                        null,
                        null,
                        null
                );
                int idStageColumnIndex = cursorStage.getColumnIndex(TaskManagerContract.StageInDb._ID);
                int nameStageColumnIndex = cursorStage.getColumnIndex(TaskManagerContract.StageInDb.COLUMN_STAGE_NAME);
                int isDoneStageColumnIndex = cursorStage.getColumnIndex(TaskManagerContract.StageInDb.COLUMN_STAGE_IS_DONE);

                while (cursorStage.moveToNext()){
                    MyStage myStage = new MyStage();
                    myStage.setmStageName(cursorStage.getString(nameStageColumnIndex));
                    myStage.setIsStageDone(cursorStage.getInt(isDoneStageColumnIndex));
                    stageArrayList.add(myStage);
                }
                MyTask myTask = new MyTask();
                myTask.setmTaskName(taskName);
                myTask.setMyStages(stageArrayList);
                myTask.setmStartDate(getCalendarFromString(startDateString));
                myTask.setmEndDate(getCalendarFromString(endDateString));
                taskArrayList.add(myTask);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
            database.close();
        }
    }

    private Calendar getCalendarFromString(String string) {
        int firstLineIndexOf = string.indexOf("-");
        int lastLineIndexOf = string.lastIndexOf("-");
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(string.substring(0, firstLineIndexOf));
        int month = Integer.parseInt(string.substring(firstLineIndexOf + 1, lastLineIndexOf));
        int day = Integer.parseInt(string.substring(lastLineIndexOf + 1, string.length()));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }
}
