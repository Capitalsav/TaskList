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

    public final static int REQUEST_CODE_VIEW_TASK = 2;
    public final static String INTENT_RESULT_NEW_TASK = "new task";
    private final static String TAG = "=================TAG: ";
    private final static int REQUEST_CODE_NEW_TASK = 1;

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

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_TASK:
//                    MyTask myTask = (MyTask) data.getSerializableExtra(INTENT_RESULT_NEW_TASK);
//                    taskArrayList.add(myTask);
                    taskArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                    selectAllTasks();
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, " select all");
                case REQUEST_CODE_VIEW_TASK:
                    taskArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                    selectAllTasks();
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, " select all");
            }
        }
        else  {
            taskArrayList.clear();
            mAdapter.notifyDataSetChanged();
            selectAllTasks();
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, " select all");
        }
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

        try {
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

                String[] selectionStage = {
                        TaskManagerContract.StageInDb._ID,
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

                while (cursorStage.moveToNext()) {
                    MyStage myStage = new MyStage();
                    myStage.setStageId(cursorStage.getInt(idStageColumnIndex));
                    myStage.setmStageName(cursorStage.getString(nameStageColumnIndex));
                    myStage.setIsStageDone(cursorStage.getInt(isDoneStageColumnIndex));
                    stageArrayList.add(myStage);
                }
                MyTask myTask = new MyTask();
                myTask.setTaskId(currentId);
                myTask.setmTaskName(taskName);
                myTask.setMyStages(stageArrayList);
                myTask.setmStartDate(getCalendarFromString(startDateString));
                myTask.setmEndDate(getCalendarFromString(endDateString));
                myTask.setChecked(false);//default set false for isChecked
                taskArrayList.add(myTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public void onClickDeleteMultipleTask(View view) {
        for (int i = taskArrayList.size() - 1; i >= 0; i--) {
            if (taskArrayList.get(i).isChecked()) {
                if (deleteTask(taskArrayList.get(i).getTaskId()) >= 0) {
                    taskArrayList.remove(taskArrayList.get(i));
                    //deleteTaskFromList(myTask.getTaskId());
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "delete success multiple");
                } else {
                    Log.d(TAG, "delete error multiple");
                }
            }
        }
    }

    private int deleteTask(int taskId) {
        SQLiteDatabase database = mTaskManagerDbHelper.getWritableDatabase();
        int rows = -1;
        try {
            rows = database.delete(TaskManagerContract.TaskInDb.TABLE_NAME, TaskManagerContract.TaskInDb._ID + "= ?", new String[]{String.valueOf(taskId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
        return rows;
    }
}