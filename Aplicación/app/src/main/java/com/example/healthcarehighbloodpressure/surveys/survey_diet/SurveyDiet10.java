package com.example.healthcarehighbloodpressure.surveys.survey_diet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.R;

public class SurveyDiet10 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_diet10);

        bundle = this.getIntent().getExtras();

        //Obtain a reference to the interface elements
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox)findViewById(R.id.checkbox3);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox3.setChecked(false);
            }
        });

        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });
    }

    //Program the method that the button will call for next question of the survey
    public void nextQuestion(View v) {
        //Create the Intent
        Intent intent;

        if(checkbox1.isChecked()){
            //The intent is filled in
            intent = new Intent(SurveyDiet10.this, SurveyDiet11.class);

            //Put in it the answers to the current question
            bundle.putString("10", "1");

            //Add the information to the intent
            intent.putExtras(bundle);

            //Initiate the new activity
            startActivity(intent);

        }else if(checkbox2.isChecked()){

            intent = new Intent(SurveyDiet10.this, SurveyDiet11.class);

            bundle.putString("10", "0");

            intent.putExtras(bundle);

            startActivity(intent);

        }else if(checkbox3.isChecked()){

            intent = new Intent(SurveyDiet10.this, SurveyDiet11.class);

            bundle.putString("10", "-1");

            intent.putExtras(bundle);

            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}
