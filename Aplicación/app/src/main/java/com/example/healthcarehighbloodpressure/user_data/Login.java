package com.example.healthcarehighbloodpressure.user_data;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class of the Login activity, manage the user login
 */
public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    private TextView textViewErrorFields;
    private ProgressBar progressBar;
    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText)findViewById(R.id.username);
        editTextPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.loading);

        textViewErrorFields = (TextView)findViewById(R.id.textViewErrorFields);

        textViewErrorFields.setVisibility(View.INVISIBLE);
    }


    /**
     * Shows a dialog window in the activity
     */
    public void showAlertDialogSuccessLogin() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        // Load a xml for the dialog window
        dialog.setContentView(R.layout.popup_text_button);

        // Change the text of the dialog window
        TextView text = (TextView) dialog.findViewById(R.id.textView);
        text.setText(getString(R.string.server_success_log));

        // Change the button text of the dialog window
        Button button = (Button) dialog.findViewById(R.id.button);
        button.setText(getString(R.string.general_exit));

        // Program the function of the button of the dialog window
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    /**
     * Method to iniciates the login
     * @param v View to call the method form the activity view
     */
    public void login(View v) {

        // Check if the fields of the view are fine. Regex for the username and void field for the pass
        if(editTextUsername.getText().toString().matches(getString(R.string.server_regex_username)) && !editTextPassword.getText().toString().matches("")){

            // Set visible the process bar and delete the error text view
            progressBar.setVisibility(View.VISIBLE);
            textViewErrorFields.setVisibility(View.INVISIBLE);

            String passSHA = shaSecure(editTextPassword.getText().toString());

            //TODO manage this situation
            if(passSHA == null) return;

            // Use de DataManger for send the data login. Return de code of the communication
            String code = DataManager.getInstance().sendDataRegister(editTextUsername.getText().toString(), passSHA);

            // Depending on the code of the communication
            switch(code) {
                case "200":
                    // Show a dialog view for the successful login
                    showAlertDialogSuccessLogin();
                    break;
                case "404":
                    // In case of the 404 code the server dont have a user with the username
                    textViewErrorFields.setText(getString(R.string.server_error_login_user));
                    textViewErrorFields.setVisibility(View.VISIBLE);
                    break;
                case "401":
                    // In case of the 404 code, the password is incorrect
                    textViewErrorFields.setText(getString(R.string.server_error_login_password));
                    textViewErrorFields.setVisibility(View.VISIBLE);
                    break;
                default:
                    // In other case are a error with the server
                    textViewErrorFields.setText(getString(R.string.server_error));
                    textViewErrorFields.setVisibility(View.VISIBLE);
                    break;
            }

            progressBar.setVisibility(View.GONE);

        }else{
            textViewErrorFields.setText(getString(R.string.error_continue_survey));
            textViewErrorFields.setVisibility(View.VISIBLE);
        }
    }

    //Source: https://stackoverflow.com/questions/46510338/sha-512-hashing-with-android

    /**
     * Transform a string in a SHA512
     * @param pass in string
     * @return out string in SHA512
     */
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
     * Change the view to the MainActivity
     * @param v View for call from the activity view
     */
    public void changeToMain(View v){

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}
