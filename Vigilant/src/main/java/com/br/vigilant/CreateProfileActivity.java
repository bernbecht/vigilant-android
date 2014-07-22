package com.br.vigilant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Berhell on 09/07/14.
 */
public class CreateProfileActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

    }

    public void createNewProfile(View view){
        ParseObject userWithNickname = ParseUser.getCurrentUser();
        EditText nickname_edittext = (EditText) findViewById(R.id.nickname__edittext);
        userWithNickname.put("nickname",nickname_edittext.getText().toString());
        userWithNickname.saveInBackground();

        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
