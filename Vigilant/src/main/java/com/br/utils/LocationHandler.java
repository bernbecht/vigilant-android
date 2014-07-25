package com.br.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.br.vigilant.MapActivity;
import com.br.vigilant.R;
import com.br.vigilant.ReportDescriptionActivity;
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

/**
 * Created by Berhell on 24/07/14.
 */
public class LocationHandler implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    Context context;
    GoogleMap map;
    private static LocationHandler locationHandler;
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private LocationHandler() {
        context = MapActivity.context;
        mLocationClient = new LocationClient(context, this, this);
        this.connect();

    }

    public static LocationHandler getInstance() {
        if (locationHandler == null) {
            locationHandler = new LocationHandler();
        }
        return locationHandler;
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status

        mCurrentLocation = mLocationClient.getLastLocation();

        Log.d("loc", "location: " + mCurrentLocation.getLatitude());

        Toast.makeText(context, "Connected " + mCurrentLocation.getLatitude() + ","
                + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();


    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(context, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public void connect() {
        mLocationClient.connect();
    }

    public void disconnect() {
        mLocationClient.disconnect();
    }

    public double getActualLatitude() {
        mCurrentLocation = mLocationClient.getLastLocation();
        return mCurrentLocation.getLatitude();
    }

    public double getActualLongitude() {
        mCurrentLocation = mLocationClient.getLastLocation();
        return mCurrentLocation.getLongitude();
    }

    public void setContext(Context c) {
        this.context = c;
    }

}
