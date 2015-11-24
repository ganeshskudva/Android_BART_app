package com.gkudva.bart.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gkudva.bart.Models.BartTransferModel;
import com.gkudva.bart.R;
import com.gkudva.bart.Util.BartHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkudva on 11/8/15.
 */
public class BartRouteDetailAdapter extends ArrayAdapter {
    private ArrayList<BartTransferModel> list;

    public BartRouteDetailAdapter(Context context,  List<BartTransferModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.list = (ArrayList)objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BartTransferModel model = (BartTransferModel) getItem(position);

        if (null == convertView)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.route_detail_list_item, parent, false);
        }

        TextView tvTrainDetail = (TextView) convertView.findViewById(R.id.tvTrainDetail);
        TextView tvOriginDetail = (TextView) convertView.findViewById(R.id.tvOriginDetail);
        TextView tvOriginTimeDetail = (TextView) convertView.findViewById(R.id.tvOriginTimeDetail);
        TextView tvDestinationDetail = (TextView) convertView.findViewById(R.id.tvDestinationDetail);
        TextView tvDestinationTimeDetail = (TextView) convertView.findViewById(R.id.tvDestinationTimeDetail);

        tvTrainDetail.setText(Html.fromHtml("Take <b>"+BartHashMap.getStationNameFromStationCode(model.getTrainHeadStation())+"</b> train from <b>"+BartHashMap.getStationNameFromStationCode(model.getOrigin())
                + "</b> station to <b>"+ BartHashMap.getStationNameFromStationCode(model.getDestination()) + "</b> station"));
        tvOriginTimeDetail.setText(model.getOriginTime());
        tvDestinationTimeDetail.setText(model.getDestinationTime());
        tvOriginDetail.setText(BartHashMap.getStationNameFromStationCode(model.getOrigin()));
        tvDestinationDetail.setText(BartHashMap.getStationNameFromStationCode(model.getDestination()));

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
