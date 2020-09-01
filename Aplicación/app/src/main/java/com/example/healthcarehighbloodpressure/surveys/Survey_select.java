package com.example.healthcarehighbloodpressure.surveys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.surveys.survey_alcohol.SurveyAlcohol;
import com.example.healthcarehighbloodpressure.surveys.survey_depression.SurveyDepression1;
import com.example.healthcarehighbloodpressure.surveys.survey_diet.SurveyDiet1;
import com.example.healthcarehighbloodpressure.surveys.survey_medication.SurveyMedication;
import com.example.healthcarehighbloodpressure.surveys.survey_physical_attributes.SurveyPhysicalAttributes;
import com.example.healthcarehighbloodpressure.surveys.survey_physical_state.Survey_physical_state_1;
import com.example.healthcarehighbloodpressure.surveys.survey_smoking.SurveySmoking;
import com.example.healthcarehighbloodpressure.surveys.survey_social.SurveySocial1;
import com.example.healthcarehighbloodpressure.surveys.survey_stress.SurveyStress1;

/**
 * Activity class for select the survey to do. All method is for a transition to the next activity.
 */
public class Survey_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_select);

    }

    public void openSurveyPhysical(View v){

        Intent intent = new Intent(Survey_select.this, Survey_physical_state_1.class);

        startActivity(intent);
    }

    public void openSurveyDiet(View v){

        Intent intent = new Intent(Survey_select.this, SurveyDiet1.class);

        startActivity(intent);
    }

    public void openSurveySocial(View v){

        Intent intent = new Intent(Survey_select.this, SurveySocial1.class);

        startActivity(intent);
    }

    public void openSurveyStress(View v){

        Intent intent = new Intent(Survey_select.this, SurveyStress1.class);

        startActivity(intent);
    }

    public void openSurveyDepression(View v){

        Intent intent = new Intent(Survey_select.this, SurveyDepression1.class);

        startActivity(intent);
    }

    public void openSurveyPhysicalAttributes(View v){

        Intent intent = new Intent(Survey_select.this, SurveyPhysicalAttributes.class);

        startActivity(intent);
    }

    public void openSurveySmoking(View v){

        Intent intent = new Intent(Survey_select.this, SurveySmoking.class);

        startActivity(intent);
    }

    public void openSurveyAlcohol(View v){

        Intent intent = new Intent(Survey_select.this, SurveyAlcohol.class);

        startActivity(intent);
    }

    // In case of the medication survey is necessary put the type of survey in a bundle.
    public void openSurveyOccasionalMedication(View v){

        Intent intent = new Intent(Survey_select.this, SurveyMedication.class);
        Bundle bundle = new Bundle();

        bundle.putInt("type", 3);

        //Add the information to the intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

    // In case of the medication survey is necessary put the type of survey in a bundle.
    public void openSurveyChronicMedication(View v){

        Intent intent = new Intent(Survey_select.this, SurveyMedication.class);
        Bundle bundle = new Bundle();

        bundle.putInt("type", 0);

        //Add the information to the intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void exitToMain(View v){
        Intent intent = new Intent(Survey_select.this, MainActivity.class);

        startActivity(intent);
    }
}
