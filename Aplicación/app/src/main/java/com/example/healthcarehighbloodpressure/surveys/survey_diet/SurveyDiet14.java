package com.example.healthcarehighbloodpressure.surveys.survey_diet;

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

public class SurveyDiet14 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_diet14);

        bundle = this.getIntent().getExtras();

        //Obtain a reference to the interface elements
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox1.setChecked(false);
            }
        });
    }

    public void finish(View v) {

        boolean finish = false;

        if(checkbox1.isChecked()){

            bundle.putString("14", "1");

            finish = true;

        }else if(checkbox2.isChecked()){

            bundle.putString("14", "0");

            finish = true;

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

        //The survey is finished, must send the data to the server
        if(finish && LoginManager.getInstance().isLogin()){

            // Send the data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 2);

            Intent intent = new Intent(SurveyDiet14.this, MainActivity.class);
            startActivity(intent);

        }

    }
}
