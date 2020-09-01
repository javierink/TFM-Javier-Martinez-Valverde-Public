package com.example.healthcarehighbloodpressure.surveys.survey_physical_state;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;
/**
 * First activity class for the physical state survey
 */
public class Survey_physical_state_1 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private EditText editText;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_state_1);

        //Obtain a reference to the interface elements
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        editText = (EditText)findViewById(R.id.editText);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We limit the values of the editText element
        editText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "7")});
        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);

        //Configure the listener of the view elements to avoid multianswers
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                editText.setText("");

                if (editText.isFocused()) {
                    editText.setCursorVisible(false);
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
                editText.setText("");

                if (editText.isFocused()) {
                    editText.setCursorVisible(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

    }

    //Program the method that the button will call for next question of the survey
    public void nextQuestion(View v) {
        String text = editText.getText().toString();

        //Create the Intent
        Intent intent;
        Bundle bundle;

        if(checkbox1.isChecked()){
            //The intent is filled in
            intent = new Intent(Survey_physical_state_1.this, Survey_physical_state_3.class);

            //Create the bundle and put in it the answers to the current question
            bundle = new Bundle();
            bundle.putString("1", "-1");
            bundle.putString("2", "-1");

            //Add the information to the intent
            intent.putExtras(bundle);

            //Initiate the new activity
            startActivity(intent);

        }else if(checkbox2.isChecked()){

            intent = new Intent(Survey_physical_state_1.this, Survey_physical_state_3.class);

            bundle = new Bundle();
            bundle.putString("1", "-2");
            bundle.putString("2", "-2");

            intent.putExtras(bundle);

            startActivity(intent);

            //In the case of taking the text, it must be checked if the value 0 has been entered as a special case
        }else if(!text.matches("")){

            if(text.matches("0")){
                intent = new Intent(Survey_physical_state_1.this, Survey_physical_state_3.class);

                bundle = new Bundle();
                bundle.putString("1", "0");
                bundle.putString("2", "0");

                intent.putExtras(bundle);

                startActivity(intent);
            }else{
                intent = new Intent(Survey_physical_state_1.this, Survey_physical_state_2.class);

                bundle = new Bundle();
                bundle.putString("1", text);

                intent.putExtras(bundle);

                startActivity(intent);
            }

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }

}
