package com.example.user1.testtaskmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewStageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MyStage> mStageList = null;

    public RecyclerViewStageAdapter(ArrayList<MyStage> list) {mStageList = list;}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder newHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_stage_item, parent, false);
        newHolder = new RecyclerStageHolder(view);
        return newHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyStage myStage = mStageList.get(position);
        RecyclerStageHolder recyclerStageHolder = (RecyclerStageHolder) holder;
        recyclerStageHolder.mStageNumber.setText(String.valueOf(position + 1));
        recyclerStageHolder.mStageTitle.setText(myStage.getmStageName());
    }

    @Override
    public int getItemCount() {
        return mStageList.size();
    }

    class RecyclerStageHolder extends RecyclerView.ViewHolder {

        TextView mStageNumber = null;
        TextView mStageTitle = null;
        Button mStageDeleteButton = null;

        public  RecyclerStageHolder(View itemView) {
            super(itemView);
            mStageNumber = (TextView) itemView.findViewById(R.id.tv_ordinal_stage_number);
            mStageTitle = (TextView) itemView.findViewById(R.id.tv_stage_name);
            mStageDeleteButton = (Button) itemView.findViewById(R.id.btn_delete_stage);
        }

        /*TODO delete button onclick*/
    }
}