package com.br.vigilant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.adapter.AdapterCategoriesList;

/**
 * Created by Berhell on 08/07/14.
 */
public class AddReportActivity extends Activity {

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        AddReportActivity.context = getApplicationContext();

    }

    public void categoryButtonClicked(View view) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Which category?");



        String[] categories_names = new String[]{"Airport-ic_airport", "Animals-ic_animals", "Environment and Pollution-ic_environment",
        "Public Lighting-ic_lighting", "Public Property-ic_property", "Public Transport-ic_transport", "Road and Street Conditions-ic_road",
        "Trees-ic_tree", "Secutiry-ic_security", "Rubbish-ic_rubbish", "Other-ic_others", "Culture, Sports and Leisure-ic_culture", "Hospitals and Health Care-ic_hospital",
        "Family and Social Assistance-ic_family", "Public Service-ic_service"};

        final AdapterCategoriesList adapter_categories = new AdapterCategoriesList(this, categories_names);

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(adapter_categories,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = adapter_categories.getItem(which);
                        String[] separated =name.split("-");
                        Log.d("tag","click: "+name);

                        TextView categoryName = (TextView) findViewById(R.id.add_report_category_name);
                        categoryName.setText(separated[0]);

                        ImageView categoryImage = (ImageView) findViewById(R.id.add_report_category_image);
                        Resources res = context.getResources();
                        int resID = res.getIdentifier(separated[1] , "drawable", context.getPackageName());
                        Drawable drawable = res.getDrawable(resID);
                        categoryImage.setImageDrawable(drawable);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();

    }
}
