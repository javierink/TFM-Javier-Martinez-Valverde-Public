package com.example.healthcarehighbloodpressure;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.supportClass.HttpGetRequest;
import com.example.healthcarehighbloodpressure.supportClass.LocationAddressModel;
import com.example.healthcarehighbloodpressure.supportClass.LocationHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Test activity class for de geo-location and external services.
 * Not in use in the actual version of the app
 */
public class Geo_test extends AppCompatActivity {
    private static final String TAG = "GeoTest";

    //Constants to know in which localization mode it is, coarse(last localization) or fine
    public final static int MY_LOCATION_PERMISSION = 0;
    public final static int MY_LOCATION_PERMISSION_FINE = 1;

    private Switch enabler;
    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblText;
    private TextView lblInfo;
    private TextView lblInfo2;
    private TextView lblInfo3;
    private Button button;

    private Location positionEnabled = null;
    private String cityEnabled = null;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_test);

        enabler = findViewById(R.id.enableGPS);
        lblLatitud = (TextView) findViewById(R.id.textGeoLat);
        lblLongitud = (TextView) findViewById(R.id.textGeoLon);
        lblText = (TextView) findViewById(R.id.textGeoText);
        lblInfo = (TextView) findViewById(R.id.textInfoText);
        lblInfo2 = (TextView) findViewById(R.id.textInfoText2);
        lblInfo3 = (TextView) findViewById(R.id.textInfoText3);
        button = (Button) findViewById(R.id.buttonGeo);

        fusedLocationClient = LocationHelper.getLocationFusedInstance(this);

        //A callBack is created to get the location
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //It may or may not contain a location, if it does, it is treated.
                if (locationResult == null) {
                    lblText.setText("Address: (unknown)");
                    lblLatitud.setText("Latitude: (unknown)");
                    lblLongitud.setText("Longitude: (unknown)");
                }
                for (Location location : locationResult.getLocations()) {
                    Log.i("DEBUG", location.toString());
                    // Update the Location
                    LocationHelper.location = location;
                    updateUI(location);
                }
            }
        };


        //The enabler is checked to operate the GPS location.
        //If activated, the last location button is deactivated.
        //If the enabler is switched off, the button is activated again.
        enabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("DEBUG", "Location updates");
                    button.setEnabled(false);
                    checkPermissionsLocationGPS();
                } else {
                    Log.i("DEBUG", "Stop location updates");
                    button.setEnabled(true);
                    stopLocationUpdates();
                }
            }
        });

    }

    /*Course Position*/

    //Method for checking permissions
    private void checkPermissionsLocation() {
        //Check the permission of the application to access the location course(last location)
        //In case of not having it, the request is made to the user.
        //If you have it, the method is called "getLastLocation()".
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, MY_LOCATION_PERMISSION);
                checkPermissionsLocation();
            }
        } else {
            getLastLocation(fusedLocationClient, this);
        }
    }

    /*
        Method that collects the location and returns it in the form of an address
        getLastLocation "doesn't work" if we are using the fine location in this project because we are overlapping the listener.
     */
    @SuppressLint("MissingPermission")
    private void getLastLocation(FusedLocationProviderClient fusedLocationProviderClient, final Activity activity){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //If the location is not null, it is used to generate the address through the LocationHelper class, it is written in the textView of the activity and updateUI is called.
                if(location != null) {
                    LocationHelper.location = location;
                    Log.i("DEBUG", location.toString());

                    updateUI(location);
                }else {
                    lblText.setText("Address: (unknown)");
                    lblLatitud.setText("Latitude: (unknown)");
                    lblLongitud.setText("Longitude: (unknown)");
                }
            }
        });
    }

    //Method that collects the last location of the mobile, called from the button.
    public void getLocation(View v) {
        //Call the method to get the location
        checkPermissionsLocation();
    }


    /*Fine Position*/

    //Methods for starting and stopping GPS location queries

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(LocationHelper.createLocationRequest(), locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    //Method to check fine location permissions
    private void checkPermissionsLocationGPS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_LOCATION_PERMISSION_FINE);

            }
        } else {
            startLocationUpdates();
        }
    }

    //Write the latitude and longitude in the activity fields
    private void updateUI(Location loc) {




        if (loc != null) {
            LocationAddressModel address = LocationHelper.getAddress(loc.getLatitude(), loc.getLongitude(), getApplicationContext());
            lblText.setText(address.getAddress());

            positionEnabled = loc;
            cityEnabled = address.getCity();

            lblLatitud.setText("Latitude: " + String.valueOf(loc.getLatitude()));
            lblLongitud.setText("Longitude: " + String.valueOf(loc.getLongitude()));
        } else {
            lblText.setText("Address: (unknown)");
            lblLatitud.setText("Latitude: (unknown)");
            lblLongitud.setText("Longitude: (unknown)");
        }
    }

    /*Temperature Services*/

    public void getInfo(View v){

        if(positionEnabled!=null){

            serviceCallOpenWeather();
            serviceCallWaqi();
        }else{
            lblInfo.setText("A location is needed");
        }
    }

    public void serviceCallOpenWeather()
    {
        //String url ="http://api.openweathermap.org/data/2.5/weather?q=Murcia&units=metric&appid=1ad13e5079daf9890772123f3e21d415";
        String url ="http://api.openweathermap.org/data/2.5/weather?q="  + cityEnabled + "&units=metric&appid="+ getString(R.string.api_key_openweather);

        HttpGetRequest get = new HttpGetRequest();

        try {
            String response = get.execute(url, "").get();

            Log.i(TAG, "Response: " + response);

            try {
                JSONObject responseJson = new JSONObject(response);

                Double temp = responseJson.getJSONObject("main").getDouble("temp");
                Double pressure = responseJson.getJSONObject("main").getDouble("pressure");
                Double humidity = responseJson.getJSONObject("main").getDouble("humidity");

                Double tempMin = responseJson.getJSONObject("main").getDouble("temp_min");
                Double tempMax = responseJson.getJSONObject("main").getDouble("temp_max");

                Double windSpeed = responseJson.getJSONObject("wind").getDouble("speed");
                Double clouds = responseJson.getJSONObject("clouds").getDouble("all");

                String description = responseJson.getJSONArray("weather").getJSONObject(0).getString("description");

                lblInfo.setText("Temperature: " + temp.toString() + " Cº" + "\n Min Temp: " + tempMin.toString() + " Cº" + "\n Max Temp: " + tempMax.toString() + " Cº"
                        + "\n Pressure: " + pressure.toString() + " hPa" + "\n Humidity: " + humidity.toString() + " %" + "\n windSpeed: " + windSpeed.toString() + " m/s"
                        + "\n Clouds: " + clouds.toString() + " %" + "\n Description: " + description.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void serviceCallWaqi()
    {
        String url ="http://api.waqi.info/feed/" + cityEnabled + "/?token=" + getString(R.string.api_token_waqi);

        HttpGetRequest get = new HttpGetRequest();

        try {
            String response = get.execute(url, "").get();

            Log.i(TAG, "Response: " + response);

            try {
                JSONObject responseJson = new JSONObject(response);

                if(responseJson.getString("status").equals("ok")){

                    Double aqi = responseJson.getJSONObject("data").getDouble("aqi");

                    Double co = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("co")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("co").getDouble("v") : -1;
                    Double h = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("h")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("h").getDouble("v") : -1;
                    Double no2 = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("no2")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("no2").getDouble("v") : -1;
                    Double o3 = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("o3")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("o3").getDouble("v") : -1;
                    Double p = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("p")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("p").getDouble("v") : -1;
                    Double pm10 = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("pm10")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm10").getDouble("v") : -1;
                    Double pm25 = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("pm25")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm25").getDouble("v") : -1;
                    Double so2 = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("so2")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("so2").getDouble("v") : -1;
                    Double t = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("t")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("t").getDouble("v") : -1;
                    Double w = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("w")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("w").getDouble("v") : -1;
                    Double wg = (responseJson.getJSONObject("data").getJSONObject("iaqi").has("wg")) ? responseJson.getJSONObject("data").getJSONObject("iaqi").getJSONObject("wg").getDouble("v") : -1;


                    lblInfo2.setText("Pollution levels in " + cityEnabled + ":" );


                    lblInfo3.setText("aqi: " + aqi.toString() + "\nco: " + co.toString()
                            + "\nh: " + h.toString() + "\nno2: " + no2.toString() + "\no3: " + o3.toString()
                            + "\np: " + p.toString() + "\npm10: " + pm10.toString() + "\npm25: " + pm25.toString()
                            + "\nso2: " + so2.toString() + "\nt: " + t.toString() + "\nw: " + w.toString()
                            + "\nwg: " + wg.toString());

                }else{
                    lblInfo2.setText("Pollution not available in " + cityEnabled);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void exitToMain(View v) {
        Intent intent = new Intent(Geo_test.this, MainActivity.class);
        startActivity(intent);
    }

}
