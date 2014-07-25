package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.br.utils.CameraUtils;
import com.br.utils.LocationHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    public static Context context;

    public static Uri fileUri;
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    LocationHandler locationHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        MapActivity.context = getApplicationContext();

        locationHandler = LocationHandler.getInstance();

        mapInit();

    }

    public GoogleMap mapInit() {

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.getUiSettings().setZoomControlsEnabled(false);


        //TODO check which will be the default location
        LatLng waterford = new LatLng(52.256667, -7.129167);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(waterford, 13));

        map.addMarker(new MarkerOptions()
                .title("Report 1")
                .snippet("This is Spaaaarta")
                .position(waterford));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapActivity.context, ReportDescriptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MapActivity.context.startActivity(intent);
            }
        });


        return map;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void changeListActivity(View view) {
        Intent cameraIntent = new Intent(this, AddReportActivity.class);
        startActivity(cameraIntent);
    }


    public void changeProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changeAddReportActivity(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

//        Intent cameraIntent = new Intent(this, AddReportActivity.class);
//        startActivity(cameraIntent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(fileUri);
                sendBroadcast(mediaScanIntent);

                Log.d("MyCamera", "data: " + data.getExtras().get("data"));

                // Image captured and saved to fileUri specified in the Intent
                Intent intent = new Intent(this, AddReportActivity.class);
                intent.putExtra("uri", fileUri);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }


}

