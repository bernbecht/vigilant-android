package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.br.adapter.AdapterProfileSettingsAuxList;
import com.br.adapter.AdapterProfileSettingsList;

public class ProfileActivity extends Activity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileActivity.context = getApplicationContext();


        ListView l1 = (ListView) this.findViewById(R.id.list1);
        ListView l2 = (ListView) this.findViewById(R.id.list2);

        String[] values1 = new String[]{"Change email", "Change password", "Change city"};
        String[] values2 = new String[]{"Logout"};
        AdapterProfileSettingsList adapter1 = new AdapterProfileSettingsList(this, values1);
        AdapterProfileSettingsAuxList adapter2 = new AdapterProfileSettingsAuxList(this, values2);

        l1.setAdapter(adapter1);
        l2.setAdapter(adapter2);
        l1.setItemsCanFocus(true);
        l1.setItemsCanFocus(false);
        l1.setOnItemClickListener(clickHandlerForListSettings);
        l2.setOnItemClickListener(getClickHandlerForListSettingsAux);


    }

    private AdapterView.OnItemClickListener clickHandlerForListSettings = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (position == 0) {
                Intent intent = new Intent(ProfileActivity.context, EditEmail.class);
                startActivity(intent);
            } else if (position == 1) {
                Intent intent = new Intent(ProfileActivity.context, EditPassword.class);
                startActivity(intent);

            } else if (position == 2) {
                Intent intent = new Intent(ProfileActivity.context, EditCity.class);
                startActivity(intent);
            }
        }
    };

    private AdapterView.OnItemClickListener getClickHandlerForListSettingsAux = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (position == 0) {
                Context context = getApplicationContext();
                CharSequence text = "Logout";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };


}
