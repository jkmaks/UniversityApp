package com.jkmaks.Types;

/**
 * Created by Max on 12/1/2014.
 *
 */
public class Quarter {
    long id;
    String name;
    String href;
    String quarter = "";

    public Quarter(long the_id, String the_name, String the_href)  {
        id = the_id;
        name = the_name;
        href = the_href;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    public String getQuarter()  {
        return quarter;
    }
}
