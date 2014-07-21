package com.br.vigilant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;


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

    private boolean isResumed = false;

    private UiLifecycleHelper uiHelper;

    private static GraphUser fb_user;

    public static ParseObject parseUser;


    //    //Link the function after the request
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    //callback from clicking FB button
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            if (session != null && state.isOpened()) {

                SharedPreferencesManager.setIsLogged(LoginActivity.context, true);

                makeRequest(session);


            } else if (state.isClosed()) {
            }
        }
    }


    //check if there is an user with this credentials registered in our server
    // if not, register user on PARSE
    private void checkUserOnServerOnLogin() {

        ParseQuery<ParseObject> userExits = ParseQuery.getQuery("User");
        userExits.whereEqualTo("facebookId", fb_user.getId());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(userExits);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (!results.isEmpty()) {
                    Log.i(ACTIVITY_TAG, "user existe");
                    Log.i(ACTIVITY_TAG, "user ID: " + results.get(0).getObjectId());
                    parseUser = results.get(0);

                    if (parseUser.get("nickname") == null) {
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

                } else {
                    Log.i(ACTIVITY_TAG, "user NAO existe");
                    Log.i(ACTIVITY_TAG, "Salvando user...");
                    ParseObject newUser = new ParseObject("User");
                    newUser.put("facebookId", fb_user.getId());
                    newUser.put("email", fb_user.asMap().get("email"));
                    newUser.put("firstName", fb_user.getFirstName());
                    newUser.put("lastName", fb_user.getLastName());
                    newUser.saveInBackground();
                    parseUser = newUser;
                }
            }
        });
    }

    private void makeRequest(final Session session) {
        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                // If the response is successful

                if (user != null) {
                    Log.d(ACTIVITY_TAG, "user " + user);
                    LoginActivity.fb_user = user;
                    Log.d(ACTIVITY_TAG, "user email: " + user.getId());

                    checkUserOnServerOnLogin();
                }
            }
        });
        Request.executeBatchAsync(request);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "iYqmrHJxkjaJNQAwRjkBu2QyXM0nYvNOJMOljLrO", "K3slw23JivOvFirBErTvyVf7da5wLmxBkPkrUmYx");


        LoginActivity.context = getApplicationContext();

        uiHelper = new UiLifecycleHelper(this, callback);

        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        LoginButton authButton = (LoginButton) findViewById(R.id.login_facebook_button);
        authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "email"));

        //Change the Facebook' button background
//        LoginButton button = (LoginButton) findViewById(R.id.login_facebook_button);
//        button.setBackgroundResource(R.drawable.ic_facebook_login);
//        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

//        Log.d(ACTIVITY_TAG, "button: " + button);
    }


    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    public void facebookLogin(View view) {

        Log.d(ACTIVITY_TAG, "function facebookLogin");

        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                Intent intent = new Intent(context, CreateProfileActivity.class);
                                startActivity(intent);
                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }

    public void twitterLogin(View view) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }

    public void googleLogin(View view) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }
}
