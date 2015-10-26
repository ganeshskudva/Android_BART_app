package com.gkudva.bart.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gkudva.bart.Adapters.BartAdapter;
import com.gkudva.bart.Models.BartTripModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;

import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvBartSchedule;
    private ArrayList<BartTripModel> bartModel;
    private BartAdapter bartAdapter;
    private String SrcStation;
    private String DestStation;
    private Spinner srcSpinner;
    private Spinner destSpinner;

    private DownloadScheduleTask task;
    private ProgressDialog progressDialog;
    private Context context;
    private final String BART_KEY = "MW9S-E7SL-26DU-VV8V";
    private final String DEBUG_TAG = "Ganesh";
    final String INTENT_ACTION = "content";
    private final int TASK_SCHEDULE = 1;
    private final int TASK_ADVISORY = 2;
    private final int TASK_ELEVATOR_STATUS = 3;
    private final int TASK_TRAIN_COUNT = 4;
    private int currentTask;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                srcSpinner.setSelection(0);
                destSpinner.setSelection(0);
                bartModel.clear();
                bartAdapter.notifyDataSetChanged();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (isNetworkAvailable()) {
            bartModel = new ArrayList<BartTripModel>();
            bartAdapter = new BartAdapter(this, bartModel);
            lvBartSchedule = (ListView) findViewById(R.id.lvSchedule);
            lvBartSchedule.setAdapter(bartAdapter);
            BartHashMap.initializeStationsMap();
            currentTask = 0;
            srcSpinner = (Spinner) findViewById(R.id.spFrom);
            destSpinner = (Spinner) findViewById(R.id.spTo);
            setupBroadcastReceiver();
            setSpinnerListeners();

        } else {
            Toast.makeText(this, "No Internet connection available", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getData();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_advisory) {
            check_advisory();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void getData() {
        if (SrcStation == null || DestStation == null) {
            Log.d(DEBUG_TAG, " getData() Src: " + SrcStation + "Dest. " + DestStation);
            return;
        }

        if (SrcStation.equals(DestStation)) {
            Toast.makeText(getApplicationContext(), "Please select different station for Origin & Destination ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (SrcStation.equals("Anywhere")) {
            Toast.makeText(getApplicationContext(), "Select a valid station for Origin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DestStation.equals("Anywhere")) {
            Toast.makeText(getApplicationContext(), "Select a valid station for Destination", Toast.LENGTH_SHORT).show();
            return;
        }

        // String url = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig="+SrcStation+"&key="+BART_KEY;
        String url = "http://api.bart.gov/api/sched.aspx?cmd=depart&orig=" + SrcStation + "&dest=" + DestStation + "&date=now&key=MW9S-E7SL-26DU-VV8V&b=0&a=4&l=0";
        Log.d(DEBUG_TAG, "URL: " + url);
        currentTask = TASK_SCHEDULE;
        bartAdapter.clear();
        task = new DownloadScheduleTask();
        task.execute(url);

    }

    public void setSpinnerListeners() {
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SrcStation = BartHashMap.getStationCodeFromStationName(parent.getItemAtPosition(position).toString());
                Log.d(DEBUG_TAG, "Src St. " + SrcStation);
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DestStation = BartHashMap.getStationCodeFromStationName(parent.getItemAtPosition(position).toString());
                Log.d(DEBUG_TAG, "Dest St. " + DestStation);
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public class DownloadScheduleTask extends AsyncTask<String, Void, String> {
        private final String DEBUG_TAG = "Ganesh";

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading BART Schedule....");
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
        /*    BartTripModel model = new BartTripModel();
            model.fromXML(response);
            bartModel.addAll(BartTripModel.fromXML(response));
            bartAdapter.notifyDataSetChanged(); */

            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra("content", response);
            sendBroadcast(intent);
            unregisterReceiver(mReceiver);
        }
    }

    public void check_advisory() {
        String url = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=" + BART_KEY + "&date=today";

    }

    public void setupBroadcastReceiver() {

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (currentTask) {
                        case TASK_SCHEDULE:
                            load_schedule(intent.getStringExtra("content"));
                            break;
                        default:
                            break;
                }
            }
        };

        registerReceiver(mReceiver, null);
    }

    public void load_schedule(String xmlResponse)
    {
        bartModel.addAll(BartTripModel.fromXML(xmlResponse));
        bartAdapter.notifyDataSetChanged();
    }

}
