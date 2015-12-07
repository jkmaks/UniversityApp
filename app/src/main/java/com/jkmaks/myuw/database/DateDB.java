package com.jkmaks.myuw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkmaks.myuw.domain.DateRow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 11/28/2014.
 */
public class DateDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DNAME, MySQLiteHelper.COLUMN_DATE};

    public DateDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DateRow createDate(DateRow the_dateRow) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_DNAME, the_dateRow.getName());
        values.put(MySQLiteHelper.COLUMN_DATE, the_dateRow.getDate());

        long insertId = database.insert(MySQLiteHelper.TABLE_DATES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DATES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DateRow a_date = cursorToDate(cursor);
        cursor.close();
        return a_date;
    }

    public void deleteDateRow(DateRow the_date) {
        long id = the_date.getId();
        System.out.println("Date deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_DATES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteDateRow(String the_name) {
        List<DateRow> dateRows = getAllDates();
        DateRow returned = new DateRow(0, "", "");
        for(DateRow d : dateRows)   {
            if(d.getName().equals(the_name))
                returned = d;
        }
        long id = returned.getId();
        System.out.println("Date deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_DATES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<DateRow> getAllDates() {
        List<DateRow> dateRows = new ArrayList<DateRow>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DATES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateRow aDateRow = cursorToDate(cursor);
            dateRows.add(aDateRow);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return dateRows;
    }

    public void deleteAllDates() {
        List<DateRow> dates = new ArrayList<DateRow>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DATES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateRow aDateRow = cursorToDate(cursor);
            deleteDateRow(aDateRow);
            dates.remove(aDateRow);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    private DateRow cursorToDate(Cursor cursor) {
        DateRow dateRow = null;
        dateRow = new DateRow(cursor.getLong(0), cursor.getString(1), cursor.getString(2));

        return dateRow;
    }
}
