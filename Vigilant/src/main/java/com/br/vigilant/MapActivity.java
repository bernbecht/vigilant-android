package com.br.vigilant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mapInit();


    }

    public void changeListActivity(View view){
        Intent intent = new Intent(this, Map2.class);
        startActivity(intent);
    }

    public void changeProfileActivity(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public GoogleMap mapInit(){
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        map.getUiSettings().setZoomControlsEnabled(false);

        return map;
    }

   }

