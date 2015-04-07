package com.jkmaks.Types;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by anyonym on 11/9/2014.
 */
public class Class implements Serializable, Comparable {

    public enum SortingType {Name, Professor, Time};
    private SortingType order = SortingType.Name;

    private long id;
    private String name;
    private String time;
    private String room;
    private String quarter;
    private String professor;
    private String href;
    private String status;
    private String major;
    private String campus;

    public Class(long the_id, String the_name, String the_time, String the_room,
                 String the_quarter, String the_professor, String the_href, String the_status)  {
        id = the_id;
        name = the_name;
        time = the_time;
        room = the_room;
        campus = the_quarter;
        professor = the_professor;
        href = the_href;
        status = the_status;
    }
    @Override
    public String toString()    {
        return "name of class is " + name + " time is " + time + "  room is  " + room +
                " professor " + professor + " a href " + href +
                " status " + status + " \n";
    }

    public long getId()    {
        return id;
    }

    public void setId(long the_id)    {
        id = the_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String the_name)  {
        name = the_name;
    }

    public String getQuarter()  {
        return quarter;
    }

    public void setQuater(String the_quarter) {
        quarter = the_quarter;
    }

    public String getProfessor()    {
        return professor;
    }

    public String getRoom() {
        return room;
    }

    public String getTime() {
        return time;
    }

    public String getHref() {
        return href;
    }

    public String getMajor()    {
        return major;
    }

    public void setMajor(String the_major)  {
        major = the_major;
    }

    public void setOrder(SortingType the_order) {
        order = the_order;
    }

    public int compareTo(Object anotherClass) throws ClassCastException {
        if (!(anotherClass instanceof Class))
            throw new ClassCastException("A class object expected");
        if(order == SortingType.Professor) {
            String other_name = ((Class) anotherClass).getProfessor();
            return this.getProfessor().compareTo(other_name);
        }
        if(order == SortingType.Time) {
            String other_time = ((Class) anotherClass).getTime();
            Log.d("Time 1 ", "" + other_time);
            Log.d("Time 2 ", "" + getTime());
            int time1 = Integer.valueOf(other_time.split("\n")[1]);

            //that is why it has to be so complicated
            //I suggested that class can't start from 10 pm to 6 am
            String stime1 = String.valueOf(time1);
            if(stime1.contains("P") || stime1.startsWith("1") || stime1.startsWith("2") ||
                    stime1.startsWith("3") || stime1.startsWith("4") || stime1.startsWith("5") ||
                    stime1.startsWith("6"))    {
                time1 = time1 + 1200;
            }
            int time2 = Integer.valueOf(getTime().split("\n")[1]);
            String stime2 = String.valueOf(time2);
            if(time.contains("P") ||
                    stime2.startsWith("1") && !stime2.startsWith("10") && !stime2.startsWith("11")  && !stime2.startsWith("12") ||
                    stime2.startsWith("2") ||
                    stime2.startsWith("3") || stime2.startsWith("4") || stime2.startsWith("5") ||
                    stime2.startsWith("6"))    {
                time2 = time2 + 1200;
            }
            return time2 - time1;
        }
        else    {
            String other_name = ((Class) anotherClass).getName();
            return this.getName().compareTo(other_name);
        }
    }

    public void setHref(String the_professor)  {
        href = the_professor;
    }
    public void setCampus(String the_campus)    {
        campus = the_campus;
    }

    public String getCampus()   {
        return campus;
    }

    public String getStatus()   {
        return status;
    }
}
