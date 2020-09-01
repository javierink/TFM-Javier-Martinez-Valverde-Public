package com.example.healthcarehighbloodpressure.surveys.survey_physical_attributes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
import com.example.healthcarehighbloodpressure.surveys.survey_physical_state.Survey_physical_state_7;
/**
 * Activity class for the physical attributes survey
 */
public class SurveyPhysicalAttributes extends AppCompatActivity {

    private EditText editWeight;
    private EditText editHeight;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_attributes);

        //Obtain a reference to the interface elements
        editWeight = (EditText)findViewById(R.id.editWeight);
        editHeight = (EditText)findViewById(R.id.editHeight);
        textViewError = (TextView)findViewById(R.id.textViewError);

        // Limit a range of numbers for the info in the editText
        editWeight.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "250")});
        editHeight.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "250")});
        textViewError.setVisibility(View.INVISIBLE);
    }

    public void finish(View v) {

        String editWeightText = editWeight.getText().toString();
        String editHeightText = editHeight.getText().toString();

        if((!editWeightText.matches(""))&&(!editHeightText.matches(""))){

            Bundle bundle = new Bundle();
            bundle.putInt("weight", Integer.parseInt(editWeightText));
            bundle.putInt("height", Integer.parseInt(editHeightText));

            // Sent the info to the server
            DataManager.getInstance().sendDataSurvey(bundle, 8);
            Intent intent = new Intent(SurveyPhysicalAttributes.this, MainActivity.class);
            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

    }
}
