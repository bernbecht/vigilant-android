package com.br.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.vigilant.R;

/**
 * Created by Berhell on 08/07/14.
 */
public class AdapterCategoriesList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public AdapterCategoriesList(Context context, String[] values) {
        super(context, R.layout.adapter_categories, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_categories, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.text_item_settings_profile);
        ImageView image = (ImageView) rowView.findViewById(R.id.image_item_settings_profile);
        String[] separated = values[position].split("-");
        textView.setText(separated[0]);
               Resources res = context.getResources();
        int resID = res.getIdentifier(separated[1] , "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        image.setImageDrawable(drawable);

//        image.setImageResource(R.drawable.ic_airport);

        return rowView;
    }
}


