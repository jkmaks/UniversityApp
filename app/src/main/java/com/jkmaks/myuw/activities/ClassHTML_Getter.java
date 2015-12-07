
package com.jkmaks.myuw.activities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Max on 10/9/2014.
 */
public class ClassHTML_Getter extends AsyncTask<String, Void, String> {

    String address;

    protected String doInBackground(String... urls) {
        StringBuilder builder = new StringBuilder();
        Log.d("address is ", "address is " + address);
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (MalformedURLException e){
                Log.e("error 2", " io exception, most likely no internet");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("error 1", " io exception, most likely no internet");
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e){
            Log.e("error 2", " io exception, most likely no internet");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("error 1", " io exception, most likely no internet");
            e.printStackTrace();
        }


        Log.d("no error", builder.toString());
        return builder.toString();
    }



    public void setAddress(String the_address) {
        address = the_address;
    }


}

