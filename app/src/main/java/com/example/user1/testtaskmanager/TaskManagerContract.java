package com.example.user1.testtaskmanager;


import android.provider.BaseColumns;

public final class TaskManagerContract {

    private TaskManagerContract() {

    }

    public static final class TaskInDb implements BaseColumns {
        public final static String TABLE_NAME = "tasks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TASK_NAME = "name";
        public final static String COLUMN_TASK_START_DATE = "start_date";
        public final static String COLUMN_TASK_END_DATE = "end_date";
    }

    public static final class StageInDb implements BaseColumns {
        public final static String TABLE_NAME = "stages";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_STAGE_NAME = "name";
        public final static String COLUMN_STAGE_IS_DONE = "is_done";
        public final static String COLUMN_STAGE_TASK_ID = "task_id";
    }
}
