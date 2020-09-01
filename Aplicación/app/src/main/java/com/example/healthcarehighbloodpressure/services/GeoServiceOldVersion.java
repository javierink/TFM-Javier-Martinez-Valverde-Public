package com.example.healthcarehighbloodpressure.services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.app.Service;

import com.example.healthcarehighbloodpressure.supportClass.ContextManager;
import com.example.healthcarehighbloodpressure.supportClass.GeoManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
// Source: https://stackoverflow.com/questions/8828639/get-gps-location-via-a-service-in-android

/**
 * Service to ask the system for the position of the phone. This is the mobile version that has
 * an Oreo system or earlier. In the case of this version, unlike the most modern mobiles, the
 * service must be terminated because it can remain open and not work the next time it is called.
 * Not in use now. This service is for continuous query through the changed position.
 */
public class GeoServiceOldVersion extends Service {

    private static final String TAG = "GeoServiceOldVersion";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            // Thread for the communication with server
            // Is needed a thread for not lost frames
            if(LoginManager.getInstance().isLogin()){
                GeoManager geo = new GeoManager();
                geo.setLocation(location);
                geo.run();
            }

            stopSelf();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
            stopSelf();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "IBinder");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        // If isn't context is because the app is close
        if(!LoginManager.getInstance().isLogin() || !ContextManager.getSingletonInstance().isAppContext()){
            Log.d(TAG, "Service stopped because context is null or not login");
            stopSelf();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Not location permission");
            stopSelf();
        }else{

            initializeLocationManager();

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.e(TAG, "Fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.e(TAG, "Network provider does not exist, " + ex.getMessage());
            }

            try {
                mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.e(TAG, "Fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.e(TAG, "GPS provider does not exist " + ex.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.e(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}