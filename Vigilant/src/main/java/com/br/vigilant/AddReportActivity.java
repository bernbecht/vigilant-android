package com.br.vigilant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

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

        String[] values1 = new String[]{"Airport", "Animals", "Environment and Pollution",
        "Public Lighting", "Public Property", "Public Transport", "Road and Street Conditions",
        "Trees", "Secutiry", "Rubbish", "Other", "Culture, Sports and Leisure", "Hospitals and Health Care",
        "Family and Social Assistance", "Public Service"};

        AdapterCategoriesList adapter1 = new AdapterCategoriesList(this, values1);

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(adapter1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builderSingle.show();

    }
}
