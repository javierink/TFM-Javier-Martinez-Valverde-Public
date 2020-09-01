package com.example.healthcarehighbloodpressure.services;

/**
 * Singleton class for manage the time for the services launch
 */
public class TimingServicesManager {

    private static TimingServicesManager instance;
    final String TAG = "TimingServicesManager";

    long healthDeviceTime;
    long dataTime;
    long geoTime;


    /**
     * Time intervals in seconds for the execution of services
     */
    final long intervalHealthDeviceTime = 900000;
    final long intervalDataTime = 1800000;
    final long intervalGeoTime = 1800000;

    /*final long intervalHealthDeviceTime = 60000;
    final long intervalDataTime = 90000;
    final long intervalGeoTime = 90000;*/


    /**
     * Singleton constructor
     */
    private TimingServicesManager() {

        // To do this at least once, use 0
        healthDeviceTime = 0;
        dataTime = 0;
        geoTime = 0;

    }

    // Set and get Methods

    public static TimingServicesManager getSingletonInstance() {
        if (instance == null){
            instance = new TimingServicesManager();
        }

        return instance;
    }

    public void setHealthDeviceTime(){
        healthDeviceTime = System.currentTimeMillis();
    }

    public void setDataTime(){
        dataTime = System.currentTimeMillis();
    }

    public void setGeoTime(){
        geoTime = System.currentTimeMillis();
    }

    public boolean isTimeForHealthDevice(){
        if((healthDeviceTime == 0) || (intervalHealthDeviceTime + healthDeviceTime < System.currentTimeMillis())){
            return true;
        }else{
            return false;
        }
    }

    public boolean isTimeForDataTime(){
        if((dataTime == 0) || (intervalDataTime + dataTime < System.currentTimeMillis())){
            return true;
        }else{
            return false;
        }
    }

    public boolean isTimeForGeoTime(){
        if((geoTime == 0) || (intervalGeoTime + geoTime < System.currentTimeMillis())){
            return true;
        }else{
            return false;
        }
    }
}
