package com.jkmaks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkmaks.Types.Major;
import com.jkmaks.Types.Quarter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Max on 11/30/2014.
 */
public class MajorDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_HREF, MySQLiteHelper.COLUMN_CAMPUS};

    public MajorDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Major createMajor(Major the_major) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_NAME, the_major.getName());
        values.put(MySQLiteHelper.COLUMN_HREF, the_major.getHref());
        values.put(MySQLiteHelper.COLUMN_CAMPUS, the_major.getQuarter());

        long insertId = database.insert(MySQLiteHelper.TABLE_MAJORS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAJORS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Major major = cursorToDate(cursor);
        cursor.close();
        return major;
    }

    public void deleteMajorRow(Quarter quarter) {
        long id = quarter.getId();
        System.out.println("Date deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_MAJORS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Major> getAllMajors(String quarter) {
        List<Major> dateRows = new ArrayList<Major>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAJORS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Major major = cursorToDate(cursor);
            if(major.getQuarter().equals(quarter))
                dateRows.add(major);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return dateRows;
    }

    public void deleteAllMajors() {
        List<Major> majors = new ArrayList<Major>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAJORS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Major major = cursorToDate(cursor);
            majors.remove(major);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    private Major cursorToDate(Cursor cursor) {
        Major major = new Major(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return major;
    }
}
