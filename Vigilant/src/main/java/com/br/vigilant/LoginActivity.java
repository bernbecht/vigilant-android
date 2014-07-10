package com.br.vigilant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

/**
 * Created by Berhell on 09/07/14.
 */
public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void facebookLogin(View view) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
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
