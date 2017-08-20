package com.example.user1.testtaskmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity
        implements EditStageDialogFragment.EditStageDialogListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyStage> mMyStageList;
    private Calendar mStartDateCalendar;
    private Calendar mEndDateCalendar;
    private String mStartDateString;
    private String mEndDateString;
    private Calendar mCurrentDate;
    private TaskManagerDbHelper mTaskManagerDbHelper;
    private EditText mEditText;
    private Button mButtonStartDate;
    private Button mButtonEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mMyStageList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewStageAdapter(mMyStageList);
        mRecyclerView.setAdapter(mAdapter);
        mCurrentDate = Calendar.getInstance();
        mStartDateCalendar = Calendar.getInstance();
        mEndDateCalendar = Calendar.getInstance();
        mTaskManagerDbHelper = new TaskManagerDbHelper(this);
        mEditText = (EditText) findViewById(R.id.edit_task_name);
        mButtonStartDate = (Button) findViewById(R.id.btn_start_date_dialog);
        mButtonEndDate = (Button) findViewById(R.id.btn_end_date_dialog);
    }

    /*TODO refactor this method*/
    public void onClickSaveTask(View view) {
        if (mEditText.getText().toString().equals("")){
            Toast.makeText(this, R.string.empty_task_name, Toast.LENGTH_SHORT).show();
        }
        else if (mMyStageList.size() == 0) {
            Toast.makeText(this, R.string.empty_stage, Toast.LENGTH_SHORT).show();
        }
        else if (mStartDateString == null || mStartDateString.equals("")) {
            Toast.makeText(this, R.string.empty_start_date, Toast.LENGTH_SHORT).show();
        }
        else if (mEndDateString == null || mEndDateString.equals("")){
            Toast.makeText(this, R.string.empty_end_date, Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase database = mTaskManagerDbHelper.getWritableDatabase();
            try {

                ContentValues contentValues = new ContentValues();
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_NAME, mEditText.getText().toString());
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE, mStartDateString);
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE, mEndDateString);
                long newRowId = database.insert(TaskManagerContract.TaskInDb.TABLE_NAME, null, contentValues);
                if (newRowId != -1) {
                    for (int i = 0; i < mMyStageList.size(); i++) {
                        MyStage stage = mMyStageList.get(i);
                        ContentValues contentValuesStage = new ContentValues();
                        contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_NAME, stage.getStageName());
                        contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_IS_DONE, MyStage.NOT_DONE);
                        contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID, newRowId);
                        try {
                            long newRowIdStage = database.insert(TaskManagerContract.StageInDb.TABLE_NAME, null, contentValuesStage);
                            if (newRowIdStage != -1) {
                                /*TODO success result*/
                            }
                            else {
                                /*TODO error message*/
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    /*TODO show error toast*/
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                database.close();
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void onClickStartDate(View view) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, startDateListener,
                mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void onClickEndDate(View view) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, endDateListener,
                mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (view.isShown()) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(month);
                builder.append("-");
                builder.append(dayOfMonth);
                mStartDateString = builder.toString();
                mStartDateCalendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mButtonStartDate.setText(format.format(mStartDateCalendar.getTime()));
            }
        }
    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (view.isShown()) {
                StringBuilder builder = new StringBuilder();
                builder.append(year);
                builder.append("-");
                builder.append(month);
                builder.append("-");
                builder.append(dayOfMonth);
                mEndDateString = builder.toString();
                mEndDateCalendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mButtonEndDate.setText(format.format(mEndDateCalendar.getTime()));
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
        myStage.setStageName(editText.getText().toString());
        mMyStageList.add(myStage);
        mAdapter.notifyDataSetChanged();
    }

}
