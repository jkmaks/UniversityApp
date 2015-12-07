package com.jkmaks.myuw.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.jkmaks.myuw.domain.*;
import com.jkmaks.myuw.domain.Class;
import com.jkmaks.myuw.database.AddedClassDB;
import com.jkmaks.myuw.database.AllClassDB;
import com.jkmaks.myuw.database.DateDB;
import com.jkmaks.myuw.R;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    private static AddedClassDB datasource;
    private static ArrayList<Campus> campuses;
    private static ArrayList<Event> events;
    private static Map<String, String> buildings = new HashMap<String, String>();
    private HtmlExtractor he = new HtmlExtractor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //here should be getextra from starting
        extractHTML("https://www.trumba.com/calendars/tac_campus.rss?filterview=No+Ongoing+Events&filter5=_409198_&filterfield5=30051");
        campuses = (ArrayList<Campus>) getIntent().getSerializableExtra("campuses");
        events = (ArrayList<Event>) getIntent().getSerializableExtra("events");
        Log.d("events are ", events + "");
        datasource = new AddedClassDB(this);
        createAddedClasses();
        setUpListeners((ListView) findViewById(R.id.main_class), (ListView) findViewById(R.id.main_time),
                (ListView) findViewById(R.id.main_building));
        createBuildings();
    }

    public void createAddedClasses() {
        try {
            datasource.open();
        } catch (SQLException s) {
            Log.e("Exception! ", "something went wrong with sql");
        }
        //datasource.createClass("CSS305 MW 11:00 1:00 JOY 103");
        List<Class> values = datasource.getAllClasses();

        ArrayList<String> times = new ArrayList<String>();
        ArrayList<String> rooms = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();

        for (Class c : values) {
            //Log.d("Class is ", c.toString());
            times.add(c.getTime());
            names.add(c.getName());
            rooms.add(c.getRoom());
        }
        createListViews(R.id.main_class, names);
        createListViews(R.id.main_time, times);
        createListViews(R.id.main_building, rooms);
        datasource.close();
    }

    public void addClass(View view) {
        final AlertDialog.Builder chooseTermBuilder = new AlertDialog.Builder(this);
        final AlertDialog.Builder chooseMajor = new AlertDialog.Builder(this);
        final Activity activity = this;
        HashMap<String, String> quarters = new HashMap<String, String>();
        for(Campus quarter : campuses) {
            quarters.put(quarter.getName(), quarter.getHref());
        }
        final String[] term_array = quarters.keySet().toArray(new String[quarters.size()]);
        final Intent intent = new Intent(this, AddClassActivity.class);
        chooseTermBuilder.setTitle("Choose a term")
                .setItems(term_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //We need to go deeper *De Caprio's face
                        final Campus selected_campus = campuses.get(which);
                        Log.d("This campus majors are", selected_campus.getMajors().size() + "");
                        final ArrayList<Major> majors = selected_campus.getMajors();
                        final String[] major_array =
                                selected_campus.getMajorNames().toArray(new String[campuses.get(which).getMajorNames().size()]);
                        final String address = "http://www.washington.edu" +selected_campus.getHref();
                        Arrays.sort(major_array);
                        Collections.sort(majors);
                        AlertDialog.Builder builder = chooseMajor.setTitle("Choose a major")
                                .setItems(major_array, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        final Major selected_major = majors.get(which);
                                        ArrayList<Class> classes = new ArrayList<Class>();
                                        String html = extractHTML(address + selected_major.getHref());

                                        DateDB datasourceDates = new DateDB(activity.getBaseContext());
                                        AllClassDB datasourceAllClasses = new AllClassDB(activity.getBaseContext());

                                        try {
                                            datasourceDates.open();
                                            datasourceAllClasses.open();
                                        } catch (SQLException s) {
                                            Log.e("Exception! ", "something went wrong with sql");
                                        }

                                        if (!checkClassUpdate(html, selected_campus.getName(), selected_major.getName(), datasourceDates)) {
                                            classes = he.extractClasses(html);

                                            for (Class c : classes) {
                                                c.setCampus(selected_campus.getName());
                                                c.setMajor(selected_major.getName());
                                                datasourceAllClasses.createClass(c);
                                            }
                                            Log.d("Getting classes offline", "no");
                                        } else {
                                            List<Class> extracted_classes =
                                                    datasourceAllClasses.getMajorCampusClasses(selected_major.getName(),
                                                            selected_campus.getName());
                                            classes.clear();
                                            Log.d("Extracted classes are", extracted_classes.size() + "");
                                            for (Class c : extracted_classes) {
                                                classes.add(c);
                                            }

                                            Log.d("Getting classes offline", "yes");
                                            Log.d("campus is ", selected_campus.getName());
                                            Log.d("major is ", selected_major.getName());
                                        }
                                        selected_major.setClasses(classes);
                                        datasourceDates.close();
                                        datasourceAllClasses.close();
                                        intent.putExtra("Classes", classes);
                                        intent.putExtra("Campuses", campuses);
                                        intent.putExtra("Events", events);

                                        startActivity(intent);
                                        //for(Class c : classes)  {  Log.i("class is ", c + "\n"); }

                                    }
                                });
                        chooseMajor.show();
                    }
                });
        chooseTermBuilder.show();

    }

    public void showBuildings(View view) {
        BuildingDialog d = new BuildingDialog();
        d.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        d.show(getFragmentManager(), "");
    }

    public void dropClass(View view) {

        DropDialog d = new DropDialog();
        try {
            datasource.open();
        } catch (SQLException s) {
            Log.e("Exception! ", "something went wrong with sql");
        }
        d.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        final List<Class> values = datasource.getAllClasses();
        if (!values.isEmpty()) {
            d.show(getFragmentManager(), "");
        } else {
            Toast.makeText(this.getBaseContext(), "No classes were added", Toast.LENGTH_SHORT).show();
        }

    }

    public void changeTerm(View view) {
        ChangeTermDialog d = new ChangeTermDialog();
        d.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        d.show(getFragmentManager(), "");
    }


    public void showEvents(View view) {

        Intent intent = new Intent(this, Events.class);
        intent.putExtra("Events", events);
        startActivity(intent);
    }

    public static class DropDialog extends DialogFragment {

        private LayoutInflater inflater;
        private Activity activity;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final List<Class> values = datasource.getAllClasses();
            String[] double_array = new String[values.size() + 1];
            double_array[0] = "Drop all the classes";
            for (int i = 0; i < values.size(); i++) {
                double_array[i + 1] = values.get(i).getName().split("\n")[0];
            }
            builder.setTitle("Drop a class")
                    .setItems(double_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                for (Class c : values) {
                                    datasource.deleteClass(c);
                                }
                                Toast.makeText(activity.getBaseContext(), "All classes are dropped", Toast.LENGTH_SHORT).show();
                            } else {
                                datasource.deleteClass(values.get(which - 1));
                                Toast.makeText(activity.getBaseContext(), values.get(which - 1).getName() + " is dropped", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.finish();
                            startActivity(intent);
                        }
                    });

            return builder.create();
        }

        public void setInflater(LayoutInflater inflater, Activity activity) {
            this.inflater = inflater;

            this.activity = activity;
        }

    }

    public static class BuildingDialog extends DialogFragment {

        private LayoutInflater inflater;
        private Activity activity;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final List<String> buildings_array = new ArrayList<String>(buildings.keySet());
            String[] s = new String[buildings_array.size()];
            for (int i = 0; i < s.length; i++) {
                s[i] = buildings_array.get(i);
            }
            builder.setTitle("Choose where you want to go")
                    .setItems(s, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent openLocation = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q= " + buildings.get(buildings_array.get(which)) + " Tacoma, WA"));
                            startActivity(openLocation);
                        }
                    });


            return builder.create();
        }

        public void setInflater(LayoutInflater inflater, Activity activity) {
            this.inflater = inflater;

            this.activity = activity;
        }

    }

    /**
     * Old method, probably will never be used.
     */
    public static class ChangeTermDialog extends DialogFragment {


        private LayoutInflater inflater;
        private Activity activity;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String[] periods = {"Autumn, 2014", "Winter, 2015"};
            builder.setTitle("Choose a term")
                    .setItems(periods, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            return builder.create();
        }

        private void setInflater(LayoutInflater inflater, Activity activity) {
            this.inflater = inflater;
            this.activity = activity;
        }

    }

    private String extractHTML(String the_address) {
        ClassHTML_Getter jg = new ClassHTML_Getter();
        jg.setAddress(the_address);
        jg.execute();
        String html = "";
        try {
            Log.e("JS is ", "" + jg.get());
            html = jg.get();
        } catch (InterruptedException i) {
            Log.e("Error ", " Interrupted Exception");
        } catch (ExecutionException e) {
            Log.e("Error ", " Execution Exception");
        }
        return html;
    }



    public void createListViews(int i, ArrayList<String> list) {
        ListView listView = (ListView) findViewById(i);
        //Log.d("Values are ", values + "");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextSize(13);
                textView.setTextColor(Color.WHITE);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
                } else {
                    params.height = 175;
                }


                view.setLayoutParams(params);

                return view;

            }
        };

        listView.setAdapter(arrayAdapter);
    }

    public void setUpListeners(final ListView listView, final ListView listView2, final ListView listView3) {

        final View[] touchSource = new View[1];

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView2.dispatchTouchEvent(event);
                    listView3.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        touchSource[0] = null;
                    }
                }

                return false;
            }
        });

        listView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView.dispatchTouchEvent(event);
                    listView3.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        touchSource[0] = null;
                    }
                }
                return false;
            }
        });

        listView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView.dispatchTouchEvent(event);
                    listView2.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        touchSource[0] = null;
                    }
                }
                return false;
            }


        });


    }

    public void createBuildings() {
        buildings.put("Academic Building (ADMC)", "1754 Pacific Avenue");
        buildings.put("Birmingham Block (BB)", "1746-48 Pacific Avenue");
        buildings.put("Birmingham Hay & Seed (BHS)", "1740-44 Pacific Avenue");
        buildings.put("Carlton Center (CAR)", "1551 Broadway");
        buildings.put("Cherry Parkes (CP)", "1922 Pacific Avenue");
        buildings.put("Dougan (DOU)", "1721 Jefferson Avenue");
        buildings.put("Garretson Woodruff & Pratt (GWP)", "1754 Pacific Avenue");
        buildings.put("Russell T. Joy (JOY)", "1718 Pacific Avenue");
        buildings.put("Keystone (KEY)", "1754 Commerce Street");
        buildings.put("Laborers Hall", "1742 Market Street");
        buildings.put("Mattress Factory (MAT)", "1953 C Street");
        buildings.put("McDonald Smith (MDS)", "1932 Pacific Avenue");
        buildings.put("William W. Philip Hall (WPH)", "1918 Pacific Avenue");
        buildings.put("Pinkerton", "1702 Broadway Avenue");
        buildings.put("Science (SCI)", "1745 Jefferson Avenue");
        buildings.put("Snoqualmie Building", "1902-10 Commerce Street");
        buildings.put("Tioga Library Building", "1907 Jefferson Avenue");
        buildings.put("University Y Student Center", "1710 Market Street");
        buildings.put("Walsh Gardner (WG)", "1908 Pacific Avenue");
        buildings.put("West Coast Grocery (WCG)", "1732-38 Pacific Avenue");
        buildings.put("The Whitney", "1901 Fawcett Avenue");
    }




    public boolean checkClassUpdate(String html, String campus_name, String major_name, DateDB ddb) {

        ///datasource.createClass("CSS305 MW 11:00 1:00 JOY 103");
        List<DateRow> values = ddb.getAllDates();
        String pat = "Modified:(.*?)<";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(html);
        String date = "";
        if (matcher.find()) {
            date = matcher.group();
        }
        boolean found = false;

        //Log.d("Searched date is ", date);
        for(DateRow d: values)   {
            Log.d("daterow is", d.getName() + " " + d.getDate());
            if(d.getName().equals("Major " + major_name + ", " + campus_name) && d.getDate().equals(date)) {
                found = true;
                //Log.d("found daterow is", d.getName() + " " + d.getDate());
                //Log.d("searched daterow is", major_name + ", " + campus_name + " " + d.getDate());
                Toast.makeText(getBaseContext(), "The classes were already added", Toast.LENGTH_SHORT).show();
            }
        }
        if(!found && date.length() > 5) {
            ddb.deleteDateRow("Major " + major_name + ", " + campus_name);
            //Log.d("Where from ", html);
            //Log.d("Adding new date ", date);
            ddb.createDate(new DateRow(0, "Major " + major_name + ", " + campus_name, date));
        }
        //Log.d("Found is ", found + "");
        return found;
    }

    private class DownloadClasses extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //displayProgressBar("Downloading...");
        }

        @Override
        protected String doInBackground(String... params) {
            String url=params[0];

            // Dummy code
            for (int i = 0; i <= 100; i += 5) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //updateProgressBar(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dismissProgressBar();
        }
    }


}

