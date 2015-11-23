package com.gkudva.bart.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.gkudva.bart.Adapters.BartHolidayListAdapter;
import com.gkudva.bart.Models.BartHolidayModel;
import com.gkudva.bart.R;

import java.io.Serializable;
import java.util.ArrayList;

public class HolidayListActivity extends AppCompatActivity implements Serializable{

    private ListView lvHolidayList;
    private BartHolidayListAdapter adapter;
    private ArrayList<BartHolidayModel> bartHolidayListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (isNetworkAvailable()) {
            bartHolidayListModel = (ArrayList) getIntent().getSerializableExtra("holidayList");
            adapter = new BartHolidayListAdapter(this, bartHolidayListModel);
            lvHolidayList = (ListView) findViewById(R.id.lvHolidayList);
            lvHolidayList.setAdapter(adapter);

            //adapter.clear();
            //adapter.addAll(bartHolidayListModel);
            adapter.notifyDataSetChanged();
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
