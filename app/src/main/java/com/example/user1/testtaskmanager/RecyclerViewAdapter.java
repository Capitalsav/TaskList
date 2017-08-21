package com.example.user1.testtaskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int MAX_PROGRESS_VALUE = 100;

    private List<MyTask> mList = null;

    public RecyclerViewAdapter(List<MyTask> arrayList) {
        mList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder newHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        newHolder = new RecyclerTaskHolder(view);
        return newHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyTask myTask = mList.get(position);
        RecyclerTaskHolder recyclerTaskHolder = (RecyclerTaskHolder) holder;
        recyclerTaskHolder.taskTitle.setText(myTask.getTaskName());
        recyclerTaskHolder.progressBarDate.setProgress(getDateProgressValue(myTask.getStartDate(), myTask.getEndDate()));
        recyclerTaskHolder.progressBarStage.setProgressWithAnimation(getStageProgressValue(myTask));
        recyclerTaskHolder.checkBox.setChecked(mList.get(position).isChecked());
    }

    private int getDateProgressValue(Calendar calendarStart, Calendar calendarEnd) {
        long firstDate = calendarStart.getTimeInMillis();
        long lastDate = calendarEnd.getTimeInMillis();
        Calendar currentDay = Calendar.getInstance();
        long currentDate = currentDay.getTimeInMillis();
        int firstDays = (int) (firstDate / (1000*60*60*24));
        int lastDays = (int) (lastDate / (1000*60*60*24));
        int curDays = (int) (currentDate / (1000*60*60*24));
        int allDays = lastDays - firstDays;
        if (allDays < 1) {
            return MAX_PROGRESS_VALUE;
        }
        else {
            return (100 * (curDays - firstDays) / (lastDays-firstDays));
        }
    }

    private int getStageProgressValue(MyTask myTask) {
        int doneCount = 0;
        int allCount = 0;
        ArrayList<MyStage> stageArrayList = myTask.getMyStages();
        for (MyStage stage : stageArrayList){
            if (stage.getIsStageDone() == MyStage.DONE){
                doneCount++;
            }
            allCount++;
        }
        try {
            return (100 * doneCount / allCount);
        }
        catch (ArithmeticException ex) {
            return MAX_PROGRESS_VALUE;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class RecyclerTaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView taskTitle = null;
        private ProgressBar progressBarDate = null;
        private CheckBox checkBox = null;
        private Context context;
        private CircularProgressBar progressBarStage;

        public RecyclerTaskHolder(View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.tv_title_of_recycler_item);
            progressBarDate = (ProgressBar) itemView.findViewById(R.id.horizontal_progress_id);
            progressBarStage = (CircularProgressBar) itemView.findViewById(R.id.circle_progress_id);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_id);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mList.get(getPosition()).setChecked(true);
                    }
                    else {
                        mList.get(getPosition()).setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            context = v.getContext();
            Intent intent = new Intent(context, ViewTaskActivity.class);
            intent.putExtra(ViewTaskActivity.INTENT_EXTRA_TASK, mList.get(getPosition()));
            ((Activity)context).startActivityForResult(intent, MainActivity.REQUEST_CODE_VIEW_TASK);
        }
    }
}
