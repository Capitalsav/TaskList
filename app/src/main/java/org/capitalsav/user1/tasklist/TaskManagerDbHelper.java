package org.capitalsav.user1.tasklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TaskManagerDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_manager_db";

    private static final int DATABASE_VERSION = 1;

    public TaskManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + TaskManagerContract.TaskInDb.TABLE_NAME
                + " (" + TaskManagerContract.TaskInDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskManagerContract.TaskInDb.COLUMN_TASK_NAME + " TEXT NOT NULL, "
                + TaskManagerContract.TaskInDb.COLUMN_TASK_START_DATE + " TEXT NOT NULL, "
                + TaskManagerContract.TaskInDb.COLUMN_TASK_END_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TASKS_TABLE);

        String SQL_CREATE_STAGE_TABLE = "CREATE TABLE " + TaskManagerContract.StageInDb.TABLE_NAME
                + " (" + TaskManagerContract.StageInDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskManagerContract.StageInDb.COLUMN_STAGE_IS_DONE + " INTEGER NOT NULL, "
                + TaskManagerContract.StageInDb.COLUMN_STAGE_NAME + " TEXT NOT NULL, "
                + TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + TaskManagerContract.StageInDb.COLUMN_STAGE_TASK_ID + ") "
                + "REFERENCES " + TaskManagerContract.TaskInDb.TABLE_NAME + "("
                + TaskManagerContract.TaskInDb._ID + ") ON DELETE CASCADE);";

        db.execSQL(SQL_CREATE_STAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
