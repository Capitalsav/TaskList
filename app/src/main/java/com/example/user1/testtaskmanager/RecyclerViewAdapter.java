package com.example.user1.testtaskmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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
        recyclerTaskHolder.mTaskTitle.setText(myTask.getmTaskName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecyclerTaskHolder extends RecyclerView.ViewHolder {

        TextView mTaskTitle = null;

        public RecyclerTaskHolder(View itemView) {
            super(itemView);
            mTaskTitle = (TextView) itemView.findViewById(R.id.tv_title_of_recycler_item);
        }
    }
}
