package com.jkmaks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkmaks.Types.Class;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 11/9/2014.
 */
public class AddedClassDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TIME,
            MySQLiteHelper.COLUMN_ROOM,  MySQLiteHelper.COLUMN_HREF};

    public AddedClassDB(Context context) {
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
        values.put(MySQLiteHelper.COLUMN_NAME, the_class.getName() + "\n by " + the_class.getProfessor());
        values.put(MySQLiteHelper.COLUMN_TIME, the_class.getTime());
        values.put(MySQLiteHelper.COLUMN_ROOM, the_class.getRoom());
        values.put(MySQLiteHelper.COLUMN_HREF, the_class.getHref());
        long insertId = database.insert(MySQLiteHelper.TABLE_CLASSES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CLASSES,
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
        database.delete(MySQLiteHelper.TABLE_CLASSES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<Class>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CLASSES,
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

    private Class cursorToClass(Cursor cursor) {
        Class aClass = new Class(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), "", cursor.getString(4), cursor.getString(4), "");

        return aClass;
    }
}
