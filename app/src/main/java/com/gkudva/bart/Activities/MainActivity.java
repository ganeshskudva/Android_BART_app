package com.gkudva.bart.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gkudva.bart.Adapters.BartAdapter;
import com.gkudva.bart.Fragments.DatePickerFragment;
import com.gkudva.bart.Fragments.TimePickerFragment;
import com.gkudva.bart.Interfaces.OnTaskCompletion;
import com.gkudva.bart.Models.BartAdvisoryModel;
import com.gkudva.bart.Models.BartHolidayModel;
import com.gkudva.bart.Models.BartSugarCRMModel;
import com.gkudva.bart.Models.BartTripModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;
import com.gkudva.bart.Util.BartSharedPrefs;
import com.gkudva.bart.Util.BartStationList;
import com.gkudva.bart.Util.DownloadTask;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnTaskCompletion, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ListView lvBartSchedule;
    private ArrayList<BartTripModel> bartModel;
    private BartAdapter bartAdapter;
    private String SrcStation;
    private String DestStation;
    private Spinner srcSpinner;
    private Spinner destSpinner;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvEmissions;
    private ImageView ivTime;

    private String time;
    private String date;
    private DownloadTask task;
    private ProgressDialog progressDialog;
    private Context context;
    private final String BART_KEY = "MW9S-E7SL-26DU-VV8V";
    final String INTENT_ACTION = "content";
    private final int TASK_SCHEDULE = 1;
    private final int TASK_ADVISORY = 2;
    private final int TASK_ELEVATOR_STATUS = 3;
    private final int TASK_TRAIN_COUNT = 4;
    private final int TASK_HOLIDAY = 5;
    private static final int TRIP_DETAIL = 1234;
    private int currentTask;
    private BroadcastReceiver mReceiver;
    public static OnTaskCompletion onTaskCompletion;
    private static final int MAIN_ACTIVITY = 1;
    private static final String KEY_SHARED_PREF_ORIGIN = "origin";
    private static final String KEY_SHARED_PREF_DEST = "destination";
    private static final String KEY_TODAY="today";
    private static final String KEY_NOW = "now";

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
                saveCurrentTrip();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordLayout);
        onTaskCompletion = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (isNetworkAvailable()) {
            //check_advisory();
            bartModel = new ArrayList<BartTripModel>();
            bartAdapter = new BartAdapter(this, bartModel);
            lvBartSchedule = (ListView) findViewById(R.id.lvSchedule);
            lvBartSchedule.setAdapter(bartAdapter);
            BartHashMap.initializeStationsMap();
            srcSpinner = (Spinner) findViewById(R.id.spFrom);
            destSpinner = (Spinner) findViewById(R.id.spTo);

            date =KEY_TODAY;
            time = KEY_NOW;

            setSrcDestFromSavedRoutes();
            setSpinnerListeners();
            setListViewTouchListener();
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
        else if (id == R.id.action_datetime)
        {
            onDatePicker();
        }
        else if (id == R.id.action_shuffle)
        {
            onFlipper();
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
        } else if (id == R.id.nav_elevator) {
            check_elevator_status();
        } else if (id == R.id.nav_count) {
            check_train_count();
        } else if (id == R.id.nav_holiday) {
            check_holidays();
        } else if (id == R.id.nav_map) {
            launch_map_activity();
        } else if (id == R.id.nav_saved_trip) {
            Intent intent = new Intent(this, SavedTripActivity.class);
            startActivityForResult(intent, TRIP_DETAIL);
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
            return;
        }

        if (SrcStation.equals(DestStation)) {
            Toast.makeText(getApplicationContext(), "Please select different station for Origin & Destination ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (SrcStation.equals("Select a Station")) {
            Toast.makeText(getApplicationContext(), "Select a valid station for Origin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DestStation.equals("Select a Station")) {
            Toast.makeText(getApplicationContext(), "Select a valid station for Destination", Toast.LENGTH_SHORT).show();
            return;
        }

        Snackbar.make(coordinatorLayout, "Fetching Schedule for " +date+" time "+time, Snackbar.LENGTH_LONG).show();
        // String url = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig="+SrcStation+"&key="+BART_KEY;
        String url = "http://api.bart.gov/api/sched.aspx?cmd=depart&orig=" + SrcStation + "&dest=" + DestStation + "&date="+date+"&time="+time+"&key=MW9S-E7SL-26DU-VV8V&b=0&a=4&l=0";
        date = KEY_TODAY;
        time = KEY_NOW;
        currentTask = TASK_SCHEDULE;
        bartAdapter.clear();
        task = new DownloadTask(this, MAIN_ACTIVITY);
        task.execute(url);

    }

    public void setSpinnerListeners() {
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SrcStation = BartHashMap.getStationCodeFromStationName(parent.getItemAtPosition(position).toString());
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
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void check_advisory() {
        String url = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=" + BART_KEY + "&date=today";
        currentTask = TASK_ADVISORY;
        task = new DownloadTask(this, MAIN_ACTIVITY);
        task.execute(url);

    }

    public void check_elevator_status()
    {
        String url = "http://api.bart.gov/api/bsa.aspx?cmd=elev&key=" + BART_KEY;
        currentTask = TASK_ELEVATOR_STATUS;
        task = new DownloadTask(this, MAIN_ACTIVITY);
        task.execute(url);
    }

    public void check_train_count()
    {
        String url = "http://api.bart.gov/api/bsa.aspx?cmd=count&key=" + BART_KEY;
        currentTask = TASK_TRAIN_COUNT;
        task = new DownloadTask(this, MAIN_ACTIVITY);
        task.execute(url);
    }

    public void check_holidays()
    {

        String url = "http://api.bart.gov/api/sched.aspx?cmd=holiday&key=" + BART_KEY;
        currentTask = TASK_HOLIDAY;
        task = new DownloadTask(this, MAIN_ACTIVITY);
        task.execute(url);
    }

    public void launch_map_activity()
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void load_schedule(String xmlResponse)
    {
        bartModel.addAll(BartTripModel.fromXML(xmlResponse));
        bartAdapter.notifyDataSetChanged();
    }

    public void onTaskCompleted(String response) {

        switch (currentTask) {
            case TASK_SCHEDULE:
                bartAdapter.clear();
                BartTripModel model = new BartTripModel();
                model.fromXML(response);
                bartModel.addAll(BartTripModel.fromXML(response));
                bartAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Check Advisory for any delays", Toast.LENGTH_SHORT).show();
                break;
            case TASK_ADVISORY:
                BartAdvisoryModel adv = new BartAdvisoryModel();
                //Snackbar.make(coordinatorLayout, adv.advisoryFromResponse(response), Snackbar.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("ADVISORY")
                        .setMessage(adv.advisoryFromResponse(response))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case TASK_ELEVATOR_STATUS:
                BartAdvisoryModel elev = new BartAdvisoryModel();
                //Snackbar.make(coordinatorLayout, elev.advisoryFromResponse(response), Snackbar.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("ELEVATOR UPDATE")
                        .setMessage(elev.advisoryFromResponse(response))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case TASK_TRAIN_COUNT:
                BartAdvisoryModel trainCount = new BartAdvisoryModel();
                Snackbar.make(coordinatorLayout, "There are curently " + trainCount.trainCountFromResponse(response) + " trains active in the system", Snackbar.LENGTH_LONG).show();
                break;
            case TASK_HOLIDAY:
                ArrayList<BartHolidayModel> holidayList = new ArrayList<>();
                holidayList = BartHolidayModel.holidaysFromResponse(response);
                Intent intent = new Intent(this, HolidayListActivity.class);
                intent.putExtra("holidayList", holidayList);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void onTimePicker()
    {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onDatePicker()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onFlipper()
    {
        String tempStation;
        tempStation = SrcStation;
        SrcStation = DestStation;
        DestStation = tempStation;

        srcSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(BartHashMap.getStationNameFromStationCode(SrcStation)));
        destSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(BartHashMap.getStationNameFromStationCode(DestStation)));

        getData();
    }

    public void saveCurrentTrip()
    {
        new AlertDialog.Builder(this)
                .setTitle("SAVE ROUTE")
                .setMessage("Origin: " + BartHashMap.getStationNameFromStationCode(SrcStation) + "\nDestination: " + BartHashMap.getStationNameFromStationCode(DestStation))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        BartSugarCRMModel model = new BartSugarCRMModel(BartHashMap.getStationNameFromStationCode(SrcStation),
                                                                        BartHashMap.getStationNameFromStationCode(DestStation));
                        model.save();
                        BartSharedPrefs.setValue(KEY_SHARED_PREF_ORIGIN,
                                BartHashMap.getStationNameFromStationCode(SrcStation),
                                getApplicationContext());


                        BartSharedPrefs.setValue(KEY_SHARED_PREF_DEST,
                                BartHashMap.getStationNameFromStationCode(DestStation),
                                getApplicationContext());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void setSrcDestFromSavedRoutes()
    {
        String origin = BartSharedPrefs.getValue(KEY_SHARED_PREF_ORIGIN, getApplicationContext());
        String destination = BartSharedPrefs.getValue(KEY_SHARED_PREF_DEST, getApplicationContext());
        if (origin != null)
        {
            SrcStation = BartHashMap.getStationCodeFromStationName(origin);
            srcSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(origin));
        }
        if (destination != null)
        {
            DestStation = BartHashMap.getStationCodeFromStationName(destination);
            destSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(destination));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (TRIP_DETAIL == requestCode)
        {
            if (RESULT_OK == resultCode)
            {
                SrcStation = BartHashMap.getStationCodeFromStationName(data.getStringExtra("Origin"));
                srcSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(data.getStringExtra("Origin")));
                DestStation = BartHashMap.getStationCodeFromStationName(data.getStringExtra("Destination"));
                destSpinner.setSelection(Arrays.asList(BartStationList.stationList).indexOf(data.getStringExtra("Destination")));
                getData();
            }
        }
    }

    public void setListViewTouchListener()
    {


        lvBartSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BartTripModel model = (BartTripModel) bartAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), RouteDetailActivity.class);
                intent.putExtra("Model", model);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //do some stuff for example write on log and update TextField on activity

        date = Integer.toString(month + 1)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        if (hourOfDay > 12)
        {
            time = Integer.toString(hourOfDay - 12)+":"+Integer.toString(minute)+"+pm";
        }
        else
        {
            time = Integer.toString(hourOfDay)+":"+Integer.toString(minute)+"+am";
        }

        getData();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

}
