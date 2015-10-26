package com.gkudva.bart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gkudva.bart.Models.BartTripModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;

import java.util.List;

/**
 * Created by gkudva on 10/24/15.
 */
public class BartAdapter extends ArrayAdapter {
    private String destination;
    private List<BartTripModel> list;
    private final String DEBUG_TAG = "Ganesh";

    public BartAdapter(Context context, List<BartTripModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.list = objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BartTripModel model = (BartTripModel) getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, parent, false);
        }


        TextView SrcStation = (TextView) convertView.findViewById(R.id.tvSrc);
        TextView SrcTime = (TextView) convertView.findViewById(R.id.tvSrcTime);
        TextView DestStation = (TextView) convertView.findViewById(R.id.tvDest);
        TextView DestTime = (TextView) convertView.findViewById(R.id.tvDestTime);
        TextView Fare = (TextView) convertView.findViewById(R.id.tvFare);
        TextView TravelDuration = (TextView) convertView.findViewById(R.id.tvTravelDuration);

        SrcStation.setText(BartHashMap.getStationNameFromStationCode(model.getOrigin()));
        DestStation.setText(BartHashMap.getStationNameFromStationCode(model.getDestination()));
        SrcTime.setText(model.getOriginTime());
        DestTime.setText(model.getDestTime());
        Fare.setText("$"+model.getFare()+"/$"+Float.parseFloat(model.getFare())*2);
        TravelDuration.setText(model.getTravelDuration());

        return convertView;
    }

    @Override
    public int getCount() {

        return list.size();
    }
}
