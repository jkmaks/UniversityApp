
package com.jkmaks.activities;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Max on 10/9/2014.
 */
public class EventXML_Getter{

    protected String get(String address) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        //httpGet.addHeader("Authorization", "Bearer 65e6bccb-8807-409a-aed4-9ec82ec08aba");
        try {
            HttpResponse response = client.execute(httpGet);
            //response.setHeader("Authorization", "Bearer 65e6bccb-8807-409a-aed4-9ec82ec08aba");
            //response.setHeader("Accept", "myuw-jkmaks/json");

            StatusLine statusLine = response.getStatusLine();

            HttpEntity entity = response.getEntity();

            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;

            //We don't need whole document for events, only for today
            //So I've counted that we have 30 lines on each event
            //If we think that in worse there will be 30 events on day then we need
            // 30 * 30 = 900 lines + 100 on intro so
            int i = 1000;
            while ((line = reader.readLine()) != null && i > 0) {
                //Log.e("getting ", " " + line);
                builder.append(line);
                i--;
            }

        } catch (ClientProtocolException e) {
            Log.e("error 0", " with client protocol");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("error 1", " io exception, most likely no internet");
            e.printStackTrace();
        }
        Log.d("Builder is ", builder.toString());
        return builder.toString();
    }

}

