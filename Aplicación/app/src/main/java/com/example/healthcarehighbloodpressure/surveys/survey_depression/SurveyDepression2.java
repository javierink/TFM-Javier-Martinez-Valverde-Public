package com.example.healthcarehighbloodpressure.surveys.survey_depression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.R;

public class SurveyDepression2 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_depression2);

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

    //Program the method that the button will call for next question of the survey
    public void nextQuestion(View v) {
        //Create the Intent
        Intent intent;

        if(checkbox1.isChecked()){
            //The intent is filled in
            intent = new Intent(SurveyDepression2.this, SurveyDepression3.class);

            bundle.putString("2", "1");

            //Add the information to the intent
            intent.putExtras(bundle);

            //Initiate the new activity
            startActivity(intent);

        }else if(checkbox2.isChecked()){

            intent = new Intent(SurveyDepression2.this, SurveyDepression3.class);

            bundle.putString("2", "2");

            intent.putExtras(bundle);

            startActivity(intent);

        }else if(checkbox3.isChecked()){

            intent = new Intent(SurveyDepression2.this, SurveyDepression3.class);

            bundle.putString("2", "3");

            intent.putExtras(bundle);

            startActivity(intent);

        }else if(checkbox4.isChecked()){

            intent = new Intent(SurveyDepression2.this, SurveyDepression3.class);

            bundle.putString("2", "4");

            intent.putExtras(bundle);

            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}
