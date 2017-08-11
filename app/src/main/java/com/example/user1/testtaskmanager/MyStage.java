package com.example.user1.testtaskmanager;


import java.io.Serializable;

public class MyStage implements Serializable{

    public final static int NOT_DONE = 0;
    public final static int DONE = 1;

    private int stageId;
    private int isStageDone;
    private String mStageName;

    public MyStage(){

    }

    public MyStage(int mStageNumber, String mStageName) {
        this.isStageDone = mStageNumber;
        this.mStageName = mStageName;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
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
