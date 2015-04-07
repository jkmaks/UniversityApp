package com.jkmaks.Types;

/**
 * Created by Max on 11/28/2014.
 */
public class DateRow {
    long id;
    String name;
    String date;

    public DateRow(long the_id, String the_name, String the_date)  {
        id = the_id;
        name = the_name;
        date = the_date;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
