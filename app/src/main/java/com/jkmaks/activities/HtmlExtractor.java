package com.jkmaks.activities;

import android.app.Activity;
import android.text.format.Time;
import android.util.Log;

import com.jkmaks.Types.*;
import com.jkmaks.Types.Class;
import com.jkmaks.database.DateDB;
import com.jkmaks.database.QuarterDB;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Max on 11/15/2014.
 */
public class HtmlExtractor {


    public ArrayList<Event> extractEvent(String html) {
        Time now = new Time();

        now.setToNow();

        String date =  "" + (new DateFormatSymbols().getMonths()[now.month]);
        date = date.substring(0, 1).toUpperCase() + date.substring(1);
        date = date.substring(0, 3) + ". " + now.monthDay + ", " + now.year;
        ArrayList<Event> events = new ArrayList<Event>();

        String pat = "<item>(.*?)" + date + "(.*?)</item>";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(html);

        String pat2 = "";
        Pattern pattern2;
        Matcher matcher2;
        Event event;
        while (matcher.find()) {

            String item = matcher.group();
            if (!item.contains("Ongoing")) {
                Log.d("Item is ", item);
                pat2 = "<title>(.*?)</title>";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                matcher2.find();
                String name = matcher2.group();
                name = name.substring(7, name.length() - 8);

                pat2 = date + "(.*?)PDT";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                String time = " On-\ngoing \n \n   ";
                if (matcher2.find()) {
                    time = matcher2.group();
                    time = time.replaceAll("nbsp;", "\n");
                    time = time.replaceAll("ndash;", "-");
                    time = time.replaceAll("&amp;", "");
                    time = time.replaceAll(date + ", ", "");
                    time = time.replace("PDT", "");
                }

                pat2 = "Campus location(.*?)/a";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                String room = "";
                if (matcher2.find()) {
                    room = matcher2.group();
                    while (room.contains("&gt;")) {
                        room = room.substring(1);
                    }
                    room = room.substring(3, room.length() - 6);
                }

                pat2 = "title=(.*?)&gt";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                String href = "";
                if (matcher2.find()) {
                    href = matcher2.group();
                    href = href.substring(7, href.length() - 4);
                }

                if ("".equals(room)) {
                    pat2 = "<description>(.*?)&lt;br/&gt;";
                    pattern2 = Pattern.compile(pat2);
                    matcher2 = pattern2.matcher(item);

                    if (matcher2.find()) {
                        room = matcher2.group();
                        room = room.replaceAll("<description>", "");
                        room = room.replaceAll("&[^;]+;", "");
                        room = room.replaceAll("#[^;]+;", "");
                    }
                    if(room.contains("2015") || room.contains("2016") || room.contains("2017")
                            ||room.contains("2018"))    {
                        room = "Room was not specified";
                    }
                }

                name = name.replaceAll("&.+;", "");

                Log.d("Name is ", name);
                Log.d("Time is ", time);
                Log.d("Room is ", room);
                Log.d("Href is ", href);
                event = new Event(0, name, href, room, time, "");
                events.add(event);
            }
            Log.d("Size of events is ", "" + events.size());

        }
        return events;
    }

    public HashMap<String, String> extractQuarters(String html, Activity activity) {
        HashMap<String, String> map = new HashMap<String, String>();
        DateDB datasourceDates = new DateDB(activity.getBaseContext());
        QuarterDB datasourceQuarters = new QuarterDB(activity.getBaseContext());
        try {
            datasourceDates.open();
            datasourceQuarters.open();
        } catch (SQLException s) {
            Log.e("Exception! ", "something went wrong with sql");
        }

        //datasource.createClass("CSS305 MW 11:00 1:00 JOY 103");
        List<DateRow> values = datasourceDates.getAllDates();
        String patDate = "Modified:(.*?)<";
        Pattern patternDate = Pattern.compile(patDate);
        Matcher matcherDate = patternDate.matcher(html);
        String date = "";
        if (matcherDate.find()) {
            date = matcherDate.group();
        }
        boolean found = false;
        for (DateRow d : values) {
            if (d.getDate().equals(date) && d.getName().equals("Quarters")) {
                found = true;
            }
        }

        if (!found) {
            datasourceDates.createDate(new DateRow(0, "Quarters", date));
            ArrayList<String> quarters = new ArrayList<String>();
            ArrayList<String> hrefs = new ArrayList<String>();
            String pat = "<blockquote>(.*?)</blockquote>";
            Pattern pattern = Pattern.compile(pat);
            Matcher matcher = pattern.matcher(html);

            String pat2 = "";
            Pattern pattern2;
            Matcher matcher2;

            while (matcher.find()) {
                String item = matcher.group();

                pat2 = "<a href=\"(.*?)\">";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                while (matcher2.find()) {
                    String href = matcher2.group();
                    href = href.replaceAll("<a href=\"", "");
                    href = href.replaceAll("\">", "");
                    hrefs.add(href);
                }
                pat2 = "\">(.*?)</a>";
                pattern2 = Pattern.compile(pat2);
                matcher2 = pattern2.matcher(item);
                while (matcher2.find()) {
                    String name = matcher2.group();
                    name = name.replaceAll("\">", "");
                    name = name.replaceAll("</a>", "");
                    quarters.add(name);
                }
            }
            Log.d("quarters are", "" + quarters);
            Log.d("hrefs are", "" + hrefs);

            for (int i = 0; i < quarters.size(); i++) {
                map.put(quarters.get(i), hrefs.get(i));
                datasourceQuarters.createQuarter(new Quarter(0, quarters.get(i), hrefs.get(i)));
            }
        } else {
            List<Quarter> quarters = datasourceQuarters.getAllQuarters();
            for (Quarter quarter : quarters) {
                map.put(quarter.getName(), quarter.getHref());
            }
            Log.d("Got quarters offline", "yes");
        }
        return map;
    }

    public HashMap<String, String> extractMajors(String html, Activity activity) {
        HashMap<String, String> map = new HashMap<String, String>();


        ArrayList<String> majors = new ArrayList<String>();
        ArrayList<String> hrefs = new ArrayList<String>();
        String pat = "<(LI|li)>(.*?).html";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find())
            hrefs.add(matcher.group());

        pat = "html>(.*?)</(a|A)>";
        pattern = Pattern.compile(pat);
        matcher = pattern.matcher(html);

        while (matcher.find())
            majors.add(matcher.group());

        for (int i = 0; i < majors.size(); i++)
            map.put(majors.get(i).replaceAll("html>", "").replaceAll("</(a|A)>", ""),
                    hrefs.get(i).replaceAll("<(LI|li)>", "").replaceAll("(?i)<a href=", ""));


        return map;
    }

    public ArrayList<com.jkmaks.Types.Class> extractClasses(String html) {
        ArrayList<Class> classes = new ArrayList<Class>();
        String pat = "A NAME=(.*?)<br>";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(html);

        String pat2 = "";
        Pattern pattern2;
        Matcher matcher2;

        String name = "";
        String professor = "";
        String room = "arranged";
        String time = "";
        String href = "";
        String status = "";
        ArrayList<String> list = new ArrayList<String>();
        String each_class;
        while (matcher.find()) {

            String clas = matcher.group();

            pat2 = ">(.*?)&nbsp;<";
            pattern2 = Pattern.compile(pat2);
            matcher2 = pattern2.matcher(clas);
            if (matcher2.find()) {
                name = matcher2.group().replace("/A>&nbsp;<", "").replace(">", "").replaceAll("&nbsp;", "").replace("<", "");
            }

            pat2 = "html(.*?)</A></b>";
            pattern2 = Pattern.compile(pat2);
            matcher2 = pattern2.matcher(clas);
            if (matcher2.find()) {
                String name2 = matcher2.group().replace("</A></b>", "");
                while (name2.contains(">")) {
                    name2 = name2.substring(1);
                }
                name = name + "\n" + name2;
            }

            pat2 = "&nbsp;<A HREF=(.*?)>";
            pattern2 = Pattern.compile(pat2);
            matcher2 = pattern2.matcher(clas);
            if (matcher2.find()) {
                href = matcher2.group().replace("&nbsp;<A HREF=", "").replace(">", "");

            }

            pat2 = "</A> (.*?)(Open|Closed)";
            pattern2 = Pattern.compile(pat2);
            matcher2 = pattern2.matcher(clas);
            while (matcher2.find() && name.length() > 1) {

                each_class = matcher2.group();
                String[] array = each_class.split(" ");
                for (String s : array) {
                    if (s.length() > 1) {
                        list.add(s);
                    }
                    if (s.contains(",")) {
                        professor = s.replace(",", "\n");
                    }
                }

                if (!list.get(1).equals("to") && list.get(2).contains("-")) {
                    time = list.get(1) + "\n" + list.get(2).replace("-", "\n");
                }
                room = list.get(3) + " " + list.get(4);
                status = list.get(list.size() - 1);
                list.clear();

                classes.add(new Class(0, name, time, room, "", professor, href, status));

            }


        }
        return classes;
    }

}

