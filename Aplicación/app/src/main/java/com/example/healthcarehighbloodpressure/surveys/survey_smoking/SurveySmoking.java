package com.example.healthcarehighbloodpressure.surveys.survey_smoking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;
/**
 * Activity class for the smoking survey
 */
public class SurveySmoking extends AppCompatActivity {

    private EditText editCigarettes;
    private EditText editElectronic;
    private EditText editOther;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_smoking);

        //Obtain a reference to the interface elements
        editCigarettes = (EditText)findViewById(R.id.editCigarettes);
        editElectronic = (EditText)findViewById(R.id.editElectronic);
        editOther = (EditText)findViewById(R.id.editOther);
        textViewError = (TextView)findViewById(R.id.textViewError);

        // Limit a range of numbers for the info in the editText
        editCigarettes.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editElectronic.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editOther.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        textViewError.setVisibility(View.INVISIBLE);
    }

    public void finish(View v) {

        String editCigarettesText = editCigarettes.getText().toString();
        String editElectronicText = editElectronic.getText().toString();
        String editOtherText = editOther.getText().toString();

        // Some field can be empty but at least one
        if((!editCigarettesText.matches(""))||(!editElectronicText.matches(""))||(!editOtherText.matches(""))){

            int intCigarettes = ((editCigarettesText.equals("")) ? 0 : Integer.parseInt(editCigarettesText));
            int intElectronic = ((editElectronicText.equals("")) ? 0 : Integer.parseInt(editElectronicText));
            int intOther = ((editOtherText.equals("")) ? 0 : Integer.parseInt(editOtherText));

            Bundle bundle = new Bundle();
            bundle.putInt("cigarettes", intCigarettes);
            bundle.putInt("electronic", intElectronic);
            bundle.putInt("other", intOther);

            // Send the data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 9);
            Intent intent = new Intent(SurveySmoking.this, MainActivity.class);
            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

    }
}
