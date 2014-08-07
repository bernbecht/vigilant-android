package com.br.vigilant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.br.utils.CameraUtils;
import com.br.utils.LocationHandler;
import com.br.utils.ParseUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends Activity {

    public static Context context;
    public static Uri fileUri;
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    List<ParseObject> problemsList;
    ParseObject problem;
    LocationHandler locationHandler;
    private Map<Marker, ParseObject> allMarkersMap = new HashMap<Marker, ParseObject>();
    GoogleMap map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("life","onCreate");

        setContentView(R.layout.activity_map);

        MapActivity.context = getApplicationContext();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        map.getUiSettings().setZoomControlsEnabled(false);


            //TODO check which will be the default location
            LatLng waterford = new LatLng(52.256667, -7.129167);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(waterford, 13));

//        locationHandler = LocationHandler.getInstance();
//
//        try {
//            getAllProblemsFromCloud();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        mapInit();

    }

    public void getAllProblemsFromCloud() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Problem");
        query.include("userObject");
        query.include("statusObject");
        query.include("problemCategory");
        query.whereEqualTo("innapropriate", false);
        problemsList = query.find();
    }

    public GoogleMap mapInit() {


        if (problemsList != null) {
            for (int i = 0; i < problemsList.size(); i++) {
                problem = problemsList.get(i);
                ParseObject category = (ParseObject) problem.get("problemCategory");
                ParseObject status = (ParseObject) problem.get("statusObject");
                final ParseGeoPoint problemCoord = (ParseGeoPoint) problem.get("coordinate");
                final LatLng problemPinCoord = new LatLng(problemCoord.getLatitude(), problemCoord.getLongitude());
                //set category logo
                Resources res = context.getResources();
                int resID = res.getIdentifier(category.get("pinName").toString()+"_"+status.get("name").toString(),
                        "drawable",
                        context.getPackageName());
                Drawable drawable = res.getDrawable(resID);

                Marker marker = map.addMarker(new MarkerOptions()
                        .title("Report " + problem.getObjectId())
                        .snippet(category.get("name").toString())
                        .position(problemPinCoord)
                        .icon(BitmapDescriptorFactory.fromResource(resID)));

                allMarkersMap.put(marker, problem);

                Log.d("teste", "user: " + problem);

                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MapActivity.context, ReportDescriptionActivity.class);
                        ParseObject problemClicked = allMarkersMap.get(marker);
                        Log.d("teste", "user infoWindow: " + problemClicked.get("description"));
                        Log.d("teste", "user infoWindow: " + problemClicked);
                        intent.putExtra("objectId", problemClicked.getObjectId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MapActivity.context.startActivity(intent);
                    }
                });
            }
        } else {
            Log.d("problemsList", "problemList Null mapInit " + problemsList);
        }


        return map;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("life","onResume");
        locationHandler = LocationHandler.getInstance();
        ParseUtils.ParseInit(this);
        
        try {
            getAllProblemsFromCloud();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mapInit();
    }

    @Override
    public void onStart() {
        Log.d("life","onStart");
        super.onStart();
    }


    public void changeListActivity(View view) {
        Intent cameraIntent = new Intent(this, AddReportActivity.class);
        startActivity(cameraIntent);
    }

    public void changeProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

