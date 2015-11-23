package com.gkudva.bart.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gkudva.bart.Interfaces.OnTaskCompletion;
import com.gkudva.bart.Models.BartStationInfoModel;
import com.gkudva.bart.Models.BartStationModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;
import com.gkudva.bart.Util.DownloadTask;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnTaskCompletion{

    //private ImageView ivMap;
    private Spinner spStation;
    private String station;
    private static final int MAP_ACTIVITY = 2;
    public static OnTaskCompletion onTaskCompletion;
    private int currentTask;
    private static final int TASK_STATION_ADDRESS = 1;
    private static final int TASK_STATION_INFO = 2;
    private static final String BART_KEY = "MW9S-E7SL-26DU-VV8V";
    private DownloadTask task;
    private ArrayList<BartStationModel> stationAddList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("BART Transit Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onTaskCompletion = this;
        //ivMap = (TouchImageView) findViewById(R.id.ivMap);
        //ivMap.setScaleType(ImageView.ScaleType.FIT_XY);

        if (isNetworkAvailable())
        {
            spStation = (Spinner) findViewById(R.id.spStation);
            stationAddList = new ArrayList<>();
            setSpinnerListeners();
        }
        else
        {
            Toast.makeText(this, "No Internet connection available", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void setSpinnerListeners()
    {
        spStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                station = BartHashMap.getStationCodeFromStationName(parent.getItemAtPosition(position).toString());
                getStationsAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getStationsAddress() {
        try {
            if (station.equals("Select a Station")) {
                Toast.makeText(getApplicationContext(), "Select a valid station", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=" + BART_KEY;
            currentTask = TASK_STATION_ADDRESS;
            task = new DownloadTask(this, MAP_ACTIVITY);
            task.execute(url);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getStationInfo()
    {
        String url = "http://api.bart.gov/api/stn.aspx?cmd=stnaccess&orig="+station+"&key="+BART_KEY+"&l=0";
        currentTask = TASK_STATION_INFO;
        task = new DownloadTask(this, MAP_ACTIVITY);
        task.execute(url);
    }

    @Override
    public void onTaskCompleted(String response) {
        switch (currentTask)
        {
            case TASK_STATION_ADDRESS:
                stationAddList = BartStationModel.stationsFromResponse(response);
                getStationInfo();
                break;
            case TASK_STATION_INFO:
                BartStationInfoModel stationInfoModel = new BartStationInfoModel();
                stationInfoModel = BartStationInfoModel.fromXML(response);
                BartStationModel stationAddress = new BartStationModel();
                for (BartStationModel stationAdd: stationAddList)
                {
                    if (stationAdd.getAbbreviation().equals(station))
                    {
                        stationAddress = stationAdd;
                    }
                }

                Intent intent = new Intent(this, StationInfoActivity.class);
                intent.putExtra("StationAddress", stationAddress);
                intent.putExtra("StationInfo", stationInfoModel);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
