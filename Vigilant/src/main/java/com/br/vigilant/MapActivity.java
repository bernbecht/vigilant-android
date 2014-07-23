package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.br.utils.CameraUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    private static Context context;

    public static Uri fileUri;

    public static Intent photoTaken;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map);

        MapActivity.context = getApplicationContext();

        mapInit();
    }

    @Override
    public void onResume() {
        super.onResume();
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
//        Intent intent = new Intent(this, AddReportActivity.class);
//        startActivity(intent);

//        // create Intent to take a picture and return control to the calling application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        fileUri = CameraUtils.getOutputMediaFileUri(CameraUtils.MEDIA_TYPE_IMAGE); // create a file to save the image
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//
//        // start the image capture Intent
//        startActivityForResult(intent, CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(fileUri);
                sendBroadcast(mediaScanIntent);

                Log.d("MyCamera","data: "+data.getExtras().get("data"));

                // Image captured and saved to fileUri specified in the Intent
                Intent intent = new Intent(this, AddReportActivity.class);
                intent.putExtra("uri",fileUri);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
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

