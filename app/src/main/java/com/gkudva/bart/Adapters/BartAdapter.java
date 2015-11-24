package com.gkudva.bart.Adapters;

import android.content.Context;
import android.graphics.Color;
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

        convertView.setTag(position);
        TextView SrcStation = (TextView) convertView.findViewById(R.id.tvSrc);
        TextView SrcTime = (TextView) convertView.findViewById(R.id.tvSrcTime);
        TextView DestStation = (TextView) convertView.findViewById(R.id.tvDest);
        TextView DestTime = (TextView) convertView.findViewById(R.id.tvDestTime);
        TextView ArrivalTime = (TextView) convertView.findViewById(R.id.tvArrival);
        TextView TransferInfo = (TextView) convertView.findViewById(R.id.tvTransfer);
        TransferInfo.setText("");


        SrcStation.setText(BartHashMap.getStationNameFromStationCode(model.getOrigin()));
        DestStation.setText(BartHashMap.getStationNameFromStationCode(model.getDestination()));
        SrcTime.setText(model.getOriginTime());
        DestTime.setText(model.getDestTime());
        ArrivalTime.setText(model.getArriveTime());



        if (model.isTransferInvolved())
        {
            TransferInfo.setText("Transfer");
            TransferInfo.setTextColor(Color.parseColor("#E34444"));

        }


        return convertView;
    }

    @Override
    public int getCount() {

        return list.size();
    }
}
