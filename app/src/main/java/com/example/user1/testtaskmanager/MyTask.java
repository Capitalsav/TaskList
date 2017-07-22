package com.example.user1.testtaskmanager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MyTask implements Serializable{

    private String mTaskName;
    private ArrayList<MyStage> myStages;
    private Calendar mStartDate;
    private Calendar mEndDate;

    public MyTask(){

    }

    public MyTask(String mTaskName, ArrayList<MyStage> myStages, Calendar mStartDate, Calendar mEndDate) {
        this.mTaskName = mTaskName;
        this.myStages = myStages;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
    }

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public ArrayList<MyStage> getMyStages() {
        return myStages;
    }

    public void setMyStages(ArrayList<MyStage> myStages) {
        this.myStages = myStages;
    }

    public Calendar getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Calendar mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Calendar getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(Calendar mEndDate) {
        this.mEndDate = mEndDate;
    }
}
