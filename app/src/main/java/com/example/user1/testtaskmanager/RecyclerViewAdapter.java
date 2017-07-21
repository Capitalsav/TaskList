package com.example.user1.testtaskmanager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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
        recyclerTaskHolder.taskTitle.setText(myTask.getmTaskName());
        recyclerTaskHolder.progressBarDate.setProgress(getDateProgressValue(myTask.getmStartDate(), myTask.getmEndDate()));
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecyclerTaskHolder extends RecyclerView.ViewHolder {

        TextView taskTitle = null;
        ProgressBar progressBarDate = null;

        public RecyclerTaskHolder(View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.tv_title_of_recycler_item);
            progressBarDate = (ProgressBar) itemView.findViewById(R.id.horizontal_progress_id);
        }
    }
}
