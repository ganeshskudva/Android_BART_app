package com.gkudva.bart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gkudva.bart.Models.BartSugarCRMModel;
import com.gkudva.bart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkudva on 11/7/15.
 */
public class BartSavedTripAdapter extends ArrayAdapter {
    private ArrayList<BartSugarCRMModel> list;
    private final String DEBUG_TAG = "Ganesh";

    public BartSavedTripAdapter(Context context,  List<BartSugarCRMModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.list = (ArrayList)objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BartSugarCRMModel model = (BartSugarCRMModel) getItem(position);

        if (null == convertView)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.saved_trip_list_item, parent, false);
        }

        TextView tvTripDetails = (TextView) convertView.findViewById(R.id.tvTripDetails);

        tvTripDetails.setText("Origin: "+model.getOrigin()+"\n"+"Destination: "+model.getDestination());


        return convertView;
    }

    @Override
    public int getCount() {

        return list.size();
    }
}
