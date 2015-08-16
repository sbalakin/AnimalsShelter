package com.app.animalsshelter.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.animalsshelter.R;


/**
 * Created by DAT on 5/31/2015.
 */
public class CustomAdapterDrawer extends BaseAdapter {

    private SparseBooleanArray enabledItems = new SparseBooleanArray();

    Context context;
    String[] listDrawer;
    int[] images = {R.drawable.shelter
            , R.drawable.shelter
            , R.drawable.sendpet
            , R.drawable.lostfound
            , R.drawable.lostdog
            , R.drawable.helpus
            , R.drawable.about};

    public CustomAdapterDrawer(Context context) {
        this.context = context;
        listDrawer = context.getResources().getStringArray(R.array.drawer_items);
    }

    int mSelectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return listDrawer.length;
    }

    @Override
    public Object getItem(int position) {
        return listDrawer[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row_drawer, parent, false);
        } else {
            row = convertView;
        }
        TextView textView = (TextView) row.findViewById(R.id.textViewInCustom);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageViewInCustom);

        textView.setText(listDrawer[position]);
        imageView.setImageResource(images[position]);

        if (position == mSelectedPosition) {// WTF??????????????????????????????????????????
            row.setBackgroundColor(context.getResources().getColor(android.R.color.secondary_text_light_nodisable));
            row.setClickable(true);
        } else {
            row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            row.setClickable(false);
            //areAllItemsEnabled();
        }

        return row;
    }
}
