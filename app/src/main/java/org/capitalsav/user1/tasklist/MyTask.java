package org.capitalsav.user1.tasklist;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class MyTask implements Serializable{

    private int mTaskId;
    private String mTaskName;
    private ArrayList<MyStage> myStages;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private boolean isChecked;


    public MyTask(){

    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public ArrayList<MyStage> getMyStages() {
        return myStages;
    }

    public void setMyStages(ArrayList<MyStage> myStages) {
        this.myStages = myStages;
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Calendar getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Calendar mEndDate) {
        this.mEndDate = mEndDate;
    }

    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int mtaskId) {
        this.mTaskId = mtaskId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
