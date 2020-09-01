package com.example.healthcarehighbloodpressure.omronDevice;

//Source of principal idea  https://github.com/codepath/android_guides/wiki/Using-a-BaseAdapter-with-ListView

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.R;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral;

import java.util.ArrayList;

/**
 * Custom adapter for represent a scannedDeviceAdapter
 */
public class ScannedDevicesAdapter extends BaseAdapter {

    private final Context context;

    private ArrayList<OmronPeripheral> peripheralList;


    public ScannedDevicesAdapter(Context context, ArrayList<OmronPeripheral> peripheralList) {
        this.context = context;
        this.peripheralList = peripheralList;
    }

    public void setPeripheralList(ArrayList<OmronPeripheral> peripheralList) {

        this.peripheralList = peripheralList;
    }

    @Override
    public int getCount() {
        return this.peripheralList.size();
    }

    @Override
    public OmronPeripheral getItem(int position) {
        return this.peripheralList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context). inflate(R.layout.list_view_custom, parent, false);
        }

        // get current item to be displayed
        OmronPeripheral currentItem = (OmronPeripheral) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.text_view_item_name);
        TextView textViewItemDescription = (TextView) convertView.findViewById(R.id.text_view_item_description);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getModelName());
        textViewItemDescription.setText(currentItem.getUuid());

        // returns the view for the current row
        return convertView;
    }

}
