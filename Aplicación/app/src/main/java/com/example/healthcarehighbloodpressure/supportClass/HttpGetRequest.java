package com.example.healthcarehighbloodpressure.supportClass;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class for get http connections with json data. Asynchronous class.
 */

public class HttpGetRequest extends AsyncTask<String, Integer, String> {

    /**
     * Class that makes a get connection in background
     * @param params Address and json parameters
     * @return String with the response data
     */
    @Override
    protected String doInBackground(String... params) {
        try {

            String urlToRead = params[0];
            //String data = params[1];
            int response;
            //byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            //int postDataLength = postData.length;
            URL url = null;
            url = new URL(urlToRead);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("charset", "utf-8");
            //conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            //DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            //wr.write(postData);
            //wr.flush();
            //wr.close();
            conn.connect();

            response = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer respuesta = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                respuesta.append(line);
            }
            rd.close();
            // Server response
            return new String(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
