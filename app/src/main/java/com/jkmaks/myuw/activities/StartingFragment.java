package com.jkmaks.myuw.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkmaks.myuw.domain.Class;
import com.jkmaks.myuw.domain.Campus;
import com.jkmaks.myuw.domain.DateRow;
import com.jkmaks.myuw.domain.Event;
import com.jkmaks.myuw.domain.Major;
import com.jkmaks.myuw.database.AllClassDB;
import com.jkmaks.myuw.database.DateDB;
import com.jkmaks.myuw.database.EventDB;
import com.jkmaks.myuw.database.MajorDB;
import com.jkmaks.myuw.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copied from Clifton's book Android User Interface Design
 * by Max
 * 1/8/2015
 */
public class StartingFragment extends Fragment {
    TextView progressText;

    ArrayList<String> textUpdates = new ArrayList<String>();
    private int num_upd = 0;

    private static ArrayList<Campus> campuses = new ArrayList<Campus>();
    private static ArrayList<Event> events = new ArrayList<Event>();
    public enum CampusName {Seattle, Tacoma, Bothell};
    private HtmlExtractor htmlExtractor = new HtmlExtractor();
    private Activity activity;
    private DateDB datasourceDates;
    private EventDB datasourceEvents;
    private MajorDB datasourceMajors;
    private AllClassDB datasourceAllClasses;

    /**
     * Classes wishing to be notified of loading progress/completion
     * implement this.
     */
    public interface ProgressListener {
        /**
         * Notifies that the task has completed
         *
         * @param result Double result of the task
         */
        public void onCompletion(Double result);
        /**
         * Notifies of progress
         *
         * @param value int value from 0-100
         */
        public void onProgressUpdate( int value);
    }
    private ProgressListener mProgressListener ;
    private Double mResult = Double. NaN ;
    private LoadingTask mTask ;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //SPLASH SCREEN 205
        // Keep this Fragment around even during config changes
        setRetainInstance( true);
    }
    /**
     * Returns the result
     *
     * @return the result
     */
    public Double getResult() {
        return mResult ;
    }
    /**
     * Returns true if aresult has already been calculated
     *
     * @return true if a result has already been calculated
     * @see #getResult()
     */
    public boolean hasResult() {
        return !Double. isNaN( mResult);
    }
    /**
     * Removes the ProgressListener
     *
     * @see #setProgressListener(ProgressListener)
     */
    public void removeProgressListener() {
        mProgressListener = null ;
    }
    /**
     * Sets the ProgressListener to be notified of updates
     *
     * @param listener ProgressListener to notify
     * @see #removeProgressListener()
     */
    public void setProgressListener(ProgressListener listener) {
        mProgressListener = listener;
    }
    /**
     * Starts loading the data
     */
    public void startLoading() {
        mTask = new LoadingTask();
        datasourceDates = new DateDB(activity.getBaseContext());
        datasourceEvents = new EventDB(activity.getBaseContext());
        datasourceMajors = new MajorDB(activity.getBaseContext());
        datasourceAllClasses = new AllClassDB(activity.getBaseContext());
        try {

            datasourceEvents.open();
            datasourceDates.open();
            datasourceAllClasses.open();
            datasourceMajors.open();
        } catch (SQLException s) {
            Log.e("Exception! ", "something went wrong with sql");
        }
        mTask.execute();

        events.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.starting,
                container, false);
        progressText = (TextView)rootView.findViewById(R.id.ProgressText);
        progressText.append("Bla");

        textUpdates.add("Starting an events update: Seattle");
        textUpdates.add("Starting an events update: Tacoma");
        textUpdates.add("Starting an events update: Bothell");
        textUpdates.add("Starting the quarter list update");
        textUpdates.add("Starting the the major list update");


        return rootView;
    }

    private class LoadingTask extends AsyncTask<Void, Integer, Double>
    {
        @Override
        protected Double doInBackground(Void... params) {

            doEventUpdate("https://www.trumba.com/calendars/sea_campus.rss?filterview=No+Ongoing+Events&filter5=_409198_&filterfield5=30051", CampusName.Seattle,
                    datasourceDates, datasourceEvents);

            this.publishProgress(10);
            doEventUpdate("https://www.trumba.com/calendars/tac_campus.rss?filterview=No+Ongoing+Events&filter5=_409198_&filterfield5=30051", CampusName.Tacoma,
                    datasourceDates, datasourceEvents);
            num_upd++;
            this.publishProgress(30);
            doEventUpdate("https://www.trumba.com/calendars/bot_campus.rss?filterview=No+Ongoing+Events&filter5=_409198_&filterfield5=30051", CampusName.Bothell,
                    datasourceDates, datasourceEvents);
            num_upd++;
            this.publishProgress(50);
            datasourceEvents.close();
            doQuarterUpdate();
            num_upd++;
            this.publishProgress(70);
            doMajorUpdate(datasourceDates, datasourceMajors);
            num_upd++;
            this.publishProgress(90);
            datasourceEvents.close();
            datasourceDates.close();
            datasourceAllClasses.close();
            datasourceMajors.close();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressText.setText("Testing");
                }
            });


            return 0.0;

        }
        @Override
        protected void onPostExecute(Double result) {

            mResult = result;
            mTask = null ;
            if ( mProgressListener != null) {
                mProgressListener.onCompletion( mResult);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

            if ( mProgressListener != null) {
                Log.d("here we go", textUpdates.get(num_upd));
                mProgressListener.onProgressUpdate(values[0]);

                progressText.setText(textUpdates.get(num_upd));
                progressText.setEnabled(true);
                progressText.refreshDrawableState();
                Log.d("text view is ", " " +progressText.getText());
            }


        }
    }


    public void doEventUpdate(String address, CampusName c, DateDB ddb, EventDB edb) {
        Log.d("Doing event update", "here, here");
        String html = extractEventHTML(address);
        if (!checkEventUpdate(c.name(), html, ddb)) {

            edb.deleteEvents(c.name());
            Log.d("getting events online", "yes");
            updateEventDB(c.name(), html, edb);
        } else {
            Log.d("getting events offline", "yes");

            List<Event> db_events = edb.getAllEvents();

            for (Event e : db_events) {
                Log.d("Event name is ", e.getName());
                if(e.getCampus().equals(c.name())) {
                    events.add(e);
                }
            }
        }
    }

    public boolean checkEventUpdate(String campus_name, String the_html, DateDB the_datasource) {
        Time now = new Time();
        now.setToNow();
        //datasource.createClass("CSS305 MW 11:00 1:00 JOY 103");
        List<DateRow> values = the_datasource.getAllDates();
        String pat = "<lastBuildDate>(.*?)</lastBuildDate>";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(the_html);
        String date = "";
        if (matcher.find()) {
            date = matcher.group();
        }
        boolean found = false;
        for (DateRow d : values) {
            if (d.getDate().equals(date + " for " + now.month + "/" + now.monthDay) && d.getName().equals("Events of " + campus_name)) {
                found = true;
                //Toast.makeText(getBaseContext(), "The events were already added", Toast.LENGTH_SHORT).show();
            }
        }
        if (!found) {
            the_datasource.deleteDateRow("Events of " + campus_name);
            the_datasource.createDate(new DateRow(0, "Events of " + campus_name, date + " for " + now.month + "/" + now.monthDay));
        }
        Log.d("date is ", date);
        Log.d("found is ", "" + found);
        return found;
    }

    public void updateEventDB(String campus_name, String the_html, EventDB edb) {
        Log.d("In campus ", campus_name);
        ArrayList<Event> new_events = htmlExtractor.extractEvent(the_html);
        Log.d("New events' size is ", "" +new_events.size());
        events.add(new Event(-1, "UW " + campus_name, "", "", "", campus_name));
        edb.createEvent(new Event(-1, "UW " + campus_name, "", "", "", campus_name));
        for (Event e : new_events) {
            Log.d("Event is ", e.getName());
            e.setCampus(campus_name);
            edb.createEvent(e);
            events.add(e);
        }

    }

    public void doQuarterUpdate() {
        String html = extractHTML("http://www.washington.edu/students/timeschd/T/");

        HashMap<String, String> quarters = htmlExtractor.extractQuarters(html, activity);
        for(String quarter: quarters.keySet())   {
            campuses.add(new Campus("UW Tacoma " + quarter.replace("Quarter ", ""), quarters.get(quarter)));
        }
    }

    public void doMajorUpdate(DateDB ddb, MajorDB datasource) {

        for (Campus quarter : campuses) {
            boolean update = false;
            String raw_majors = extractHTML("http://www.washington.edu/" + quarter.getHref());
            final String address = "http://www.washington.edu" + quarter.getHref();
            HashMap<String, String> majors;
            if (!checkMajorUpdate(ddb, quarter.getName(), address)) {
                majors = htmlExtractor.extractMajors(raw_majors, activity);
                update = true;
                ArrayList<String> removal_list = new ArrayList<String>();
                for(String s : majors.keySet()) {
                    String html = extractHTML(address + majors.get(s));
                    ArrayList<Class> classes = htmlExtractor.extractClasses(html);
                    if(classes.size() < 1)
                        removal_list.add(s);
                    classes.clear();
                }

                Log.d("removal list's size is ", removal_list.size() + "");
                for(String s: removal_list)
                    majors.remove(s);

            } else {
                update = false;
                majors = new HashMap<String, String>();
                List<Major> all_majors = datasource.getAllMajors(quarter.getName());
                Log.d("quarterMajor size is ", "" + all_majors.size());

                for (Major major : all_majors) {
                    if (major.getQuarter().equals(quarter.getName())) {
                        majors.put(major.getName(), major.getHref());
                    }
                }
                //Log.d("Getting majors offline", "yes");
            }

            if(update)  {
                for (String s : majors.keySet()) {
                    datasource.createMajor(new Major(0, s, majors.get(s), quarter.getName()));
                }
            }

            ArrayList<Major> list_major = new ArrayList<Major>();
            for(String s : majors.keySet())
                list_major.add(new Major(0, s, majors.get(s)));
            //Log.d("listmajor size is ", "" + list_major.size());
            majors.clear();
            quarter.clearMajors();
            quarter.setMajors(list_major);
        }
    }

    public boolean checkMajorUpdate(DateDB datasource, String quarter, String html) {

        Time now = new Time();
        now.setToNow();
        //datasource.createClass("CSS305 MW 11:00 1:00 JOY 103");
        List<DateRow> values = datasource.getAllDates();
        String pat = "Modified:(.*?)</ADDRESS>";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(html);
        String date = "";
        if (matcher.find()) {
            date = matcher.group();
        }
        boolean found = false;
        for (DateRow d : values) {
            if (d.getName().equals("Majors of " + quarter) && d.getDate().equals(date)) {
                found = true;
                //Toast.makeText(getBaseContext(), "The events were already added", Toast.LENGTH_SHORT).show();
            }
        }
        if (!found) {
            datasource.deleteDateRow("Majors of " + quarter);
            datasource.createDate(new DateRow(0, "Majors of " + quarter, date));
        }
        return found;
    }

    private String extractEventHTML(String the_address) {
        EventFromXML jg = new EventFromXML();
        String html = jg.get(the_address);
        return html;
    }

    private String extractHTML(String the_address) {
        HTML_Getter jg = new HTML_Getter();

        String html = "";
        html = jg.get(the_address);
        return html;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Campus> getCampuses()   {
        return campuses;
    }


    public void setActivity(Activity the_activity)  {
        activity = the_activity;
    }

}