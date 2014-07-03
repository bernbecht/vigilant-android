package com.br.vigilant;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class Map2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        mapInit();
    }

    public GoogleMap mapInit(){
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map2)).getMap();

        map.getUiSettings().setZoomControlsEnabled(false);

        return map;
    }
}

