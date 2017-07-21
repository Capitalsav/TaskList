package com.example.user1.testtaskmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity
        implements EditStageDialogFragment.EditStageDialogListener{

    private final static String TAG = "=================TAG";
    private final static int DIALOG_START_DATE = 1;
    private final static int DIALOG_END_DATE = 2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<MyStage> myStageList;

    private String mTaskTitle;
    private String mStartDateString;
    private String mEndDateString;
//    private ArrayList<String> mStagesTexts;

    private Calendar currentDate;

    private TaskManagerDbHelper taskManagerDbHelper;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        myStageList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stages);
        mRecyclerView.setHasFixedSize(true);
        /*user linear layout manager*/
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewStageAdapter(myStageList);
        mRecyclerView.setAdapter(mAdapter);
        currentDate = Calendar.getInstance();
        taskManagerDbHelper = new TaskManagerDbHelper(this);
        editText = (EditText) findViewById(R.id.edit_task_name);
    }

    public void onClickSaveTask(View view) {
        /*TODO return to previous activity and error handle*/
        Log.d(TAG, "00000000000000");
        SQLiteDatabase database = taskManagerDbHelper.getWritableDatabase();
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_NAME, editText.getText().toString());
            contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE, mStartDateString);
            contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE, mEndDateString);
            long newRowId = database.insert(TaskManagerContract.TaskInDb.TABLE_NAME, null, contentValues);
            if (newRowId != -1) {
                Log.d(TAG, String.valueOf(newRowId));
//                String selectQuery = "SELECT " + TaskManagerContract.TaskInDb._ID + " FROM " +
//                        TaskManagerContract.TaskInDb.TABLE_NAME +
                for (int i = 0; i < myStageList.size(); i++) {
                    MyStage stage = myStageList.get(i);
                    ContentValues contentValuesStage = new ContentValues();
                    contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_NAME, stage.getmStageName());
                    contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_NUMBER, i + 1);
                    contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID, newRowId);
                    try {
                        long newRowIdStage = database.insert(TaskManagerContract.StageInDb.TABLE_NAME, null, contentValuesStage);
                        if (newRowIdStage != -1) {
                            Log.d(TAG, String.valueOf(newRowIdStage));
                        }
                        else {
                            Log.d(TAG, String.valueOf(newRowIdStage));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Log.d(TAG, "row not inserted");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            database.close();
        }
    }

    public void onClickStartDate(View view) {
        new DatePickerDialog(CreateTaskActivity.this, startDateListener,
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickEndDate(View view) {
        new DatePickerDialog(CreateTaskActivity.this, endDateListener,
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            /*call StringBuilder once*/
            if (view.isShown()) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(month);
                builder.append("-");
                builder.append(dayOfMonth);
                mStartDateString = builder.toString();
            }
        }
    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            /*call StringBuilder once*/
            if (view.isShown()) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(month);
                builder.append("-");
                builder.append(dayOfMonth);
                mEndDateString = builder.toString();
            }
        }
    };

    public void onClickCreateStage(View view) {
        FragmentManager manager = getSupportFragmentManager();
        EditStageDialogFragment editStageDialogFragment = new EditStageDialogFragment();
        editStageDialogFragment.show(manager, "fragment_edit_stage");
    }

    @Override
    public void onFinishEditStageDialog(DialogFragment dialogFragment) {
        Dialog dialog = dialogFragment.getDialog();
        EditText editText = (EditText) dialog.findViewById(R.id.edit_text_stage_name);
        MyStage myStage = new MyStage();
        myStage.setmStageName(editText.getText().toString());
//        myStage.setmStageNumber(myStageList.size() + 1);
        Log.d(TAG, String.valueOf(myStage.getmStageNumber()));
        myStageList.add(myStage);
        Log.d(TAG, "test");
        mAdapter.notifyDataSetChanged();
    }

}