package com.gkudva.bart.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gkudva.bart.Models.BartStationInfoModel;
import com.gkudva.bart.Models.BartStationModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;

public class StationInfoActivity extends AppCompatActivity {

    private BartStationModel stationAddress;
    private BartStationInfoModel stationInfo;
    private TextView tvStationName;
    private TextView tvStationAddress;
    private TextView tvParkingInfo;
    private TextView tvParkingFillTime;
    private TextView tvBikeRacks;
    private TextView tvBikeStation;
    private TextView tvBikeLocker;
    private TextView tvTransitInfo;
    private static final String AVAILABLE = "1";
    private static final String NOT_AVAILABLE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvStationName = (TextView) findViewById(R.id.tvStationName);
        tvStationAddress = (TextView) findViewById(R.id.tvStationAddress);
        tvParkingInfo = (TextView) findViewById(R.id.tvParkingInfo);
        tvParkingFillTime = (TextView) findViewById(R.id.tvParkingFillTime);
        tvBikeRacks = (TextView) findViewById(R.id.tvBikeRacks);
        tvBikeStation = (TextView) findViewById(R.id.tvBikeStation);
        tvBikeLocker = (TextView) findViewById(R.id.tvBikeLocker);
        tvTransitInfo = (TextView) findViewById(R.id.tvTransitInfo);

        stationAddress = (BartStationModel) getIntent().getSerializableExtra("StationAddress");
        stationInfo = (BartStationInfoModel) getIntent().getSerializableExtra("StationInfo");
        getSupportActionBar().setTitle(BartHashMap.getStationNameFromStationCode(stationAddress.getAbbreviation()) + " Station Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStationInfoDetail();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDirection);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+stationAddress.getLatitude()+","+stationAddress.getLongitude()));
                startActivity(intent);
            }
        });
    }

    public void setStationInfoDetail()
    {
        tvStationName.setText(BartHashMap.getStationNameFromStationCode(stationAddress.getAbbreviation()));
        tvStationAddress.setText(stationAddress.getAddress() + "\n" + stationAddress.getCity() +", CA-" +stationAddress.getZip());
        tvParkingInfo.setText(Html.fromHtml(stationInfo.getParkingInfo()));

        if (stationInfo.getParkingFillTime().isEmpty())
        {
            tvParkingFillTime.setText("Not Available");
        }
        else
        {
            tvParkingFillTime.setText(stationInfo.getParkingFillTime());
        }
        if (stationInfo.getBikeParking().equals(AVAILABLE))
        {
            tvBikeRacks.setText("Bike Racks available");
        }
        else
        {
            tvBikeRacks.setText("Bike Racks not available");
        }

        if (stationInfo.getBikeStation().equals(AVAILABLE))
        {
            tvBikeStation.setText("Yes, station is a bike station");
        }
        else
        {
            tvBikeStation.setText("Not a bike station");
        }

        if (stationInfo.getLocker().equals(AVAILABLE))
        {
            tvBikeLocker.setText("Yes, station has lockers");
        }
        else
        {
            tvBikeLocker.setText("No lockers at this station");
        }

        tvTransitInfo.setText(Html.fromHtml(stationInfo.getTransitInfo()));
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
