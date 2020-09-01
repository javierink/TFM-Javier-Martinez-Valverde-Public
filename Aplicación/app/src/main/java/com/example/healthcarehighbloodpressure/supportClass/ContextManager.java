package com.example.healthcarehighbloodpressure.supportClass;

import android.app.Application;
import android.content.Context;

/**
 * Singleton class for save context app
 */
public class ContextManager extends Application {

    private static ContextManager instance;
    final String TAG = "OmronApp";

    public Context AppContext;

    /**
     * Singleton constructor
     */
    private ContextManager() {
        AppContext = null;
    }

    // Set and get Methods

    public static ContextManager getSingletonInstance() {
        if (instance == null){
            instance = new ContextManager();
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
}