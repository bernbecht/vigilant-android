

package com.br.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.br.vigilant.R;

/**
 * Created by Berhell on 07/07/14.
 */
public class AdapterProfileSettingsAuxList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public AdapterProfileSettingsAuxList(Context context, String[] values) {
        super(context, R.layout.adapter_settings_aux_profile, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_settings_aux_profile, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.text_item_settings_profile);
        textView.setText(values[position]);

        return rowView;
    }
}
