package com.marcoscastany.locationsample.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 4/29/2014.
 */
class Server extends AsyncTask<String, Integer, String> {
    CallBackListener mListener;
    TextView view;

    public void setListener(CallBackListener listener){
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("http://10.0.2.2:8080/api/locations");
        HttpPost httppost = new HttpPost("http://tracking-aplication.herokuapp.com/api/locations");
        Exception lastException = null;

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                // Add your data
                Date date = new Date();
                JSONObject postData = new JSONObject();

                postData.put("lat", params[0]);
                postData.put("lon", params[1]);
                postData.put("date", new SimpleDateFormat("yyyyMMdd").format(date));
                postData.put("time", new SimpleDateFormat("HH:mm:ss").format(date));
                postData.put("name", "marcos");

                StringEntity se = new StringEntity( postData.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setEntity(se);

                //We send to the UI to notify status
                publishProgress(attempt + 1, 3);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    return response.getStatusLine().getReasonPhrase();
                }

            } catch (ClientProtocolException e) {
                return e.toString();
            } catch (IOException e) {
                //We wait two seconds !
                SystemClock.sleep(5000);
                lastException = e;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (lastException != null)
            return lastException.toString();
        else
            return "Failed to save position";
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        view.setText("Attempt number " + progress[0] + " from " + progress[1]);
    }

    protected void onPostExecute(String response) {
        if (mListener != null){
            mListener.callback(response);
        }
        if(view != null){
            view.setText(response);
        }
    }

    public void setFinalStatus(TextView viewById) {
        view = viewById;
    }
}
