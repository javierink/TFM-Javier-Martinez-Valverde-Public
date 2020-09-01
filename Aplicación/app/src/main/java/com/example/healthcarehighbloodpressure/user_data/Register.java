package com.example.healthcarehighbloodpressure.user_data;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.DatePickerFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Class of the Register activity, manage the user register
 */
public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    private EditText editTextUsername;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText birthDate;
    private Spinner spinnerSex;
    private Spinner spinnerCountryBirth;
    private EditText editTextRegion;
    private EditText editTextCity;
    private EditText editTextPostal;
    private Spinner spinnerCountryResidence;
    private EditText editTextAddress;
    private EditText editTextEmail;
    private Spinner spinnerStudyLevel;
    private EditText editTextPassword;
    private EditText editTextRepeat;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Prepares the selection of the date of birth
        birthDate = (EditText) findViewById(R.id.birthDate);

        //Implement the event by clicking the button
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();
            }
        });

        //Prepares the selection of countrys, ethnicity and studies
        spinnerSex = (Spinner)findViewById(R.id.spinnerSex);
        spinnerCountryBirth = (Spinner)findViewById(R.id.spinnerCountry1);
        spinnerCountryResidence = (Spinner)findViewById(R.id.spinnerCountry2);
        spinnerStudyLevel = (Spinner)findViewById(R.id.spinnerStudyLevel);

        // Save countries in a list for the spinner
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);

        // Create the adapters to the spinners

        ArrayAdapter<String> adaptadorCountry =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, countries);

        String strSpinnerSex[] = getString(R.string.Register_listGender).split(",");

        ArrayAdapter<String> adaptadorSex =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, strSpinnerSex);

        String strSpinnerStudy[] = getString(R.string.user_data_listStudiesLevel).split(",");

        ArrayAdapter<String> adaptadorStudies =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, strSpinnerStudy);


        adaptadorSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptadorCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptadorStudies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Make the contents in the spinners
        spinnerSex.setAdapter(adaptadorSex);
        spinnerCountryBirth.setAdapter(adaptadorCountry);
        spinnerCountryResidence.setAdapter(adaptadorCountry);
        spinnerStudyLevel.setAdapter(adaptadorStudies);

        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextLastName = (EditText)findViewById(R.id.editTextLastName);
        editTextRegion = (EditText)findViewById(R.id.editTextRegion);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        editTextPostal = (EditText)findViewById(R.id.editTextPostal);
        editTextAddress = (EditText)findViewById(R.id.editTextAddress);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextRepeat = (EditText)findViewById(R.id.editTextRepeatPassword);
        textViewError = (TextView)findViewById(R.id.textViewError);

        textViewError.setVisibility(View.INVISIBLE);


    }

    /**
     * Shows a dialog window in the activity
     */
    public void showAlertDialogSuccessRegister() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        // Load a xml for the dialog window
        dialog.setContentView(R.layout.popup_text_button);

        // Change the text of the dialog window
        TextView text = (TextView) dialog.findViewById(R.id.textView);
        text.setText(getString(R.string.server_success_register));

        // Change the button text of the dialog window
        Button button = (Button) dialog.findViewById(R.id.button);
        button.setText(getString(R.string.general_exit));

        // Program the function of the button of the dialog window
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    /**
     * Method to send the register to server
     * @param v View to call the method form the activity view
     */
    public void finish(View v){

        textViewError.setVisibility(View.INVISIBLE);

        // Save the data from the view

        String editTextUsernameContent = editTextUsername.getText().toString();
        String editTextNameContent = editTextName.getText().toString();
        String editTextLastNameContent = editTextLastName.getText().toString();
        String birthDateContent = birthDate.getText().toString();

        // You need to take the position and a enumeration in English because the server only understands English
        String genderString = getString(R.string.server_gender_enumeration);
        String[] separated = genderString.split(",");
        String spinnerSexContent = separated[spinnerSex.getSelectedItemPosition()];

        String spinnerCountry1Content = spinnerCountryBirth.getSelectedItem().toString();

        String editTextRegionContent = editTextRegion.getText().toString();
        String editTextCityContent = editTextCity.getText().toString();
        String editTextPostalContent = editTextPostal.getText().toString();

        String spinnerCountryContent = spinnerCountryResidence.getSelectedItem().toString();


        String editTextAddressContent = editTextAddress.getText().toString();
        String editTextEmailContent = editTextEmail.getText().toString();

        // You need to take the position and a enumeration in English because the server only understands English
        String studyString = getString(R.string.server_study_level_enumeration);
        separated = studyString.split(",");
        String spinnerStudyContent = separated[spinnerStudyLevel.getSelectedItemPosition()];

        String editTextPasswordContent = editTextPassword.getText().toString();
        String editTextRepeatContent = editTextRepeat.getText().toString();

        if(!editTextUsernameContent.matches("") &&
                !editTextNameContent.matches("") &&
                !editTextLastNameContent.matches("") &&
                !birthDateContent.matches("") &&
                !spinnerSexContent.matches("") &&
                !spinnerCountry1Content.matches("") &&
                !editTextRegionContent.matches("") &&
                !editTextCityContent.matches("") &&
                !editTextPostalContent.matches("") &&
                !spinnerCountryContent.matches("") &&
                !editTextAddressContent.matches("") &&
                !editTextEmailContent.matches("") &&
                !spinnerStudyContent.matches("") &&
                !editTextPasswordContent.matches("") &&
                !editTextRepeatContent.matches("")){
            // If the content of the fields isn't void

            // Check the username with a regex
            if(!editTextUsernameContent.matches(getString(R.string.server_regex_username))){
                textViewError.setText(getString(R.string.user_data_bad_username));
                textViewError.setVisibility(View.VISIBLE);
                return;
            }

            // Check the postal code with a regex
            if(!editTextPostalContent.matches(getString(R.string.server_regex_zp))){
                textViewError.setText(getString(R.string.user_data_bad_zp));
                textViewError.setVisibility(View.VISIBLE);
                return;
            }

            // Check the email with a regex
            if(!editTextEmailContent.matches(getString(R.string.server_regex_email))){
                textViewError.setText(getString(R.string.user_data_bad_email));
                textViewError.setVisibility(View.VISIBLE);
                return;
            }

            // Check the two passwords is the same
            if(!editTextPasswordContent.equals(editTextRepeatContent)){
                textViewError.setText(getString(R.string.user_data_bad_same_pass));
                textViewError.setVisibility(View.VISIBLE);
                return;
            }

            // Transforms the date to UNIX millis
            long millisDate = 0;

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                formatter.setLenient(false);

                String oldTime = birthDateContent;
                Date date = null;

                date = formatter.parse(oldTime);
                millisDate = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String passSHA =  shaSecure(editTextPasswordContent);

            //TODO manage this situation
            if(passSHA == null) return;

            String code = DataManager.getInstance().sendDataRegister(editTextUsernameContent, passSHA, editTextNameContent, editTextLastNameContent,
                    millisDate, spinnerSexContent, spinnerCountry1Content, editTextRegionContent,
                    editTextCityContent, Integer.parseInt(editTextPostalContent), spinnerCountryContent, editTextAddressContent,
                    editTextEmailContent, spinnerStudyContent);

            switch(code) {
                case "201":
                    // Show a dialog view for the successful register
                    showAlertDialogSuccessRegister();
                    break;
                case "406":
                    // If the code is 406, the username is yet register
                    textViewError.setText(getString(R.string.server_error_register_user));
                    textViewError.setVisibility(View.VISIBLE);
                    break;
                default:
                    //In other are a error with the server
                    textViewError.setText(getString(R.string.server_error));
                    textViewError.setVisibility(View.VISIBLE);
                    break;
            }

        }else{
            // Some fields is empty
            textViewError.setText(getString(R.string.error_continue_survey));
            textViewError.setVisibility(View.VISIBLE);
        }
    }

    //Source: https://stackoverflow.com/questions/46510338/sha-512-hashing-with-android
    private String shaSecure(String pass){

        MessageDigest md = null;
        StringBuilder sb = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(pass.getBytes());
            sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Show a date view to collected the date
     */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month + 1) + "-" + year;
                birthDate.setText(selectedDate);
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }
}
