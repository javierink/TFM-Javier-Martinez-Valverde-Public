package com.example.healthcarehighbloodpressure.supportClass;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.healthcarehighbloodpressure.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Runnable class for manage the geo location info. The geo is a special info of the app.
 * That's why he's apart from the rest. We are trying to find a city and a country to take away
 * work from the server, the services for climate and pollution that we use are free.
 */
public class GeoManager implements Runnable {
    private static final String TAG = "GeoManager";

    private Location location;


    @Override
    public void run() {

        if(location != null){

            try{
            /**
             * The Geocoder gives us the information of the area through its coordinates.
             * It may fail or have incomplete information. For example, it may not give
             * us the city if we are in the country far from the city.
             */
            LocationAddressModel address = geoCoder();
                sendGeo(address.getCountry(), address.getCity());
                // The geoCoder class maybe have a null pointer when dont have internet access
            }catch (NullPointerException i){
                Log.i(TAG, "Failured connexion for get geoCoder");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Set for location
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * We use a geocoder to complete an address.
     * @return Address of the location
     */
    private LocationAddressModel geoCoder() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ContextManager.getSingletonInstance().getAppContext(), Locale.getDefault());
        LocationAddressModel addressModel = null;
        try {

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses != null && addresses.size() > 0){
                addressModel = new LocationAddressModel(addresses.get(0).getAddressLine(0),
                        addresses.get(0).getLocality(),
                        addresses.get(0).getAdminArea(),
                        addresses.get(0).getCountryName(),
                        addresses.get(0).getPostalCode(),
                        addresses.get(0).getFeatureName());

                Log.i(TAG, "Country: " + addressModel.getCountry());
                Log.i(TAG, "City: " + addressModel.getCity());
                Log.i(TAG, "CP: " + addressModel.getPostalCode());
                Log.i(TAG, "Address: " + addressModel.getAddress());
                Log.i(TAG, "KnownName: " + addressModel.getKnownName());
                Log.i(TAG, "City: " + addressModel.toString());
            }else{
                Log.i(TAG, "Null or empty list in getFromLocation");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressModel;
    }

    /**
     * Send the geolocation to the server
     * @param country Country if exist
     * @param city City if exist
     */
    private void sendGeo(String country, String city) {


        JSONObject json = new JSONObject();

        long time = System.currentTimeMillis();
        android.util.Log.i(TAG, "Time value in millisecinds: " + time);

        try {
            json.put("username", LoginManager.getInstance().getUsername());
            json.put("datetime", time);
            json.put("country", country);
            json.put("city", city);
            json.put("lat", location.getLatitude());
            json.put("lon", location.getLongitude());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "JSON: " + json.toString());

        // Sends the json to the login server
        HttpsPostRequest post = new HttpsPostRequest();
        try {
            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_weather_method), json.toString()).get();
            String code = codResponse.get("code");
            String response = codResponse.get("response");

            Log.i(TAG, "Code: " + code);
            Log.i(TAG, "Response: " + response);


            if (code.equals("201")) {
                // If the code is 201 is a successful connexion

                Log.i(TAG, "Geolocation sent correctly");

            }else{
                // In other case are a error with the server
                Log.i(TAG, "Failured connexion with the server");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}