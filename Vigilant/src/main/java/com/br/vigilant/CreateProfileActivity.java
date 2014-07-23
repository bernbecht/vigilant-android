package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Berhell on 09/07/14.
 */
public class CreateProfileActivity extends Activity {

    private final String ACTIVITY_TAG = "CreateProfileActivity";

    private static final Pattern nicknamePattern
            = Pattern.compile("^(\\w{6,12})$");
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        CreateProfileActivity.context = getApplicationContext();

    }

    public void createNewProfile(View view) {

        EditText nickname_edittext = (EditText) findViewById(R.id.nickname__edittext);

        final String nickname = nickname_edittext.getText().toString();

        if (nickname.matches("")) {
            Toast.makeText(this, "Please, insert a nickname", Toast.LENGTH_SHORT).show();

        } else {
            if (nicknamePattern.matcher(nickname).matches()) {

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("nickname", nickname);
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (objects.isEmpty()) {
//                            Toast.makeText(CreateProfileActivity.context, "Shablau", Toast.LENGTH_SHORT).show();
                            ParseObject userWithNickname = ParseUser.getCurrentUser();
                            userWithNickname.put("nickname", nickname);
                            userWithNickname.saveInBackground();

                            Intent intent = new Intent(CreateProfileActivity.context, MapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CreateProfileActivity.context, "Nickname in use :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//
            } else {
                Toast.makeText(this, "Please, a nickname needs 6-12 alphanumeric letters or _", Toast.LENGTH_SHORT).show();
            }

        }

//        Log.d(ACTIVITY_TAG, "nickname:"+ nickname_edittext.getText().toString());

//        userWithNickname.put("nickname",nickname_edittext.getText().toString());
//        userWithNickname.saveInBackground();

//        Intent intent = new Intent(this, MapActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

}
