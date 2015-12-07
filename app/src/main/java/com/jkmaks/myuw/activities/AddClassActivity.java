package com.jkmaks.myuw.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jkmaks.myuw.domain.Campus;
import com.jkmaks.myuw.domain.Class;
import com.jkmaks.myuw.domain.Event;
import com.jkmaks.myuw.database.AddedClassDB;
import com.jkmaks.myuw.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.jkmaks.myuw.R.layout.add_class;


public class AddClassActivity extends Activity {

    private ArrayList<Class> classes;
    private ArrayList<Event> events;
    private ArrayList<Campus> campuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(add_class);
        classes = (ArrayList<Class>) getIntent().getSerializableExtra("Classes");
        events = (ArrayList<Event>) getIntent().getSerializableExtra("Events");
        campuses = (ArrayList<Campus>) getIntent().getSerializableExtra("Campuses");
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> professors = new ArrayList<String>();
        ArrayList<String> times = new ArrayList<String>();
        Collections.sort(classes);
        for(Class c : classes) {
            names.add(c.getName() + "\n     in\n " + c.getRoom());
            times.add(c.getTime());
            professors.add(c.getProfessor());
        }
        fillOutList(professors, (ListView) findViewById(R.id.class_room));
        fillOutList(times, (ListView) findViewById(R.id.class_time));
        fillOutList(names, (ListView) findViewById(R.id.class_name));
        setUpListeners((ListView) findViewById(R.id.class_room), (ListView) findViewById(R.id.class_time),
                (ListView) findViewById(R.id.class_name), this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillOutList(final ArrayList<String> values, ListView listView)    {

        //Log.d("size of first column size is ", "" + values.size());
        //Log.d("list view is ", "" + listView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values){

            @Override
            public View getView(int position, View convertView, ViewGroup parent)   {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextSize(13);
                textView.setTextColor(Color.WHITE);
                boolean full = false;
                for(Class c : classes)  {
                    if(c.getName().equals(classes.get(position).getName()) &&
                            c.getTime().equals(classes.get(position).getTime()) &&
                            c.getHref().equals(classes.get(position).getHref()) &&
                            !c.getStatus().equals("Open")) {
                        textView.setBackgroundColor(new Color().argb(255, 133, 117, 77));
                        Log.d("name is", c.getName());
                        Log.d("status is", c.getStatus());
                        full = true;
                        break;
                    }

                }
                if(!full) {
                    Color uw_purple = new Color();
                    textView.setBackgroundColor(uw_purple.argb(255, 75, 46, 131));
                }
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


    public void setUpListeners(final ListView listView, final ListView listView2, final ListView listView3, final Activity activity)    {

        final View[] touchSource = new View[1];


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                AddedClassDB datasource;
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("campuses", campuses);
                intent.putExtra("events", events);
                datasource = new AddedClassDB(activity.getBaseContext());
                try {
                    datasource.open();
                }
                catch(SQLException s)   {
                    Log.e("Exception! ", "something went wrong with sql");
                }
                boolean duplicate = false;
                List<Class> allclasses = datasource.getAllClasses();
                for(Class c : allclasses)   {
                    //I'm sure there is a smarter way of doing comparison of time, but I didn't find it out. Yet.
                    if(c.getTime().equals(classes.get(position).getTime()) && c.getProfessor().equals(classes.get(position).getHref())) {

                        duplicate = true;
                        Toast.makeText(getBaseContext(), "Class was already added", Toast.LENGTH_SHORT).show();
                    }
                    else if(c.getTime().equals(classes.get(position).getTime()) && c.getProfessor().equals(classes.get(position).getHref())) {
                        duplicate = true;
                        Toast.makeText(getBaseContext(), "Time contradiction", Toast.LENGTH_SHORT).show();
                    }
                    else if(c.getTime().contains("M") && classes.get(position).getTime().contains("M")
                            || c.getTime().contains("T") && classes.get(position).getTime().contains("T") && !c.getTime().contains("Th")
                            || c.getTime().contains("Th") && classes.get(position).getTime().contains("Th")
                            || c.getTime().contains("F") && classes.get(position).getTime().contains("F")) {

                        if(c.getTime().contains("P") && classes.get(position).getTime().contains("P")
                                || !c.getTime().contains("P") && !classes.get(position).getTime().contains("P"))  {
                            String[] array1 = c.getTime().split("\n");
                            String[] array2 = classes.get(position).getTime().split("\n");
                            int time1_beg = Integer.valueOf(array1[1]);
                            int time2_beg = Integer.valueOf(array2[1]);

                            array1[2] = array1[2].replace("p", "");
                            array1[2] = array1[2].replace("P", "");
                            array2[2] = array2[2].replace("p", "");
                            array2[2] = array2[2].replace("P", "");

                            int time1_end = Integer.valueOf(array1[2]);
                            int time2_end = Integer.valueOf(array2[2]);
                            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                            Date start1 = null;
                            Date start2 = null;
                            Date end1 = null;
                            Date end2 = null;
                            try {
                                //it drops zero that is why I have to use splitted array again
                                //for example 3:05 becomes 3:5 which kills the formatter
                                start1 = formatter.parse(time1_beg / 100 + ":" + array1[1].substring(array1[1].length() - 2)
                                        + " AM");


                                end1 = formatter.parse(time1_end / 100 + ":" + array1[2].substring(array1[2].length() - 2)
                                        + " AM");


                                start2 = formatter.parse(time2_beg / 100 + ":" + array2[1].substring(array1[1].length() - 2)
                                        + " AM");


                                end2 = formatter.parse(time2_end / 100 + ":" + array2[2].substring(array2[2].length()- 2)
                                        + " AM");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(start1.getTime() <= end2.getTime() && start2.getTime() <= end1.getTime())    {
                                duplicate = true;
                                Toast.makeText(getBaseContext(), "Time contradiction", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                }


                if(!duplicate) {
                    datasource.createClass(classes.get(position));
                    intent.putExtra("campuses", campuses);
                    intent.putExtra("events", events);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }
        });

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

    public void setByTime(View view)  {
        for(Class c : classes) {
            c.setOrder(Class.SortingType.Time);
        }
        final Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("Classes", classes);
        startActivity(intent);
        this.finish();
    }

    public void setByProfessor(View view)  {
        for(Class c : classes) {
            c.setOrder(Class.SortingType.Professor);
        }
        final Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("Classes", classes);
        startActivity(intent);
        this.finish();
    }


}
