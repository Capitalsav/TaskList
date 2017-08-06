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
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity
        implements EditStageDialogFragment.EditStageDialogListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyStage> myStageList;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private String mStartDateString;
    private String mEndDateString;
    private Calendar currentDate;
    private TaskManagerDbHelper taskManagerDbHelper;
    private EditText editText;
    private TextView textViewStartDate;
    private TextView textViewEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        myStageList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_stages);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewStageAdapter(myStageList);
        mRecyclerView.setAdapter(mAdapter);
        currentDate = Calendar.getInstance();
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        taskManagerDbHelper = new TaskManagerDbHelper(this);
        editText = (EditText) findViewById(R.id.edit_task_name);
        textViewStartDate = (TextView) findViewById(R.id.tv_start_date);
        textViewEndDate = (TextView) findViewById(R.id.tv_end_date);
    }

    /*TODO refactor this method*/
    public void onClickSaveTask(View view) {
        if (editText.getText().toString().equals("")){
            Toast.makeText(this, "Please input task title", Toast.LENGTH_SHORT).show();
        }
        else if (myStageList.size() == 0) {
            Toast.makeText(this, "Please create 1 stage", Toast.LENGTH_SHORT).show();
        }
        else if (mStartDateString == null || mStartDateString.equals("")) {
            Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
        }
        else if (mEndDateString == null || mEndDateString.equals("")){
            Toast.makeText(this, "Please select end date", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase database = taskManagerDbHelper.getWritableDatabase();
            try {

                ContentValues contentValues = new ContentValues();
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_NAME, editText.getText().toString());
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE, mStartDateString);
                contentValues.put(TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE, mEndDateString);
                long newRowId = database.insert(TaskManagerContract.TaskInDb.TABLE_NAME, null, contentValues);
                if (newRowId != -1) {
                    for (int i = 0; i < myStageList.size(); i++) {
                        MyStage stage = myStageList.get(i);
                        ContentValues contentValuesStage = new ContentValues();
                        contentValuesStage.put(TaskManagerContract.StageInDb.COLUMN_STAGE_NAME, stage.getmStageName());
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
                            /*TODO error message*/
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
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void onClickEndDate(View view) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, endDateListener,
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
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
                startDateCalendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                textViewStartDate.setText(format.format(startDateCalendar.getTime()));
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
                endDateCalendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                textViewEndDate.setText(format.format(endDateCalendar.getTime()));
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
        myStageList.add(myStage);
        mAdapter.notifyDataSetChanged();
    }

}
