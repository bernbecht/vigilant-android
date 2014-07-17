package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.br.fragment.LoginFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Map;

/**
 * Created by Berhell on 09/07/14.
 */
public class LoginActivity extends FragmentActivity {

    private static Context context;

    private final String ACTIVITY_TAG = "LoginActivity";

    private boolean isResumed = false;

    private UiLifecycleHelper uiHelper;


    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {

            Log.i(ACTIVITY_TAG,"login face session"+ session);

            if (session != null && state.isOpened()) {

                SharedPreferences settings = getSharedPreferences(MapActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged", true);
                // Commit the edits!
                editor.commit();

                if (!checkIsUserOnServer()) {
                    Intent intent = new Intent(LoginActivity.context, CreateProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.context, MapActivity.class);
                    startActivity(intent);
                }



            } else if (state.isClosed()) {
            }
        }
    }

    private boolean checkIsUserOnServer() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginActivity.context = getApplicationContext();

        uiHelper = new UiLifecycleHelper(this, callback);

        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        LoginButton button = (LoginButton) findViewById(R.id.login_facebook_button);

        button.setBackgroundResource(R.drawable.ic_facebook_login);
        button.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        Log.d(ACTIVITY_TAG, "button: "+ button);



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
