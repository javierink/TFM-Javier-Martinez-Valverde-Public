package com.example.healthcarehighbloodpressure.supportClass;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Support class for storing the user's logging information
 */
public final class LoginManager {

    private static final String TAG = "LoginManager";
    JSONObject personalDataJson = null;

    /**
     * Private constructor so nobody can instantiate the class.
     */
    private LoginManager() {
        Log.d(TAG, "Create Singleton");
    }

    /**
     * Static to class instance of the class.
     */
    private static final LoginManager INSTANCE = new LoginManager();

    /**
     * To be called by user to obtain instance of the class.
     *
     * @return instance of the singleton.
     */
    public static LoginManager getInstance() {
        Log.d(TAG, "GetIntance");
        return INSTANCE;
    }

    /**
     * Checks if the user is login
     * @return Boolean that return true if is login
     */
    public boolean isLogin() {
        return (personalDataJson != null);
    }

    /**
     * Deletes user's logging information
     */
    public void logout() {
        personalDataJson = null;
    }

    /**
     * Changes the user's logging information
     * @param personalDataJson User's data json
     */
    public void setPersonalDataJson(JSONObject personalDataJson) {
        this.personalDataJson = personalDataJson;
    }

    /**
     * Returns the user's logging information
     * @return String with the user info in a json
     */
    public JSONObject getPersonalDataJson(){
        return personalDataJson;
    }

    /**
     * Returns the logged-in username
     * @return String with the username
     * @throws JSONException
     */
    public String getUsername() throws JSONException {
        return personalDataJson.getString("username");
    }

}
