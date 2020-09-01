package com.example.healthcarehighbloodpressure.services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.healthcarehighbloodpressure.supportClass.ContextManager;
import com.example.healthcarehighbloodpressure.supportClass.GeoManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

import java.util.List;

//Source: https://www.vogella.com/tutorials/AndroidLocationAPI/article.html

/**
 * Service to ask the system for the position of the phone.
 */

public class GeoService extends JobIntentService implements LocationListener {

    private static final String TAG = "GeoService";

    private LocationManager locationManager;
    Location location;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, GeoService.class, 124, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        if (!LoginManager.getInstance().isLogin() || !ContextManager.getSingletonInstance().isAppContext()) {
            Log.d(TAG, "Service stopped because context is null or not login");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Not location permission");
        } else {

            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the location provider -> use
            // default
            location = getLastKnownLocation();

            // Initialize the location fields
            if (location != null) {
                Log.d(TAG, "onLocationChanged " + location);
            } else {
                Log.d(TAG, "Location not available");
            }
        }

    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");

        // Thread for the communication with server
        if(location != null && LoginManager.getInstance().isLogin()){
            GeoManager geo = new GeoManager();
            geo.setLocation(location);
            geo.run();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        Log.d(TAG, "onLocationChanged " + location );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}

// Source: https://stackoverflow.com/questions/8828639/get-gps-location-via-a-service-in-android
// Continuous position with changed position. Not in use.
// Service to ask the system for the position of the phone. This is the mobile version that has a
// post Oreo system.
/*
public class GeoService extends JobIntentService {


    private static final String TAG = "GeoService";

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 60000;
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


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, GeoService.class, 124, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        // If isn't context is because the app is close
        if(!LoginManager.getInstance().isLogin() || !ContextManager.getSingletonInstance().isAppContext()){
            Log.d(TAG, "Service stopped because context is null or not login");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Not location permission");
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
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "Fail to remove location listeners, ignore", ex);
                }
            }
        }

    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

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


    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}*/
