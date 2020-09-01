package com.example.healthcarehighbloodpressure.surveys.survey_physical_state;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;

public class Survey_physical_state_6 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private EditText editText1;
    private EditText editText2;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_state_6);

        bundle = this.getIntent().getExtras();
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        textViewError = (TextView)findViewById(R.id.textViewError);

        editText1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "16")});
        editText2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "960")});
        textViewError.setVisibility(View.INVISIBLE);

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                checkbox1.setChecked(false);
                checkbox2.setChecked(false);
            }
        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox2.setChecked(false);
                editText1.setText("");
                editText2.setText("");

                if (editText1.isFocused()|| editText2.isFocused()) {
                    editText1.setCursorVisible(false);
                    editText2.setCursorVisible(false);
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
                editText1.setText("");
                editText2.setText("");

                if (editText1.isFocused()|| editText2.isFocused()) {
                    editText1.setCursorVisible(false);
                    editText2.setCursorVisible(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

    }

    public void nextQuestion(View v) {

        Intent intent;

        String text1 = editText1.getText().toString();
        String text2 = editText2.getText().toString();

        if(checkbox1.isChecked()){

            intent = new Intent(Survey_physical_state_6.this, Survey_physical_state_7.class);

            bundle.putString("6", "-1");

            intent.putExtras(bundle);

            startActivity(intent);
        }else if(checkbox2.isChecked()){

            intent = new Intent(Survey_physical_state_6.this, Survey_physical_state_7.class);

            bundle.putString("6", "-2");

            intent.putExtras(bundle);

            startActivity(intent);
        }else if((!text1.matches(""))&&(!text2.matches(""))){

            intent = new Intent(Survey_physical_state_6.this, Survey_physical_state_7.class);

            int total;
            int h = 0;
            int m = 0;

            try {
                h = Integer.parseInt(text1);
                m = Integer.parseInt(text2);
            } catch(NumberFormatException nfe) {
                Log.e("ConvertType", "Error in convert of string to int in physical poll");
            }

            total = ((h*60)+m);

            bundle.putString("6", Integer.toString(total));

            intent.putExtras(bundle);

            startActivity(intent);
        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}
