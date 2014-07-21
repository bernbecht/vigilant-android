package com.br.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.vigilant.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_login, container, false);

//        LoginButton authButton = (LoginButton) view.findViewById(R.id.login_facebook_button);
//        authButton.setFragment(this);
//        authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes"));

        return view;
    }


}
