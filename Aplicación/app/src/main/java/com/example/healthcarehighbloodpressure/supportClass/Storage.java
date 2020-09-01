package com.example.healthcarehighbloodpressure.supportClass;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Support class for saving or loading data from or to local files
 */
public class Storage {
    private static final String TAG = "Storage";

    /**
     * Write a file with the data passed by parameters. Delete the existing one.
     * @param filePath Path and name of the file
     * @param data Data to write
     * @param context Contex of the app
     */
    public void writeToFile(String filePath, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    /**
     * Reads the contents of a file
     * @param filePath Path and name of the file
     * @param context Contex of the app
     * @return Data content of the file in a string
     */
    public String readFromFile(String filePath, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filePath);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }

    /**
     * Check if the file exists
     * @param filename Path and name of the file
     * @param context Contex of the app
     * @return Boolean with the result of the operation
     */
    public boolean fileExists(String filename, Context context) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Delete the file
     * @param filename Path and name of the file
     * @param context Contex of the app
     * @return Boolean with the result of the operation
     */
    public boolean deleteFile(String filename, Context context){
        File file = context.getFileStreamPath(filename);
        return file.delete();
    }

}
