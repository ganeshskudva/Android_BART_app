package com.gkudva.bart.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.gkudva.bart.Adapters.BartRouteDetailAdapter;
import com.gkudva.bart.Models.BartTransferModel;
import com.gkudva.bart.Models.BartTripModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;

import java.util.ArrayList;

public class RouteDetailActivity extends AppCompatActivity {

    private BartTripModel model;
    private TextView tvFareDetail;
    private TextView tvTravelDurationDetail;
    private TextView tvTrainDetail;
    private TextView tvOriginDetail;
    private TextView tvOriginTimeDetail;
    private TextView tvDestDetail;
    private TextView tvDestTimeDetail;
    private BartRouteDetailAdapter adapter;
    private ArrayList<BartTransferModel> stationList;
    private ListView lvRouteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = (BartTripModel) getIntent().getSerializableExtra("Model");
        getSupportActionBar().setTitle(BartHashMap.getStationNameFromStationCode(model.getOrigin()) + " -> " + BartHashMap.getStationNameFromStationCode(model.getDestination()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvFareDetail = (TextView) findViewById(R.id.tvFareDetail);
        tvTravelDurationDetail = (TextView) findViewById(R.id.tvTravelDurationDetail);

        stationList = (ArrayList)model.getTransferStationList();
        adapter = new BartRouteDetailAdapter(this, stationList);
        lvRouteDetail = (ListView) findViewById(R.id.lvRouteDetail);
        lvRouteDetail.setAdapter(adapter);

        displayRouteDetails();
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

    public void displayRouteDetails()
    {
        tvFareDetail.setText(model.getFare()+"/"+Float.parseFloat(model.getFare())*2+"$");
        tvTravelDurationDetail.setText(model.getTravelDuration());

//        adapter.clear();
//        adapter.addAll(stationList);
        adapter.notifyDataSetChanged();

    }
}
