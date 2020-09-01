package com.example.healthcarehighbloodpressure.surveys.survey_alcohol;

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
import com.example.healthcarehighbloodpressure.surveys.survey_physical_attributes.SurveyPhysicalAttributes;
/**
 * Activity class for the alcohol survey
 */
public class SurveyAlcohol extends AppCompatActivity {

    private EditText editBeer;
    private EditText editWine;
    private EditText editWineMix;
    private EditText editOtherFermented;
    private EditText editDestilled;
    private EditText editDestilledMix;
    private EditText editOther;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_alcohol);

        //Obtain a reference to the interface elements
        editBeer = (EditText)findViewById(R.id.editBeer);
        editWine = (EditText)findViewById(R.id.editWine);
        editWineMix = (EditText)findViewById(R.id.editWineMix);
        editOtherFermented = (EditText)findViewById(R.id.editOtherFermented);
        editDestilled = (EditText)findViewById(R.id.editDestilled);
        editDestilledMix = (EditText)findViewById(R.id.editDestilledMix);
        editOther = (EditText)findViewById(R.id.editOther);
        textViewError = (TextView)findViewById(R.id.textViewError);

        // Limit a range of numbers for the info in the editText
        editBeer.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editWine.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editWineMix.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editOtherFermented.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editDestilled.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        editDestilledMix.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "250")});
        editOther.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "250")});
        textViewError.setVisibility(View.INVISIBLE);
    }

    public void finish(View v) {

        String editBeerText = editBeer.getText().toString();
        String editWineText = editWine.getText().toString();
        String editWineMixText = editWineMix.getText().toString();
        String editOtherFermentedText = editOtherFermented.getText().toString();
        String editDestilledText = editDestilled.getText().toString();
        String editDestilledMixText = editDestilledMix.getText().toString();
        String editOtherText = editOther.getText().toString();

        // Some field can be empty but at least one
        if((!editBeerText.matches(""))||(!editWineText.matches(""))||(!editWineMixText.matches(""))
                ||(!editOtherFermentedText.matches(""))||(!editDestilledText.matches(""))||(!editDestilledMixText.matches(""))
                ||(!editOtherText.matches(""))){

            int intBeer = ((editBeerText.equals("")) ? 0 : Integer.parseInt(editBeerText));
            int intWine = ((editWineText.equals("")) ? 0 : Integer.parseInt(editWineText));
            int intWineMix = ((editWineMixText.equals("")) ? 0 : Integer.parseInt(editWineMixText));
            int intOtherFermented = ((editOtherFermentedText.equals("")) ? 0 : Integer.parseInt(editOtherFermentedText));
            int intDestilled = ((editDestilledText.equals("")) ? 0 : Integer.parseInt(editDestilledText));
            int intDestilledMix = ((editDestilledMixText.equals("")) ? 0 : Integer.parseInt(editDestilledMixText));
            int intOther = ((editOtherText.equals("")) ? 0 : Integer.parseInt(editOtherText));

            Bundle bundle = new Bundle();
            bundle.putInt("beer", intBeer);
            bundle.putInt("wine", intWine);
            bundle.putInt("wineMix", intWineMix);
            bundle.putInt("otherFermented", intOtherFermented);
            bundle.putInt("destilled", intDestilled);
            bundle.putInt("destilledMix", intDestilledMix);
            bundle.putInt("other", intOther);

            // Send the data to the server
            DataManager.getInstance().sendDataSurvey(bundle, 10);
            Intent intent = new Intent(SurveyAlcohol.this, MainActivity.class);
            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

    }
}
