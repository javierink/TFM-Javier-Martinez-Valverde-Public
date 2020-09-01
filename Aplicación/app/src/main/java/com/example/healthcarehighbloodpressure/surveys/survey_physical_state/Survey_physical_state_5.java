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

public class Survey_physical_state_5 extends AppCompatActivity {

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private EditText editText;
    private TextView textViewError;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_state_5);

        bundle = this.getIntent().getExtras();
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        editText = (EditText)findViewById(R.id.editText);
        textViewError = (TextView)findViewById(R.id.textViewError);

        editText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "7")});
        textViewError.setVisibility(View.INVISIBLE);

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

    public void nextQuestion(View v) {

        Intent intent;

        String text = editText.getText().toString();

        if(checkbox1.isChecked()){

            intent = new Intent(Survey_physical_state_5.this, Survey_physical_state_7.class);

            bundle.putString("5", "-1");
            bundle.putString("6", "-1");

            intent.putExtras(bundle);

            startActivity(intent);
        }else if(checkbox2.isChecked()){

            intent = new Intent(Survey_physical_state_5.this, Survey_physical_state_7.class);

            bundle.putString("5", "-2");
            bundle.putString("6", "-2");

            intent.putExtras(bundle);

            startActivity(intent);
        }else if(!text.matches("")){

            if(text.matches("0")){
                intent = new Intent(Survey_physical_state_5.this, Survey_physical_state_7.class);

                bundle.putString("5", "0");
                bundle.putString("6", "0");

                intent.putExtras(bundle);

                startActivity(intent);
            }else{
                intent = new Intent(Survey_physical_state_5.this, Survey_physical_state_6.class);

                bundle.putString("5", text);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}
