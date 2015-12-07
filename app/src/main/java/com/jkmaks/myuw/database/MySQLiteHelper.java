package com.jkmaks.myuw.database;

/**
 * Created by Max on 11/9/2014.
 *
 * taken from vogella.com
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ALL_CLASSES = "all_classes";
    public static final String TABLE_MAJORS = "majors";
    public static final String TABLE_CLASSES = "classes";
    public static final String TABLE_DATES = "dates";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_QUARTERS = "quarters";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "class_name";
    public static final String COLUMN_MAJOR = "class_major";
    public static final String COLUMN_TIME = "class_time";
    public static final String COLUMN_ROOM = "class_room";
    public static final String COLUMN_HREF = "class_href";
    public static final String COLUMN_CAMPUS = "class_campus";
    public static final String COLUMN_PROFESSOR = "class_professor";
    public static final String COLUMN_STATUS = "class_status";

    public static final String COLUMN_DATE = "date_date";
    public static final String COLUMN_DNAME = "date_name";

    private static final String DATABASE_NAME = "classes.db";
    private static final int DATABASE_VERSION = 1;

    // Command for basic database for classes that were already added
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CLASSES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_TIME + " text not null, " + COLUMN_ROOM + " text not null, "
            + COLUMN_HREF + " text not null); ";

    // Command for date table that will check if dates were changed and database should be updated
    private static final String DATETABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DATES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DNAME
            + " text not null, " + COLUMN_DATE + " text not null); ";

    // Command for all classes table to create
    private static final String ALLCLASSES_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ALL_CLASSES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_MAJOR
            + " text not null, " + COLUMN_TIME + " text not null, " + COLUMN_ROOM + " text not null, "
            + COLUMN_HREF + " text not null, " + COLUMN_CAMPUS + " text not null, " +
            COLUMN_STATUS + " text not null, " + COLUMN_PROFESSOR + " text not null); ";

    private static final String EVENTTABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EVENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_TIME + " text not null, " + COLUMN_ROOM + " text not null, "
            + COLUMN_HREF + " text not null, " + COLUMN_CAMPUS + " text not null); ";

    private static final String QUARTERTABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_QUARTERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_HREF + " text not null); ";

    private static final String MAJORTABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MAJORS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_HREF + " text not null, "
            + COLUMN_CAMPUS + " text not null); ";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATETABLE_CREATE);
        database.execSQL(EVENTTABLE_CREATE);
        database.execSQL(QUARTERTABLE_CREATE);
        database.execSQL(ALLCLASSES_TABLE_CREATE);
        database.execSQL(MAJORTABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUARTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAJORS);
        onCreate(db);
    }

} 