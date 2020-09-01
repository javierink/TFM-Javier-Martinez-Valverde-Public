package com.example.healthcarehighbloodpressure.omronDevice;

import android.app.Application;
import android.content.Context;

import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral;

/**
 * This class manage the omron peripheral memory. It needs to be stored on the periphery but it is
 * also necessary to keep a context that should be used in all calls to the sdk so that it does not
 * give problems.
 */
public final class OmronManager extends Application {


    private static OmronManager instance;
    // It is necessary to save the scan to pass it to the activity, since it is not castable
    OmronPeripheral scanOmronPeripheral, connectedOmronPeripheral;
    final String TAG = "OmronApp";

    public Context AppContext;

    /**
     * Singleton constructor
     */
    private OmronManager() {

        AppContext = null;
        scanOmronPeripheral = null;
        connectedOmronPeripheral = null;
    }

    // Set and get Methods

    public static OmronManager getSingletonInstance() {
        if (instance == null){
            instance = new OmronManager();
        }

        return instance;
    }

    public void setAppContext(Context context){
        AppContext = context;
    }

    public Context getAppContext(){
        return AppContext;
    }

    public boolean isAppContext(){
        if(AppContext != null){
            return true;
        }else{
            return false;
        }
    }

    public void setScanOmronPeripheral(OmronPeripheral scan){
        scanOmronPeripheral = scan;
    }

    public OmronPeripheral getScanOmronPeripheral(){
        return scanOmronPeripheral;
    }

    public void setConnectedOmronPeripheral(OmronPeripheral connect){
        connectedOmronPeripheral = connect;
    }

    public OmronPeripheral getConnectedOmronPeripheral(){
        return connectedOmronPeripheral;
    }

    public boolean isConnected(){
        if(connectedOmronPeripheral != null){
            return true;
        }else{
            return false;
        }
    }
}
