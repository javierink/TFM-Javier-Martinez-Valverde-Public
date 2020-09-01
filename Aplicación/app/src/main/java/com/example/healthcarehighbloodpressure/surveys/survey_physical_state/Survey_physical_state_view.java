package com.example.healthcarehighbloodpressure.surveys.survey_physical_state;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Survey_physical_state_view extends AppCompatActivity {

    private TextView text1n;
    private TextView text1a;
    private TextView text2n;
    private TextView text2a;
    private TextView text3n;
    private TextView text3a;
    private TextView text4n;
    private TextView text4a;
    private TextView text5n;
    private TextView text5a;
    private TextView text6n;
    private TextView text6a;
    private TextView text7n;
    private TextView text7a;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_physical_state_view);

        textViewError = (TextView)findViewById(R.id.textViewError);
        textViewError.setVisibility(View.INVISIBLE);

        LinkedList<String> list = new LinkedList<String>();

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(openFileInput("pollPhysic.txt")));

            String line;

            do{
                line = fin.readLine();
                list.addLast(line);
            }while(line != null);

            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }

        if(!list.isEmpty()){
            text1n = (TextView)findViewById(R.id.text1n);;
            text1a = (TextView)findViewById(R.id.text1a);;
            text2n = (TextView)findViewById(R.id.text2n);;
            text2a = (TextView)findViewById(R.id.text2a);;
            text3n = (TextView)findViewById(R.id.text3n);;
            text3a = (TextView)findViewById(R.id.text3a);;
            text4n = (TextView)findViewById(R.id.text4n);;
            text4a = (TextView)findViewById(R.id.text4a);;
            text5n = (TextView)findViewById(R.id.text5n);;
            text5a = (TextView)findViewById(R.id.text5a);;
            text6n = (TextView)findViewById(R.id.text6n);;
            text6a = (TextView)findViewById(R.id.text6a);;
            text7n = (TextView)findViewById(R.id.text7n);;
            text7a = (TextView)findViewById(R.id.text7a);;

            text1n.setText(getString(R.string.survey_physical_state_1_TextView));
            text2n.setText(getString(R.string.survey_physical_state_2_TextView));
            text3n.setText(getString(R.string.survey_physical_state_3_TextView1));
            text4n.setText(getString(R.string.survey_physical_state_4_TextView));
            text5n.setText(getString(R.string.survey_physical_state_5_TextView1));
            text6n.setText(getString(R.string.survey_physical_state_6_TextView));
            text7n.setText(getString(R.string.survey_physical_state_7_TextView1));

            //Responses to the survey are completed

            String dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text1a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text1a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text1a.setText(getString(R.string.survey_physical_state_1_TxtImagenHint)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text2a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text2a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text2a.setText(getString(R.string.survey_physical_state_2_TxtImagenHint2)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text3a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text3a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text3a.setText(getString(R.string.survey_physical_state_3_TxtImagenHint)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text4a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text4a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text4a.setText(getString(R.string.survey_physical_state_4_TxtImagenHint2)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text5a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text5a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text5a.setText(getString(R.string.survey_physical_state_5_TxtImagenHint)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text6a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text6a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    text6a.setText(getString(R.string.survey_physical_state_6_TxtImagenHint2)
                            + getString(R.string.two_point) + " " + dataPop);
            }

            dataPop = list.pop();

            switch (dataPop){
                case "-1" :
                    text7a.setText(getString(R.string.survey_not_sure));
                    break;
                case "-2" :
                    text7a.setText(getString(R.string.survey_refused));
                    break;
                default:
                    String[] separated = dataPop.split(";");
                    text7a.setText(getString(R.string.survey_physical_state_7_TxtImagenHint2)
                            + getString(R.string.two_point) + " " + dataPop);
            }

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }

    }

    public void exitToMain(View v){
        Intent intent = new Intent(Survey_physical_state_view.this, MainActivity.class);

        startActivity(intent);
    }
}
