package com.example.user1.testtaskmanager;


public class MyStage{

    public final static int NOT_DONE = 0;
    public final static int DONE = 1;

    private int isStageDone;
    private String mStageName;

    public MyStage(){

    }

    public MyStage(int mStageNumber, String mStageName) {
        this.isStageDone = mStageNumber;
        this.mStageName = mStageName;
    }

    public int getIsStageDone() {
        return isStageDone;
    }

    public void setIsStageDone(int mStageNumber) {
        this.isStageDone = mStageNumber;
    }

    public String getmStageName() {
        return mStageName;
    }

    public void setmStageName(String mStageName) {
        this.mStageName = mStageName;
    }
}
