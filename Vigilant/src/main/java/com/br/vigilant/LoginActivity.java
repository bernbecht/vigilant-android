package com.br.vigilant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.br.SharedPreferencesManager;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Berhell on 09/07/14.
 */
public class LoginActivity extends FragmentActivity {

    private static Context context;

    private final String ACTIVITY_TAG = "LoginActivity";


    private static GraphUser fb_user = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "zmCTLzchKlxqd3r9ygqYZZYaQwKyzgpvTPhEtO5e", "sktKbehLkB8ukPjTHnVYeYg3kRVPicvviZXQC1kJ");


        ParseFacebookUtils.initialize("684872108258145");

        LoginActivity.context = getApplicationContext();

        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_facebook_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        //TODO check the nickname too
        if(ParseUser.getCurrentUser() != null ){
            Intent intent = new Intent(LoginActivity.context, MapActivity.class);
            //make the previous this activity out of backstack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void onLoginButtonClicked() {
        List<String> permissions = Arrays.asList("user_location", "user_birthday", "user_likes", "email");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(ACTIVITY_TAG,
                            "Uh oh. The user cancelled the Facebook login." + user);
                } else if (user.isNew()) {
                    Log.d(ACTIVITY_TAG,
                            "User signed up and logged in through Facebook!" + user);

                    Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            // If the response is successful

                            if (user != null) {

                                LoginActivity.fb_user = user;

                                Log.d(ACTIVITY_TAG, "user facebook " + user);
                                Log.d(ACTIVITY_TAG, "user id: " + user.getId());
                                Log.d(ACTIVITY_TAG, "Current user id: " + ParseUser.getCurrentUser().getObjectId());

                                ParseObject new_user = ParseUser.getCurrentUser();

                                new_user.put("email", fb_user.asMap().get("email"));
                                new_user.put("firstName", fb_user.getFirstName());
                                new_user.put("lastName", fb_user.getLastName());
                                new_user.put("facebookId", fb_user.getId());
                                new_user.put("location", fb_user.getLocation().getProperty("name"));
                                new_user.put("gender", fb_user.asMap().get("gender"));

                                new_user.saveInBackground();

                            }
                        }
                    });
                    Request.executeBatchAsync(request);


                    Intent intent = new Intent(LoginActivity.context, CreateProfileActivity.class);
                    //make the previous this activity out of backstack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Log.d(ACTIVITY_TAG,
                            "User logged in through Facebook! " + user.getObjectId() + " " + user.isNew());
                    Log.d(ACTIVITY_TAG,
                            "Current user " + ParseUser.getCurrentUser());

                    if (user.get("nickname") == null) {
                        Intent intent = new Intent(LoginActivity.context, CreateProfileActivity.class);
                        //make the previous this activity out of backstack
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.context, MapActivity.class);
                        //make the previous this activity out of backstack
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
