package com.jkmaks.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jkmaks.myuw.R;
import com.jkmaks.Types.Event;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Events extends Activity implements Serializable {

    private ArrayList<String> hrefs = new ArrayList<String>();
    private ArrayList<String> locations = new ArrayList<String>();
    private ArrayList<String> times = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        ArrayList<Event> list = (ArrayList<Event>) getIntent().getSerializableExtra("Events");

        /**

         list.add(new Event(0, "Dancing", "www.yandex.ru", "Main Hall", "10:00\n-\n11:55\np.m."));
         list.add(new Event("Talking", "www.yandex.ru", "Joy", "2:00 - 3:00 PM"));
         list.add(new Event("Dying", "www.yandex.ru", "Joy", "5:00 - 8:00 PM"));

         **/


        Log.d("size of list in events is ", "" + list.size());


        for (Event e : list) {
            if (!e.getName().contains("CANCELED")) {
                names.add(e.getName());
                locations.add(e.getLocation());
                times.add(e.getTime());
                hrefs.add(e.getHref());
            }
        }
        Time now = new Time();
        now.setToNow();
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).contains("-") && !times.get(i).contains("O")) {

                String original = times.get(i);

                //if it contains "a" AND "p" then it means it starts in the morning and ends in the afternoon
                //if it contains a - then it is morning event
                //if it contains p - then it is afternoon event
                String apm1 = "";
                String apm2 = "";
                if (original.contains("a") && original.contains("p")) {
                    apm1 = "A";
                    apm2 = "P";
                } else if (original.contains("a")) {
                    apm1 = "A";
                    apm2 = "A";
                } else {
                    apm1 = "P";
                    apm2 = "P";
                }
                String[] splitted = original.split("\n");

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                Date start1 = new Date();
                Date start2 = new Date();
                Date end1 = new Date();
                Date end2 = new Date();
                //300K miliseconds is 5 minutes. Date API is weak.
                end2.setTime(start2.getTime() + 300000);

                //then if it contains ":" then there are minutes, if not there are only hours.
                //this info is for parsing today's date but not time.
                String info = now.monthDay + "/" + (now.month + 1) + "/" + now.year + " ";
                try {
                    if (!splitted[0].contains(":")) {
                        start1 = formatter.parse(info + splitted[0] + ":00:00 " + apm1 + "M");
                    } else {
                        start1 = formatter.parse(info + splitted[0] + ":00 " + apm1 + "M");
                    }
                    if (!splitted[2].contains(":") && !splitted[2].contains("-")) {
                        end1 = formatter.parse(info + splitted[2] + ":00:00 " + apm2 + "M");
                    } else if (splitted[2].contains(":") && !splitted[2].contains("-")) {
                        end1 = formatter.parse(info + splitted[2] + ":00 " + apm2 + "M");
                    } else if (splitted[2].contains("-") && !splitted[3].contains(":")) {
                        end1 = formatter.parse(info + splitted[3] + ":00:00 " + apm2 + "M");
                    } else  {
                        end1 = formatter.parse(info + splitted[3] + ":00 " + apm2 + "M");
                    }

                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                //Log.d("event starts at ", info + splitted[0] + ":00 " + apm1 + "M");
                //Log.d("event ends at ", info + splitted[2] + ":00 " + apm2 + "M");
                //Log.d("start 1 is ", "" + start1.getTime());
                //Log.d("end 1 is ", "" + end1.getTime());
                //Log.d("start 2 is ", "" + start2.getTime());
                //Log.d("end 2 is ", "" + end2.getTime());

                if (start1.getTime() <= end2.getTime() && start2.getTime() <= end1.getTime()) {
                    Toast.makeText(getBaseContext(), names.get(i) + " in "
                            + locations.get(i) + " is going on now", Toast.LENGTH_LONG).show();
                }


            }
        }
        if(names.size() == 0)   {
            names.add("No events for today");
        }
        fillOutList(names, (ListView) findViewById(R.id.events_list));
        fillOutList(times, (ListView) findViewById(R.id.time_list));
        fillOutList(locations, (ListView) findViewById(R.id.room_list));
        setUpListeners((ListView) findViewById(R.id.events_list), (ListView) findViewById(R.id.room_list), (ListView) findViewById(R.id.time_list));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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

    public void fillOutList(ArrayList<String> values, ListView listView) {

        //Log.d("size of first column size is ", "" + values.size());
        //Log.d("list view is ", "" + listView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values) {


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


        final View[] clickSource = new View[1];
        final View[] touchSource = new View[1];

        listView.setOnTouchListener(new View.OnTouchListener() {
            float touched;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView2.dispatchTouchEvent(event);
                    listView3.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (Math.abs(touched - event.getY()) < 10) {
                            int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                            clickOnEvent(position);
                        }
                        touchSource[0] = null;
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        touched = event.getY();

                    }
                }

                return false;
            }
        });

        listView2.setOnTouchListener(new View.OnTouchListener() {
            float touched;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView.dispatchTouchEvent(event);
                    listView3.dispatchTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (Math.abs(touched - event.getY()) < 10) {
                            int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                            clickOnLocation(position);
                        }
                        touchSource[0] = null;
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        touched = event.getY();

                    }
                }
                return false;
            }
        });

        listView3.setOnTouchListener(new View.OnTouchListener() {
            float touched;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource[0] == null)
                    touchSource[0] = v;

                if (v == touchSource[0]) {
                    listView2.dispatchTouchEvent(event);
                    listView.dispatchTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (Math.abs(touched - event.getY()) < 10) {
                            int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                            clickOnDate(position);
                        }
                        touchSource[0] = null;
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        touched = event.getY();

                    }
                }
                return false;
            }
        });

    }

    public void clickOnDate(int num) {
        String time = times.get(num);
        if (time.contains("m")) {
            int[] start_end = convertTo24Time(time);
            int start = start_end[0];
            int end = start_end[1];
            Calendar begin_of_event = Calendar.getInstance();
            Calendar end_of_event = Calendar.getInstance();
            Time now = new Time();
            now.setToNow();
            begin_of_event.set(now.year, now.month, now.monthDay, start / 100, start % 100);
            end_of_event.set(now.year, now.month, now.monthDay, end / 100, end % 100);
            Intent intent = new Intent(Intent.ACTION_EDIT);
            Log.d("Start is", "" + start);
            Log.d("End is", "" + end);

            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", begin_of_event.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("endTime", end_of_event.getTimeInMillis());
            intent.putExtra("title", names.get(num));
            intent.putExtra("description", hrefs.get(num));
            intent.putExtra("eventLocation", locations.get(num));
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

            startActivity(intent);
        }


    }

    public void clickOnEvent(int num) {
        if (!"UW Seattle".equals(hrefs.get(num))
                && !"UW Tacoma".equals(hrefs.get(num))
                && !"UW Bothell".equals(hrefs.get(num))
                && hrefs.get(num).contains("http")) {

            Intent openEvent = new Intent(Intent.ACTION_VIEW, Uri.parse(hrefs.get(num)));
            startActivity(openEvent);
        }


    }

    public void clickOnLocation(int num) {
        Intent openLocation = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=UW " + locations.get(num)));
        startActivity(openLocation);
    }


    private int fixDate(int date)  {
        if (date == 12 || date % 100 == 12)
            date = 1200;
        else if (date > 99)
            date = date + 1200;
        else if (date != 12)
            date = (date + 12) * 100;

        return date;
    }

    private int[] convertTo24Time(String time)   {
        String[] time_array = time.split("\n");
        int start = Integer.parseInt(time_array[0].replaceAll(":", ""));
        int end;
        if (time_array.length < 3 && time_array[1].contains("p.m")) {
            start = fixDate(start);
            end = start;
        } else if (time_array.length < 3 && time_array[1].contains("a.m")) {
            end = start;
        } else if (time_array[1].contains("p.m") && time_array[4].contains("p.m")) {
            end = Integer.parseInt(time_array[3].replaceAll(":", ""));
            start = fixDate(start);
            end = fixDate(end);
            Log.d("case 1", "");
        } else if (time_array[1].contains("a.m") && time_array[4].contains("p.m")) {
            end = Integer.parseInt(time_array[3].replaceAll(":", ""));
            end = fixDate(end);
            start = start * 100;
            Log.d("case 2", "");
        } else if (time_array[1].contains("a.m") && time_array[4].contains("a.m")) {
            end = Integer.parseInt(time_array[3].replaceAll(":", ""));
            end = end * 100;
            start = start * 100;
            Log.d("case 3", "");
        } else if (time_array[1].contains("p.m") && time_array[4].contains("a.m")) {
            end = Integer.parseInt(time_array[3].replaceAll(":", ""));
            start = fixDate(start);
            end = end * 100;
            Log.d("case 4", "");
        } else if (time_array[3].contains("p.m")) {
            end = Integer.parseInt(time_array[2].replaceAll(":", ""));
            start = fixDate(start);
            end = fixDate(end);
            Log.d("case 5", "");
        } else {
            end = Integer.parseInt(time_array[2].replaceAll(":", ""));
            start = start * 100;
            end = end * 100;
            Log.d("case 6", "");
        }
        return new int[]{start, end};
    }

}
