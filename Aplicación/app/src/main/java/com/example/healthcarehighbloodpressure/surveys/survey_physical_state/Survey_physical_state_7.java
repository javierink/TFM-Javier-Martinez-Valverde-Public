package com.example.healthcarehighbloodpressure.surveys.survey_physical_state;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

public class Survey_physical_state_7 extends AppCompatActivity {
    private static final String TAG = "Survey_physical_state_7";

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private EditText editText1;
    private EditText editText2;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_state_7);

        bundle = this.getIntent().getExtras();
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        textViewError = (TextView)findViewById(R.id.textViewError);

        editText1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "16")});
        editText2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "960")});
        textViewError.setVisibility(View.INVISIBLE);

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                editText1.setText("");
                editText2.setText("");

                if (editText1.isFocused()|| editText2.isFocused()) {
                    editText1.setCursorVisible(false);
                    editText2.setCursorVisible(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                editText1.setText("");
                editText2.setText("");

                if (editText1.isFocused()|| editText2.isFocused()) {
                    editText1.setCursorVisible(false);
                    editText2.setCursorVisible(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

    }

    public void finish(View v) {

        String text1 = editText1.getText().toString();
        String text2 = editText2.getText().toString();
        Boolean finish = false;

        if(checkbox1.isChecked()){

            bundle.putString("7", "-1");

            finish = true;

        }else if(checkbox2.isChecked()){

            bundle.putString("7", "-2");

            finish = true;

        }else if((!text1.matches(""))&&(!text2.matches(""))){

            int total;
            int h = 0;
            int m = 0;

            try {
                h = Integer.parseInt(text1);
                m = Integer.parseInt(text2);
            } catch(NumberFormatException nfe) {
                Log.e("ConvertType", "Error in convert of string to int in physical poll");
            }

            total = ((h*60)+m);

            bundle.putString("7", Integer.toString(total));

            finish = true;

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

        //The survey is finished, must send the data to the server
        if(finish && LoginManager.getInstance().isLogin()){

            //Send data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 1);
            Intent intent = new Intent(Survey_physical_state_7.this, MainActivity.class);
            startActivity(intent);

        }
    }
}
