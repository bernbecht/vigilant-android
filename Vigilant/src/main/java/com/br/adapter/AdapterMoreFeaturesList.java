package com.br.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.vigilant.R;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Berhell on 08/07/14.
 */
public class AdapterMoreFeaturesList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public AdapterMoreFeaturesList(Context context, String[] values) {
        super(context, R.layout.adapter_more_features, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_more_features, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textview_categoryLegend_adapaterMoreFeatures);
        textView.setText(values[position].toString());

        return rowView;
    }
}


