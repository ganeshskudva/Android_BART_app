package com.gkudva.bart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gkudva.bart.Models.BartHolidayModel;
import com.gkudva.bart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkudva on 10/31/15.
 */
public class BartHolidayListAdapter extends ArrayAdapter {
    private ArrayList<BartHolidayModel> list;
    private final String DEBUG_TAG = "Ganesh";

    public BartHolidayListAdapter(Context context,  List<BartHolidayModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.list = (ArrayList)objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BartHolidayModel model = (BartHolidayModel) getItem(position);

        if (null == convertView)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.holiday_list_item, parent, false);
        }

        TextView holidayName = (TextView) convertView.findViewById(R.id.tvHolidayName);
        TextView holidatDate = (TextView) convertView.findViewById(R.id.tvHolidayDate);

        holidayName.setText(model.getName());
        holidatDate.setText(model.getDay() + ", " + model.getDate());

        return convertView;
    }

    @Override
    public int getCount() {

        return list.size();
    }
}
