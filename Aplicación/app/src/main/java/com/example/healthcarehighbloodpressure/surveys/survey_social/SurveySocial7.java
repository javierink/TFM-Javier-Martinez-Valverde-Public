package com.example.healthcarehighbloodpressure.surveys.survey_social;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
import com.example.healthcarehighbloodpressure.surveys.survey_diet.SurveyDiet14;
import com.example.healthcarehighbloodpressure.surveys.survey_physical_state.Survey_physical_state_7;

public class SurveySocial7 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private CheckBox checkbox5;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_social7);

        bundle = this.getIntent().getExtras();

        //Obtain a reference to the interface elements
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox)findViewById(R.id.checkbox3);
        checkbox4 = (CheckBox)findViewById(R.id.checkbox4);
        checkbox5 = (CheckBox)findViewById(R.id.checkbox5);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox4.setChecked(false);
                checkbox5.setChecked(false);
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox3.setChecked(false);
                checkbox4.setChecked(false);
                checkbox5.setChecked(false);
            }
        });

        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
                checkbox4.setChecked(false);
                checkbox5.setChecked(false);
            }
        });

        checkbox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox5.setChecked(false);
            }
        });

        checkbox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox4.setChecked(false);
            }
        });
    }

    public void finish(View v) {

        boolean finish = false;

        if(checkbox1.isChecked()){

            bundle.putString("7", "1");

            finish = true;

        }else if(checkbox2.isChecked()){

            bundle.putString("7", "2");

            finish = true;

        }else if(checkbox3.isChecked()){

            bundle.putString("7", "3");

            finish = true;

        }else if(checkbox4.isChecked()){

            bundle.putString("7", "4");

            finish = true;

        }else if(checkbox5.isChecked()){

            bundle.putString("7", "5");

            finish = true;

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

        if(finish && LoginManager.getInstance().isLogin()){

            // Call this method to send the data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 4);

            Intent intent = new Intent(SurveySocial7.this, MainActivity.class);
            startActivity(intent);

        }

    }
}
