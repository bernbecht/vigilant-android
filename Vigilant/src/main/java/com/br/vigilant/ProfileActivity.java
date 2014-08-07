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
import android.widget.TextView;
import android.widget.Toast;

import com.br.utils.ParseUtils;
import com.br.utils.SharedPreferencesManager;
import com.br.adapter.AdapterProfileSettingsAuxList;
import com.br.adapter.AdapterProfileSettingsList;
import com.facebook.Session;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

public class ProfileActivity extends Activity {

    private static Context context;

    public static final String ACTIVITY_TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ParseUtils.ParseInit(this);
        setContentView(R.layout.activity_profile);

        TextView nicknameField = (TextView) findViewById(R.id.textview_nickname_profile);
        TextView emailField = (TextView) findViewById(R.id.textview_email_profile);
        TextView otherDataField = (TextView) findViewById(R.id.textview_otherData_profile);

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");

        nicknameField.setText(ParseUser.getCurrentUser().get("nickname").toString());
        emailField.setText(ParseUser.getCurrentUser().get("email").toString());
        otherDataField.setText(ParseUser.getCurrentUser().get("location").toString() + " | User since " +
                df.format(ParseUser.getCurrentUser().getCreatedAt()));


        ProfileActivity.context = getApplicationContext();

        ListView l1 = (ListView) this.findViewById(R.id.list1);
        ListView l2 = (ListView) this.findViewById(R.id.list2);

        String[] values1 = new String[]{"Change email", "Change Avatar"};
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (position == 1) {
//                Intent intent = new Intent(ProfileActivity.context, EditPassword.class);
//                startActivity(intent);

            }
//            } else if (position == 1) {
//                Intent intent = new Intent(ProfileActivity.context, EditPassword.class);
//                startActivity(intent);
//
//            } else if (position == 2) {
//                Intent intent = new Intent(ProfileActivity.context, EditCity.class);
//                startActivity(intent);
//            }
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
        ParseUser.logOut();
        return true;
    }


}
