package org.capitalsav.user1.tasklist;


import java.io.Serializable;

public class MyStage implements Serializable{

    public final static int NOT_DONE = 0;
    public final static int DONE = 1;

    private int mStageId;
    private int mIsStageDone;
    private String mStageName;

    public MyStage(){

    }


    public int getStageId() {
        return mStageId;
    }

    public void setStageId(int mStageId) {
        this.mStageId = mStageId;
    }

    public int getIsStageDone() {
        return mIsStageDone;
    }

    public void setIsStageDone(int mStageNumber) {
        this.mIsStageDone = mStageNumber;
    }

    public String getStageName() {
        return mStageName;
    }

    public void setStageName(String mStageName) {
        this.mStageName = mStageName;
    }
}
