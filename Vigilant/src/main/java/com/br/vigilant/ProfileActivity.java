package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.br.SharedPreferencesManager;
import com.br.adapter.AdapterProfileSettingsAuxList;
import com.br.adapter.AdapterProfileSettingsList;
import com.facebook.Session;

public class ProfileActivity extends Activity {

    private static Context context;

    public static final String ACTIVITY_TAG = "ProfileActivity";

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
                logout();

                Intent intent = new Intent(ProfileActivity.context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    };

    private boolean logout() {
        Session session = Session.getActiveSession();
        Log.i(ACTIVITY_TAG, "face -- logout clicked -- session: " + session);
        if (session != null) {
            session.closeAndClearTokenInformation();
            Log.i(ACTIVITY_TAG, "face -- pos logout -- session: " + session);
        }
        SharedPreferencesManager.setIsLogged(ProfileActivity.context, false);
        Log.i(ACTIVITY_TAG, "shared preference logged " + SharedPreferencesManager.isLogged(ProfileActivity.context));
        return true;
    }


}
