package com.marcoscastany.locationsample.app;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 4/29/2014.
 */
class Server extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.2.2:8080/api/locations");

        try {
            // Add your data
            Date date = new Date();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("lat", params[0]));
            nameValuePairs.add(new BasicNameValuePair("lon", params[1]));
            nameValuePairs.add(new BasicNameValuePair("date", new SimpleDateFormat("yyyyMMdd").format(date)));
            nameValuePairs.add(new BasicNameValuePair("time", new SimpleDateFormat("HH:mm:ss").format(date)));
            nameValuePairs.add(new BasicNameValuePair("name", "marcos"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return response.toString();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return e.toString();
        }
    }

    protected void onPostExecute(String response) {
        // TODO: check this.exception
        // TODO: do something with the feed

    }
}
