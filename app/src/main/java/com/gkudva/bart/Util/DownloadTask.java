package com.gkudva.bart.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.gkudva.bart.Activities.MainActivity;
import com.gkudva.bart.Activities.MapsActivity;

import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gkudva on 10/31/15.
 */
public class DownloadTask extends AsyncTask<String, Void, String>  {

    private Context context;
    private ProgressDialog progressDialog;
    private int callerActivity;
    private static final int CALLER_MAIN_ACTIVITY = 1;
    private static final int CALLER_MAP_ACTIVITY = 2;

    public DownloadTask(Context context, int activity) {
        this.context = context;
        this.callerActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {

            for (String url : urls) {
                HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    response = IOUtils.toString(httpConn.getInputStream(), "UTF-8");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        //super.onPostExecute(s);
        progressDialog.dismiss();

        switch (callerActivity) {
            case CALLER_MAIN_ACTIVITY:
                MainActivity.onTaskCompletion.onTaskCompleted(response);
                break;
            case CALLER_MAP_ACTIVITY:
                MapsActivity.onTaskCompletion.onTaskCompleted(response);
                break;
            default:
                break;
        }
    }
}
