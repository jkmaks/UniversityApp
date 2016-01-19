
package com.jkmaks.myuw.activities;

import android.util.Log;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
            while(reader.readLine() != null) {
                builder.append(reader.readLine());
            }
        } catch (MalformedURLException e){
            Log.e("error ", " most likely wrong address");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("error ", " io exception, most likely no internet");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        Log.d("no error", builder.toString());

        return builder.toString();
    }
    /**
    public String pageGet(String address){
     Log.d("The htmlunit version", "htmlunit " + pageGet(address));
        String pageAsText = "";
        final WebClient webClient = new WebClient();

        try {
            final HtmlPage page = webClient.getPage(address);
            pageAsText = page.asText();
        } catch (IOException e) {
            Log.e("error ", " io exception, most likely no internet");
            e.printStackTrace();
        }
        return pageAsText;
    }
     **/
}

