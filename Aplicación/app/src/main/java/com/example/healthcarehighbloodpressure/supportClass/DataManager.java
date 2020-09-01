package com.example.healthcarehighbloodpressure.supportClass;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.healthcarehighbloodpressure.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton class for manage data app.
 */
public final class DataManager {
    private static final String TAG = "DataManager";

    private Lock lockDeviceData;
    private Lock lockSurveyData;

    /**
     * Private constructor so nobody can instantiate the class.
     */
    private DataManager() {

        Log.i(TAG, "Create DataManager Singleton");
        lockDeviceData = new ReentrantLock();
        lockSurveyData= new ReentrantLock();
    }

    /**
     * Static to class instance of the class.
     */
    private static final DataManager INSTANCE = new DataManager();

    /**
     * To be called by user to obtain instance of the class.
     *
     * @return instance of the singleton.
     */
    public static DataManager getInstance() {
        Log.i(TAG, "Get intance DataManager");
        return INSTANCE;
    }

    /**
     * Save a json in the file of old device info
     * @param json
     */
    public void saveDataDeviceToFile(JSONObject json){

        // Block the lock
        lockDeviceData.lock();

        Storage storage = new Storage();
        Context context = ContextManager.getSingletonInstance().getAppContext();

        if(storage.fileExists(context.getString(R.string.fileNameJsonDataDevice), context)){

            String jsonData = storage.readFromFile(context.getString(R.string.fileNameJsonDataDevice), context);

            try {

                JSONArray jsonArr = new JSONArray(jsonData);
                Log.i(TAG, "Read ArrayJson: " + jsonArr.toString());
                jsonArr.put(json);
                Log.i(TAG, "New ArrayJson: " + jsonArr.toString());
                storage.writeToFile(context.getString(R.string.fileNameJsonDataDevice), jsonArr.toString(), context);

            } catch (Throwable t) {
                Log.i(TAG, "Could not parse malformed JSON: \"" + jsonData + "\"");

                JSONArray jsonArr = new JSONArray();
                jsonArr.put(json);

                Log.i(TAG, "ArrayJson: " + jsonArr.toString());

                storage.writeToFile(context.getString(R.string.fileNameJsonDataDevice), jsonArr.toString(), context);
            }

        }else{

            JSONArray jsonArr = new JSONArray();
            jsonArr.put(json);

            Log.i(TAG, "ArrayJson: " + jsonArr.toString());

            storage.writeToFile(context.getString(R.string.fileNameJsonDataDevice), jsonArr.toString(), context);
        }

        // Unblock the lock
        lockDeviceData.unlock();
    }

    /**
     * Send old data device in the file to the server
     */
    public void sendDataDeviceFromFile(){

        lockDeviceData.lock();

        Storage storage = new Storage();
        Context context = ContextManager.getSingletonInstance().getAppContext();

        if(storage.fileExists(context.getString(R.string.fileNameJsonDataDevice), context)){

            String jsonData = storage.readFromFile(context.getString(R.string.fileNameJsonDataDevice), context);

            try {
                JSONArray jsonArr = new JSONArray(jsonData);
                Log.i(TAG, "ArrayJson: " + jsonArr.toString());

                // Sends the json to the login server
                HttpsPostRequest post = new HttpsPostRequest();
                try {
                    HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + context.getString(R.string.ip_server_database) +":" + context.getString(R.string.port_server_database) + context.getString(R.string.server_uri_device_method), jsonArr.toString()).get();
                    String code = codResponse.get("code");
                    String response = codResponse.get("response");

                    Log.i(TAG, "Code: " + code);
                    Log.i(TAG, "Response: " + response);


                    if (code.equals("201")) {
                        // If the code is 201 is a successful connexion

                        boolean del = storage.deleteFile(context.getString(R.string.fileNameJsonDataDevice), context);
                        Log.i(TAG, "The device's data file has been deleted: " + del);

                    }else{
                        // In other case are a error with the server
                        Log.i(TAG, "Failured connexion with the server");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (Throwable t) {
                Log.i(TAG, "Could not parse malformed JSON: \"" + jsonData + "\"");
            }

        }

        lockDeviceData.unlock();

    }

    /**
     * Save a json in the file of old survey
     * @param json
     */
    public void saveDataSurveyToFile(JSONObject json){

        // Block the lock
        lockSurveyData.lock();

        Storage storage = new Storage();
        Context context = ContextManager.getSingletonInstance().getAppContext();


        if(storage.fileExists(context.getString(R.string.fileNameJsonDataSurveys), context)){

            String jsonData = storage.readFromFile(context.getString(R.string.fileNameJsonDataSurveys), context);

            try {

                JSONArray jsonArr = new JSONArray(jsonData);
                Log.i(TAG, "Read ArrayJson: " + jsonArr.toString());
                jsonArr.put(json);
                Log.i(TAG, "New ArrayJson: " + jsonArr.toString());
                storage.writeToFile(context.getString(R.string.fileNameJsonDataSurveys), jsonArr.toString(), context);

            } catch (Throwable t) {
                Log.i(TAG, "Could not parse malformed JSON: \"" + jsonData + "\"");

                JSONArray jsonArr = new JSONArray();
                jsonArr.put(json);

                Log.i(TAG, "ArrayJson: " + jsonArr.toString());

                storage.writeToFile(context.getString(R.string.fileNameJsonDataSurveys), jsonArr.toString(), context);
            }

        }else{

            JSONArray jsonArr = new JSONArray();
            jsonArr.put(json);

            Log.i(TAG, "ArrayJson: " + jsonArr.toString());

            storage.writeToFile(context.getString(R.string.fileNameJsonDataSurveys), jsonArr.toString(), context);
        }

        // Unblock the lock
        lockSurveyData.unlock();
    }

    /**
     * Send old data survey in the file to the server
     */
    public void sendDataSurveyFromFile(){

        lockSurveyData.lock();

        Storage storage = new Storage();
        Context context = ContextManager.getSingletonInstance().getAppContext();

        if(storage.fileExists(context.getString(R.string.fileNameJsonDataSurveys), context)){

            String jsonData = storage.readFromFile(context.getString(R.string.fileNameJsonDataSurveys), context);

            try {
                JSONArray jsonArr = new JSONArray(jsonData);
                Log.i(TAG, "ArrayJson: " + jsonArr.toString());

                // Sends the json to the login server
                HttpsPostRequest post = new HttpsPostRequest();
                try {
                    HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + context.getString(R.string.ip_server_database) +":" + context.getString(R.string.port_server_database) + context.getString(R.string.server_uri_surveys_method), jsonArr.toString()).get();
                    String code = codResponse.get("code");
                    String response = codResponse.get("response");

                    Log.i(TAG, "Code: " + code);
                    Log.i(TAG, "Response: " + response);


                    if (code.equals("201")) {
                        // If the code is 201 is a successful connexion

                        boolean del = storage.deleteFile(context.getString(R.string.fileNameJsonDataSurveys), context);
                        Log.i(TAG, "The device's data file has been deleted: " + del);

                    }else{
                        // In other case are a error with the server
                        Log.i(TAG, "Failured connexion with the server");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (Throwable t) {
                Log.i(TAG, "Could not parse malformed JSON: \"" + jsonData + "\"");
            }

        }

        lockSurveyData.unlock();

    }


    /**
     * Sent data register to ther server
     * @param usename
     * @param password
     * @param name
     * @param surname
     * @param millisDate
     * @param gender
     * @param birthCountry
     * @param region
     * @param city
     * @param zp
     * @param homeCountry
     * @param address
     * @param email
     * @param educationLevel
     * @return
     */
    public String sendDataRegister(String usename, String password, String name, String surname,
                                   long millisDate, String gender, String birthCountry, String region,
                                   String city, int zp, String homeCountry, String address,
                                   String email, String educationLevel)
    {

        // Makes a json with the data info collected in the view
        JSONObject userJson = new JSONObject();
        String code = "-1";


        try {
            userJson.put("username", usename);
            userJson.put("password", password);
            userJson.put("name", name);
            userJson.put("surname", surname);
            userJson.put("birthdate", millisDate);
            userJson.put("gender", gender);
            userJson.put("birthcountry", birthCountry);
            userJson.put("region", region);
            userJson.put("city", city);
            userJson.put("zp", zp);
            userJson.put("homecountry", homeCountry);
            userJson.put("address", address);
            userJson.put("email", email);
            userJson.put("educationlevel", educationLevel);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "Json: " + userJson.toString());

        // Send the json to the register server
        HttpsPostRequest post = new HttpsPostRequest();
        try {
            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_register_method), userJson.toString()).get();

            code = codResponse.get("code");
            String response = codResponse.get("response");

            if(code.equals("201")){
                // If the register is successful

                // Save the data in the LoginManager
                LoginManager.getInstance().setPersonalDataJson(userJson);

                // Save the info in a file
                Storage storage = new Storage();
                storage.writeToFile(ContextManager.getSingletonInstance().getAppContext().getString(R.string.fileNamePersonalLoginInfo), userJson.toString(), ContextManager.getSingletonInstance().getAppContext());

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return code;
    }


    /**
     * Method that send a login to the server
     * @param usename Username
     * @param password Password
     */
    public String sendDataRegister(String usename, String password){

        // Creates a json file with the information for the logging
        JSONObject userJson = new JSONObject();
        String code = "-1";

        try {
            userJson.put("username", usename);
            userJson.put("password", password);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "Json: " + userJson.toString());

        // Sends the json to the login server
        HttpsPostRequest post = new HttpsPostRequest();
        try {
            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_login_method), userJson.toString()).get();
            code = codResponse.get("code");
            String response = codResponse.get("response");

            Log.i(TAG, "Code: " + code);
            Log.i(TAG, "Response: " + response);


            if (code.equals("200")) {
                // If the code is 200 is a successful login
                try {

                    // The information returned by the server is saved. If the logging has been successful it returns all the information of our profile.
                    JSONObject responseJson = new JSONObject(response);

                    Log.i(TAG, responseJson.toString());

                    // Set the info in the LoginManager
                    LoginManager.getInstance().setPersonalDataJson(responseJson);

                    // Save the user info in a file, to future automatic login. The app read this file in the MainSplash
                    Storage storage = new Storage();
                    storage.writeToFile(ContextManager.getSingletonInstance().getAppContext().getString(R.string.fileNamePersonalLoginInfo), responseJson.toString(), ContextManager.getSingletonInstance().getAppContext());

                } catch (Throwable t) {
                    //In case of failure of the info format, there is a problem with the communication to the server
                    Log.i(TAG, "Could not parse malformed JSON: \"" + response + "\"");
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return code;

    }

    /**
     * Method that send a survey to the server
     * @param bundle Bundle with de answers of the survey
     * @param type Survey type
     */
    public void sendDataSurvey(Bundle bundle, int type){

        // Json to send to the server
        JSONObject json = new JSONObject();
        // Flag to know if the data has been send
        boolean sended = false;

        // Save the time in millis
        long time = System.currentTimeMillis();
        android.util.Log.i(TAG, "Time value in millisecinds: " + time);

        // We make the datason
        try {
            json.put("username", LoginManager.getInstance().getUsername());
            json.put("datetime", time);
            json.put("type", type);

            // Depending on the type of survey, it is created differently
            // Physical and social surveys
            if(type == 1 || type == 4){
                for (int i = 1; i < 8; i++)
                {
                    json.put("q"+ i, Integer.parseInt(bundle.getString(Integer.toString(i))));
                }
            }

            // Diet and stress surveys
            if(type == 2 || type == 5){
                for (int i = 1; i < 15; i++)
                {
                    json.put("q"+ i, Integer.parseInt(bundle.getString(Integer.toString(i))));
                }
            }

            // Depression survey
            if(type == 7){
                for (int i = 1; i < 11; i++)
                {
                    json.put("q"+ i, Integer.parseInt(bundle.getString(Integer.toString(i))));
                }
            }

            // Physical attributes survey
            if(type == 8){
                json.put("weight", bundle.getInt("weight"));
                json.put("height", bundle.getInt("height"));
            }

            // Smoke survey
            if(type == 9){
                json.put("cigarettes", bundle.getInt("cigarettes"));
                json.put("electronic", bundle.getInt("electronic"));
                json.put("other", bundle.getInt("other"));
            }

            // Alcohol survey
            if(type == 10){
                json.put("beer", bundle.getInt("beer"));
                json.put("wine", bundle.getInt("wine"));
                json.put("wineMix", bundle.getInt("wineMix"));
                json.put("otherFermented", bundle.getInt("otherFermented"));
                json.put("destilled", bundle.getInt("destilled"));
                json.put("destilledMix", bundle.getInt("destilledMix"));
                json.put("other", bundle.getInt("other"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "JSON: " + json.toString());


        // Sends the json to the survey server
        HttpsPostRequest post = new HttpsPostRequest();

        try {

            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_surveys_method), json.toString()).get();
            String code = codResponse.get("code");
            String response = codResponse.get("response");

            Log.i(TAG, "Code: " + code);
            Log.i(TAG, "Response: " + response);


            if (code.equals("201")) {
                // If the code is 201 is a successful connexion
                Log.i(TAG, "Survey sent correctly");
                // We mark the flag as sent
                sended = true;

            }else{
                // In other case are a error with the server
                Log.i(TAG, "Failured connexion with the server");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If it has not been sent, we save the data in the memory
        if(sended == false){
            Log.i(TAG, "The survey has been saved in a file");
            saveDataSurveyToFile(json);
        }
    }

    /**
     * Method that send a medication survey to the server
     * @param list List with de answers of the survey
     * @param type Survey type
     */
    public void sendDataMedicationSurvey(List<String> list, int type){

        // Json to send to the server
        JSONObject json = new JSONObject();
        // Flag to know if the data has been send
        boolean sended = false;

        // Save the time in millis
        long time = System.currentTimeMillis();
        android.util.Log.i(TAG, "Time value in millisecinds: " + time);

        // We make the datason
        try {
            json.put("username", LoginManager.getInstance().getUsername());
            json.put("datetime", time);
            json.put("type", type);

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i< list.size(); i++){
                jsonArray.put(list.get(i));
            }

            json.put("medication", jsonArray);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "JSON: " + json.toString());


        // Sends the json to the survey server
        HttpsPostRequest post = new HttpsPostRequest();

        try {

            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_surveys_method), json.toString()).get();
            String code = codResponse.get("code");
            String response = codResponse.get("response");

            Log.i(TAG, "Code: " + code);
            Log.i(TAG, "Response: " + response);


            if (code.equals("201")) {
                // If the code is 201 is a successful connexion
                Log.i(TAG, "Survey sent correctly");
                // We mark the flag as sent
                sended = true;

            }else{
                // In other case are a error with the server
                Log.i(TAG, "Failured connexion with the server");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If it has not been sent, we save the data in the memory
        if(sended == false){
            Log.i(TAG, "The survey has been saved in a file");
            saveDataSurveyToFile(json);
        }
    }

    /**
     * Send data measurement to the server
     * @param datatime Time in millis of the measurement
     * @param pulse Pulse of the measurement
     * @param pressuresys Systolic pressure of the measurement
     * @param pressuredia Diastolic pressure of the measurement
     * @param daySteps Steps of the measurement
     */
    public void sendDataDevice(Double datatime, Double pulse, Double pressuresys, Double pressuredia, Double daySteps){

        // Json to send to the server
        JSONObject json = new JSONObject();
        // Flag to know if the data has been send
        boolean sended = false;

        // We make the datason
        try {
            json.put("username", LoginManager.getInstance().getUsername());
            json.put("datetime", datatime);
            json.put("pulse", pulse);
            json.put("pressuresys", pressuresys);
            json.put("pressuredia", pressuredia);
            json.put("steps", daySteps);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i(TAG, "JSON: " + json.toString());

        // Sends the json to the survey server
        HttpsPostRequest post = new HttpsPostRequest();
        try {
            HashMap<String, String> codResponse = post.execute(ContextManager.getSingletonInstance().getAppContext().getString(R.string.method_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.ip_server_database) +":" + ContextManager.getSingletonInstance().getAppContext().getString(R.string.port_server_database) + ContextManager.getSingletonInstance().getAppContext().getString(R.string.server_uri_device_method), json.toString()).get();
            String code = codResponse.get("code");
            String response = codResponse.get("response");

            Log.i(TAG, "Code: " + code);
            Log.i(TAG, "Response: " + response);


            if (code.equals("201")) {
                // If the code is 201 is a successful connexion
                Log.i(TAG, "Data device sent correctly");
                // We mark the flag as sent
                sended = true;

            }else{
                // In other case are a error with the server
                Log.i(TAG, "Failured connexion with the server");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If it has not been sent, we save the data in the memory
        if(sended == false){
            Log.i(TAG, "The survey has been saved in a file");
            saveDataDeviceToFile(json);
        }
    }

}
