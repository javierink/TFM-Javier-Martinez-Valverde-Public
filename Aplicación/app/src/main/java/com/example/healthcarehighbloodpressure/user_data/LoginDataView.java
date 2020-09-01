package com.example.healthcarehighbloodpressure.user_data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Activity class for view the login information
 */
public class LoginDataView extends AppCompatActivity {

    private TextView textUsername;
    private TextView textName;
    private TextView textLastName;
    private TextView textDate;
    private TextView textGender;
    private TextView textBirthCountry;
    private TextView textRegion;
    private TextView textCity;
    private TextView textZipCode;
    private TextView textResidenceCountry;
    private TextView textAddress;
    private TextView textEmail;
    private TextView textEducational;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_data_view);

        if(LoginManager.getInstance().isLogin()){

            textUsername = (TextView)findViewById(R.id.textSaveUsername);
            textName = (TextView)findViewById(R.id.textSaveName);
            textLastName = (TextView)findViewById(R.id.textSaveSurname);
            textDate = (TextView)findViewById(R.id.textSaveBirthdate);
            textGender = (TextView)findViewById(R.id.textSaveGender);
            textBirthCountry = (TextView)findViewById(R.id.textSaveBirthcountry);
            textRegion = (TextView)findViewById(R.id.textSaveRegion);
            textCity = (TextView)findViewById(R.id.textSaveCity);
            textZipCode = (TextView)findViewById(R.id.textSaveZipCode);
            textResidenceCountry = (TextView)findViewById(R.id.textSaveHomeCountry);
            textAddress = (TextView)findViewById(R.id.textSaveAddress);
            textEmail = (TextView)findViewById(R.id.textSaveEmail);
            textEducational = (TextView)findViewById(R.id.textSaveEducational);

            // Get the login file and complete the data of the anctivity (windows)
            try {

                JSONObject json = LoginManager.getInstance().getPersonalDataJson();

                Log.i("Data", json.toString());
                Log.i("Data", json.getString("birthdate"));

                long millis = Long.parseLong(json.getString("birthdate"));;
                String date = getDate(millis, "dd/MM/yyyy");

                textUsername.setText(json.getString("username"));
                textName.setText(json.getString("name"));
                textLastName.setText(json.getString("surname"));
                textDate.setText(date);
                textGender.setText(json.getString("gender"));
                textBirthCountry.setText(json.getString("birthcountry"));
                textRegion.setText(json.getString("region"));
                textCity.setText(json.getString("city"));
                textZipCode.setText(json.getString("zp"));
                textResidenceCountry.setText(json.getString("homecountry"));
                textAddress.setText(json.getString("address"));
                textEmail.setText(json.getString("email"));
                textEducational.setText(json.getString("educationlevel"));

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    /**
     * Change the date format
     * @param milliSeconds Date in illiseconds
     * @param dateFormat Format of the new date
     * @return New date in the format entered in the parameters
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void changeToMain(View v){

        Intent intent = new Intent(LoginDataView.this, MainActivity.class);
        startActivity(intent);
    }
}
