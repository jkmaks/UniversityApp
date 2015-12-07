
package com.jkmaks.myuw.activities;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Max on 10/9/2014.
 */
public class HTML_Getter{

    protected String get(String address) {
        StringBuilder builder = new StringBuilder();

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Log.e("error 1", " io exception, most likely no internet");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.d("no error 1", builder.toString());
        return builder.toString();
    }

}

