package com.example.healthcarehighbloodpressure.supportClass;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.healthcarehighbloodpressure.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Class for post http connections with json data. Asynchronous class. With security x509.
 */
public class HttpsPostRequest extends AsyncTask<String, Integer, HashMap<String, String>> {

    /**
     * Class that makes a post connection in background
     * @param params Address and json parameters
     * @return Hashmap with code and response data
     */
    @Override
    protected HashMap<String, String> doInBackground(String... params) {

        String urlToRead = params[0];
        String data = params[1];

        int responseCode;
        byte[] postData = data.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        URL url = null;
        try {
            url = new URL(urlToRead);

            Log.d("Http: ", "url:" + urlToRead);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setSSLSocketFactory(CustomSSLSocketFactory.getSSLSocketFactory(ContextManager.getSingletonInstance().getAppContext()));

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setConnectTimeout(10000);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();
            conn.connect();

            InputStream is ;
            responseCode = conn.getResponseCode();

            // Check the type of code received
            if ((responseCode == HttpURLConnection.HTTP_OK)||(responseCode == HttpURLConnection.HTTP_CREATED)){
                Log.d("Http: ", Integer.toString(responseCode));
                is = conn.getInputStream();
            }else {
                // If is a error must be manage
                Log.d("Http: ", Integer.toString(responseCode));
                is = conn.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            HashMap<String, String> codResponse = new HashMap<String, String>();
            codResponse.put("code", Integer.toString(responseCode));
            codResponse.put("response", new String(response));

            //Return code and data response
            return codResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //In other case return a neutral error
        HashMap<String, String> codResponse = new HashMap<String, String>();
        codResponse.put("code", "0");
        codResponse.put("response", "Fatal error");

        return codResponse;
    }
}
