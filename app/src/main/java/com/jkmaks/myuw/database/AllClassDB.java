package com.jkmaks.myuw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jkmaks.myuw.domain.Class;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 12/8/2014.
 */
public class AllClassDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TIME,
            MySQLiteHelper.COLUMN_ROOM, MySQLiteHelper.COLUMN_HREF,
            MySQLiteHelper.COLUMN_PROFESSOR, MySQLiteHelper.COLUMN_MAJOR,
            MySQLiteHelper.COLUMN_CAMPUS, MySQLiteHelper.COLUMN_STATUS};

    public AllClassDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Class createClass(Class the_class) {
        ContentValues values = new ContentValues();
        //Log.d("We put this class ", the_class.toString());

        values.put(MySQLiteHelper.COLUMN_NAME, the_class.getName());
        values.put(MySQLiteHelper.COLUMN_TIME, the_class.getTime());
        values.put(MySQLiteHelper.COLUMN_ROOM, the_class.getRoom());
        values.put(MySQLiteHelper.COLUMN_HREF, the_class.getHref());
        values.put(MySQLiteHelper.COLUMN_PROFESSOR, the_class.getProfessor());
        values.put(MySQLiteHelper.COLUMN_MAJOR, the_class.getMajor());
        values.put(MySQLiteHelper.COLUMN_CAMPUS, the_class.getCampus());
        values.put(MySQLiteHelper.COLUMN_STATUS, the_class.getCampus());
        long insertId = database.insert(MySQLiteHelper.TABLE_ALL_CLASSES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ALL_CLASSES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Class a_class = cursorToClass(cursor);
        cursor.close();
        return a_class;
    }

    public void deleteClass(Class the_class) {
        long id = the_class.getId();
        System.out.println("Class deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ALL_CLASSES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<Class>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ALL_CLASSES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Class aClass = cursorToClass(cursor);
            classes.add(aClass);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return classes;
    }

    public List<Class> getMajorCampusClasses(String major_name, String campus_name) {
        List<Class> classes = new ArrayList<Class>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ALL_CLASSES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Class aClass = cursorToClass(cursor);
            if(aClass.getMajor().equals(major_name) && aClass.getCampus().equals(campus_name))
                classes.add(aClass);
            cursor.moveToNext();

        }
        // make sure to close the cursor
        cursor.close();
        return classes;
    }

    private Class cursorToClass(Cursor cursor) {
        Class aClass = new Class(cursor.getLong(0),
                cursor.getString(1), //name
                cursor.getString(2), //time
                cursor.getString(3), //room
                cursor.getString(7), //quarter and major
                cursor.getString(5), //professor
                cursor.getString(4), //href
                "" //status
        );
        Log.d("Class status is", cursor.getString(8));
/**
 private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
 MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TIME,
 MySQLiteHelper.COLUMN_ROOM, MySQLiteHelper.COLUMN_HREF,
 MySQLiteHelper.COLUMN_PROFESSOR,
 MySQLiteHelper.COLUMN_MAJOR, MySQLiteHelper.COLUMN_CAMPUS};
 **/






        aClass.setMajor(cursor.getString(6));

        return aClass;
    }
}
