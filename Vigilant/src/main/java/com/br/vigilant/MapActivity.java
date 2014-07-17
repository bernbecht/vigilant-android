package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    private static Context context;

    public static final String PREFS_NAME = "VigilantPrefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_map);

            MapActivity.context = getApplicationContext();

            mapInit();

//        if(!isLogged()){
//            Intent intent = new Intent(MapActivity.context, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }

    public boolean isLogged(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean("logged", false);
    }

    @Override
    public void onResume(){
        super.onResume();
//        onResume(!isLogged()){
//            Intent intent = new Intent(MapActivity.context, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }

    public void changeListActivity(View view) {
        Intent intent = new Intent(this, Map2.class);
        startActivity(intent);
    }


    public void changeProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changeAddReportActivity(View view) {
        Intent intent = new Intent(this, AddReportActivity.class);
        startActivity(intent);
    }

    public GoogleMap mapInit() {
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        map.getUiSettings().setZoomControlsEnabled(false);

        LatLng waterford = new LatLng(52.25667, -7.12917);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(waterford, 13));

        map.addMarker(new MarkerOptions()
                .title("Report 1")
                .snippet("This is Spaaaarta")
                .position(waterford));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapActivity.context, ReportDescriptionActivity.class);
                startActivity(intent);
            }
        });


        return map;
    }

}

