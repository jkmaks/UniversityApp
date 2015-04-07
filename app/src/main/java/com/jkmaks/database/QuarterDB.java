package com.jkmaks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkmaks.Types.Quarter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Max on 11/30/2014.
 */
public class QuarterDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_HREF};

    public QuarterDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Quarter createQuarter(Quarter the_quarter) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_NAME, the_quarter.getName());
        values.put(MySQLiteHelper.COLUMN_HREF, the_quarter.getHref());

        long insertId = database.insert(MySQLiteHelper.TABLE_QUARTERS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUARTERS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Quarter quarter = cursorToDate(cursor);
        cursor.close();
        return quarter;
    }

    public void deleteQuarterRow(Quarter quarter) {
        long id = quarter.getId();
        System.out.println("Date deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_QUARTERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Quarter> getAllQuarters() {
        List<Quarter> dateRows = new ArrayList<Quarter>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUARTERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quarter quarter = cursorToDate(cursor);
            dateRows.add(quarter);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return dateRows;
    }

    public void deleteAllDates() {
        List<Quarter> quarters = new ArrayList<Quarter>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUARTERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quarter quarter = cursorToDate(cursor);
            quarters.remove(quarter);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    private Quarter cursorToDate(Cursor cursor) {
        Quarter quarter = new Quarter(cursor.getLong(0), cursor.getString(1), cursor.getString(2));

        return quarter;
    }
}
