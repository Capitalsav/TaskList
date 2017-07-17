package com.example.user1.testtaskmanager;


public class MyStage{

    private int mStageNumber;
    private String mStageName;

    public MyStage(){

    }

    public MyStage(int mStageNumber, String mStageName) {
        this.mStageNumber = mStageNumber;
        this.mStageName = mStageName;
    }

    public int getmStageNumber() {
        return mStageNumber;
    }

    public void setmStageNumber(int mStageNumber) {
        this.mStageNumber = mStageNumber;
    }

    public String getmStageName() {
        return mStageName;
    }

    public void setmStageName(String mStageName) {
        this.mStageName = mStageName;
    }
}
