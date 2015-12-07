package com.jkmaks.myuw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkmaks.myuw.domain.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 11/28/2014.
 */
public class EventDB {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TIME,
            MySQLiteHelper.COLUMN_ROOM,  MySQLiteHelper.COLUMN_HREF,
            MySQLiteHelper.COLUMN_CAMPUS};

    public EventDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Event createEvent(Event the_event) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_NAME, the_event.getName());
        values.put(MySQLiteHelper.COLUMN_TIME, the_event.getTime());
        values.put(MySQLiteHelper.COLUMN_ROOM, the_event.getLocation());
        values.put(MySQLiteHelper.COLUMN_HREF, the_event.getHref());
        values.put(MySQLiteHelper.COLUMN_CAMPUS, the_event.getCampus());
        long insertId = database.insert(MySQLiteHelper.TABLE_EVENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Event a_class = cursorToClass(cursor);
        cursor.close();
        return a_class;
    }

    public void deleteEvent(Event the_class) {
        long id = the_class.getId();
        System.out.println("Event deleted with id and name " + id + ", " + the_class.getName());
        database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteEvents(String name) {

        List<Event> events = getAllEvents();
        for(Event e : events)   {
            if(e.getCampus().equals(name)) {
                database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.COLUMN_ID
                        + " = " + e.getId(), null);
                //events.remove(e);
                System.out.println("Event deleted with name " + e.getName());
            }

        }
        events.clear();
    }

    public void deleteShallowEvent(String name) {

        List<Event> events = getAllEvents();
        for(Event e : events)   {
            if(e.getName().equals(name)) {
                database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.COLUMN_ID
                        + " = " + e.getId(), null);
                //events.remove(e);
                System.out.println("Event deleted with name " + e.getName());
            }

        }
        events.clear();
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToClass(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public void deleteAllEvents() {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event aClass = cursorToClass(cursor);
            events.remove(aClass);
            deleteEvent(aClass);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    private Event cursorToClass(Cursor cursor) {
        Event event = new Event(cursor.getLong(0), cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));

        return event;
    }
}
