package com.jkmaks.myuw.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Max on 12/10/2014.
 */
public class Major implements Serializable, Comparable {
    private long id;
    private String name;
    private String href;
    private String quarter;
    private ArrayList<Class> classes = new ArrayList<Class>();

    public Major(long the_id,String the_name, String the_href, ArrayList<Class> the_classes)  {
        id = the_id;
        name = the_name;
        href = the_href;
        for(Class c: the_classes)   {
            classes.add(c);
        }
    }

    public Major(long the_id, String the_name, String the_href, String the_quarter)  {
        id = the_id;
        name = the_name;
        href = the_href;
        quarter = the_quarter;

    }


    public Major(long the_id, String the_name, String the_href)  {
        id = the_id;
        name = the_name;
        href = the_href;

    }

    public ArrayList<Class> getClasses()    {
        ArrayList<Class> the_classes = new ArrayList<Class>();
        for(Class c: classes)   {
            the_classes.add(c);
        }
        return the_classes;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    public void setClasses(ArrayList<Class> the_classes)    {
        for(Class c : the_classes)
            classes.add(c);
    }

    public int compareTo(Object anotherClass) throws ClassCastException {
        if (!(anotherClass instanceof Major)) {
            throw new ClassCastException("A class object expected");
        }
        else {
            String other_name = ((Major) anotherClass).getName();
            return this.getName().compareTo(other_name);
        }

    }

    public String getQuarter()  {
        return quarter;
    }

    public void setQuarter(String the_quarter)    {
        quarter = the_quarter;
    }
}
