package com.example.user1.testtaskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public final static String INTENT_LIST_FOR_NOTIFICATION = "tasks for notification";
    public final static String DATE_PARSE_ERROR = "Date parse error.";
    public final static int REQUEST_CODE_VIEW_TASK = 2;
    private final static int REQUEST_CODE_NEW_TASK = 1;

    private TaskManagerDbHelper mTaskManagerDbHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyTask> mTaskArrayList = new ArrayList<>();
    private AlarmManager mAlarmManager;
    private Calendar mCurrentCalendar;
    private Calendar mTargetCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskManagerDbHelper = new TaskManagerDbHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(mTaskArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mCurrentCalendar = Calendar.getInstance();
        mTargetCalendar = Calendar.getInstance();
        mTargetCalendar.set(Calendar.HOUR, 8);
        mTargetCalendar.set(Calendar.MINUTE, 30);
        mTargetCalendar.set(Calendar.SECOND, 0);
        mTargetCalendar.set(Calendar.AM_PM, Calendar.AM);
        selectAllTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTaskArrayList.clear();
        selectAllTasks();
        mAdapter.notifyDataSetChanged();

    }

    /*TODO refactor this method*/
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
                    myStage.setStageName(cursorStage.getString(nameStageColumnIndex));
                    myStage.setIsStageDone(cursorStage.getInt(isDoneStageColumnIndex));
                    stageArrayList.add(myStage);
                }
                MyTask myTask = new MyTask();
                myTask.setTaskId(currentId);
                myTask.setTaskName(taskName);
                myTask.setMyStages(stageArrayList);
                myTask.setStartDate(getCalendarFromString(startDateString));
                myTask.setEndDate(getCalendarFromString(endDateString));
                myTask.setChecked(false);//default set false for isChecked
                mTaskArrayList.add(myTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            database.close();
        }
        setUserNotifications();
    }

    private Calendar getCalendarFromString(String string) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, DATE_PARSE_ERROR, Toast.LENGTH_SHORT).show();
        }
        calendar.add(Calendar.MONTH, 1);
        return calendar;
    }

    public void onClickDeleteMultipleTask(View view) {
        for (int i = mTaskArrayList.size() - 1; i >= 0; i--) {
            if (mTaskArrayList.get(i).isChecked()) {
                if (deleteTask(mTaskArrayList.get(i).getTaskId()) >= 0) {
                    mTaskArrayList.remove(mTaskArrayList.get(i));
                    mAdapter.notifyDataSetChanged();
                } else {
                    /*TODO notification for user if error occurred*/
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

    private void setUserNotifications() {
        long targetTime = mTargetCalendar.getTimeInMillis();
        long currentTime = mCurrentCalendar.getTimeInMillis();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < mTaskArrayList.size(); i++) {
            arrayList.add(mTaskArrayList.get(i).getTaskName());
        }
        Intent intent = new Intent(this, MyScheduledReceiver.class);
        intent.putStringArrayListExtra(INTENT_LIST_FOR_NOTIFICATION, arrayList);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (currentTime >= targetTime) {
            mTargetCalendar.add(Calendar.DAY_OF_MONTH, 1);
            targetTime = mTargetCalendar.getTimeInMillis();
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}