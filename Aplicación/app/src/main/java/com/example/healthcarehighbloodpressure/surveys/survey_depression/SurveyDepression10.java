package com.example.healthcarehighbloodpressure.surveys.survey_depression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
import com.example.healthcarehighbloodpressure.surveys.survey_stress.SurveyStress10;
import com.example.healthcarehighbloodpressure.surveys.survey_stress.SurveyStress14;

public class SurveyDepression10 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_depression10);

        bundle = this.getIntent().getExtras();

        //Obtain a reference to the interface elements
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox)findViewById(R.id.checkbox3);
        checkbox4 = (CheckBox)findViewById(R.id.checkbox4);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox4.setChecked(false);
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox3.setChecked(false);
                checkbox4.setChecked(false);
            }
        });

        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
                checkbox4.setChecked(false);
            }
        });

        checkbox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
            }
        });
    }

    public void finish(View v) {

        boolean finish = false;

        if(checkbox1.isChecked()){

            bundle.putString("10", "1");

            finish = true;

        }else if(checkbox2.isChecked()){

            bundle.putString("10", "2");

            finish = true;

        }else if(checkbox3.isChecked()){

            bundle.putString("10", "3");

            finish = true;

        }else if(checkbox4.isChecked()){

            bundle.putString("10", "4");

            finish = true;

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

        if(finish && LoginManager.getInstance().isLogin()){

            //Send data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 7);

            Intent intent = new Intent(SurveyDepression10.this, MainActivity.class);
            startActivity(intent);

        }

    }
}
