package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.br.utils.ConnectionUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.regex.Pattern;


public class EditEmail extends Activity {

    public Context context;
    private static final Pattern emailPattern
            = Pattern.compile("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_edit_email);
        EditText newEmail = (EditText) findViewById(R.id.editText_editEmail_editEmail);
        newEmail.setText(ParseUser.getCurrentUser().get("email").toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveItem(MenuItem item) {
        boolean isValid = true;
        EditText newEmail = (EditText) findViewById(R.id.editText_editEmail_editEmail);
        ParseUser user = ParseUser.getCurrentUser();
        if (ConnectionUtils.detectConnection(this)) {
            if (emailPattern.matcher(newEmail.getText().toString()).matches()) {
                Toast.makeText(context, "Saving...", Toast.LENGTH_SHORT).show();
                user.put("email", newEmail.getText().toString());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.d("Tag", "exception " + e);
                        }
                    }
                });
                finish();
            } else {
                Toast.makeText(context, "Email not valid!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "You are without connection. Try it again later.", Toast.LENGTH_LONG).show();
        }

    }
}

