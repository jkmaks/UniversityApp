package com.jkmaks.Types;

import java.io.Serializable;

/**
 * Created by anyonym on 11/15/2014.
 */
public class Event implements Serializable {
    String name;
    String href;
    String location;
    String time;
    String campus;
    long id;

    public Event(long the_id, String the_name, String the_href, String the_room, String the_time, String the_campus)    {
        id = the_id;
        name = the_name;
        href = the_href;
        location = the_room;
        time = the_time;
        campus = the_campus;
    }

    public String getName()    {
        return name;
    }

    public String getHref() {
        return href;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String toString()    {
        return name + " " + location;
    }

    public long getId() {
        return id;
    }

    public String getCampus()   {
        return campus;
    }

    public void setCampus(String the_campus) {
        campus = the_campus;
    }
}
