package com.jkmaks.myuw.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Max on 11/28/2014.
 *
 * This class will contain all majors and information in it.
 */
public class Campus implements Serializable {
    String name;
    String href;
    ArrayList<Major> majors = new ArrayList<Major>();
    Map<String, String> buildings = new HashMap<String, String>();

    public Campus(String the_name, String the_href, ArrayList<Major> the_majors, HashMap<String, String> the_buildings)   {

        name = the_name;
        href = the_href;
        for(Major m : the_majors)   {
            majors.add(m);
        }
        buildings = the_buildings;

    }


    public Campus(String the_name, String the_href)   {

        name = the_name;
        href = the_href;
    }

    public ArrayList<Major> getMajors()    {
        ArrayList<Major> the_majors = new ArrayList<Major>();
        for(Major m : majors)   {
            the_majors.add(m);
        }
        return the_majors;
    }

    public void clearMajors() {
        majors.clear();
    }
    public Map<String, String> getBuildings()   {
        return buildings;
    }

    public String getHref() {
        return href;
    }

    public void setMajors(ArrayList<Major> the_majors)  {
        for(Major m : the_majors)   {
            majors.add(m);
        }
    }

    public ArrayList<String> getMajorNames() {
        ArrayList<String> the_majors = new ArrayList<String>();
        for(Major m : majors)   {
            the_majors.add(m.getName());
        }
        return the_majors;
    }

    public String getName() {
        return name;
    }

}
