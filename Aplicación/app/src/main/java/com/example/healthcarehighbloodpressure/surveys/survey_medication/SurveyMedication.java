package com.example.healthcarehighbloodpressure.surveys.survey_medication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//Source: https://stackoverflow.com/questions/17693578/android-how-to-display-2-listviews-in-one-activity-one-after-the-other
/**
 * Activity class for the medication survey
 */
public class SurveyMedication extends AppCompatActivity {

    /**
     * Class for the items of the custom list
     */
    public class Item {
        boolean checked;
        String ItemStringTile;
        String ItemStringSub;
        Item(String t, String s,boolean b){
            ItemStringTile = t;
            ItemStringSub = s;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView textTile;
        TextView textSub;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.medication_custom_list, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.textTile = (TextView) rowView.findViewById(R.id.textViewTitle);
                viewHolder.textSub = (TextView) rowView.findViewById(R.id.textViewSubtitle);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr1 = list.get(position).ItemStringTile;
            viewHolder.textTile.setText(itemStr1);

            final String itemStr2 = list.get(position).ItemStringSub;
            viewHolder.textSub.setText(itemStr2);

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    // Items on each list
    List<Item> itemsNonOpioidAnalgesics;
    ListView listviewNonOpioidAnalgesics;
    ItemsListAdapter myItemsListAdapterNonOpioidAnalgesics;

    List<Item> itemsOpioidAnalgesics;
    ListView listviewOpioidAnalgesics;
    ItemsListAdapter myItemsListAdapterOpioidAnalgesics;

    List<Item> itemsAntiAnginoses;
    ListView listviewAntiAnginoses;
    ItemsListAdapter myItemsListAdapterAntiAnginoses;

    List<Item> itemsAntiArrhythmics;
    ListView listviewAntiArrhythmics;
    ItemsListAdapter myItemsListAdapterAntiArrhythmics;

    List<Item> itemsDiabetes;
    ListView listviewDiabetes;
    ItemsListAdapter myItemsListAdapterDiabetes;

    List<Item> itemsHypertension;
    ListView listviewHypertension;
    ItemsListAdapter myItemsListAdapterHypertension;

    List<Item> itemsAntithrombotics;
    ListView listviewAntithrombotics;
    ItemsListAdapter myItemsListAdapterAntithrombotics;

    List<Item> itemsPsychotropics;
    ListView listviewPsychotropics;
    ItemsListAdapter myItemsListAdapterPsychotropics;

    List<Item> itemsAntihistamines;
    ListView listviewAntihistamines;
    ItemsListAdapter myItemsListAdapterAntihistamines;

    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_medication);

        //Obtain a reference to the interface elements
        listviewNonOpioidAnalgesics = (ListView)findViewById(R.id.listview_non_opioid_analgesics);
        listviewOpioidAnalgesics = (ListView)findViewById(R.id.listview_opioid_analgesics);
        listviewAntiAnginoses = (ListView)findViewById(R.id.listview_anti_anginoses);
        listviewAntiArrhythmics = (ListView)findViewById(R.id.listview_anti_arrhythmics);
        listviewDiabetes = (ListView)findViewById(R.id.listview_diabetes);
        listviewHypertension = (ListView)findViewById(R.id.listview_hypertension);
        listviewAntithrombotics = (ListView)findViewById(R.id.listview_antithrombotics);
        listviewPsychotropics = (ListView)findViewById(R.id.listview_psychotropics);
        listviewAntihistamines = (ListView)findViewById(R.id.listview_antihistamines);
        textViewError = (TextView)findViewById(R.id.textViewError);

        textViewError.setVisibility(View.INVISIBLE);

        //The class of the lists is filled in with the elements
        initItems();

        //The adapter for each list is created

        myItemsListAdapterNonOpioidAnalgesics = new ItemsListAdapter(this, itemsNonOpioidAnalgesics);
        listviewNonOpioidAnalgesics.setAdapter(myItemsListAdapterNonOpioidAnalgesics);

        myItemsListAdapterOpioidAnalgesics = new ItemsListAdapter(this, itemsOpioidAnalgesics);
        listviewOpioidAnalgesics.setAdapter(myItemsListAdapterOpioidAnalgesics);

        myItemsListAdapterAntiAnginoses = new ItemsListAdapter(this, itemsAntiAnginoses);
        listviewAntiAnginoses.setAdapter(myItemsListAdapterAntiAnginoses);

        myItemsListAdapterAntiArrhythmics = new ItemsListAdapter(this, itemsAntiArrhythmics);
        listviewAntiArrhythmics.setAdapter(myItemsListAdapterAntiArrhythmics);

        myItemsListAdapterDiabetes = new ItemsListAdapter(this, itemsDiabetes);
        listviewDiabetes.setAdapter(myItemsListAdapterDiabetes);

        myItemsListAdapterHypertension = new ItemsListAdapter(this, itemsHypertension);
        listviewHypertension.setAdapter(myItemsListAdapterHypertension);

        myItemsListAdapterAntithrombotics = new ItemsListAdapter(this, itemsAntithrombotics);
        listviewAntithrombotics.setAdapter(myItemsListAdapterAntithrombotics);

        myItemsListAdapterPsychotropics = new ItemsListAdapter(this, itemsPsychotropics);
        listviewPsychotropics.setAdapter(myItemsListAdapterPsychotropics);

        myItemsListAdapterAntihistamines = new ItemsListAdapter(this, itemsAntihistamines);
        listviewAntihistamines.setAdapter(myItemsListAdapterAntihistamines);

        //Disable the scrolling of each list
        ListUtils.setDynamicHeight(listviewNonOpioidAnalgesics);
        ListUtils.setDynamicHeight(listviewOpioidAnalgesics);
        ListUtils.setDynamicHeight(listviewAntiAnginoses);
        ListUtils.setDynamicHeight(listviewAntiArrhythmics);
        ListUtils.setDynamicHeight(listviewDiabetes);
        ListUtils.setDynamicHeight(listviewHypertension);
        ListUtils.setDynamicHeight(listviewAntithrombotics);
        ListUtils.setDynamicHeight(listviewPsychotropics);
        ListUtils.setDynamicHeight(listviewAntihistamines);

    }

    /**
     * Initialize the lists for the adapters for the lists of the activity. Take the values of a string.
     */
    private void initItems(){
        itemsNonOpioidAnalgesics = new ArrayList<Item>();

        String string = getString(R.string.medication_non_opioid_analgesics);
        String[] arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsNonOpioidAnalgesics.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsNonOpioidAnalgesics.add(item);
            }
        }


        itemsOpioidAnalgesics = new ArrayList<Item>();

        string = getString(R.string.medication_opioid_analgesics);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsOpioidAnalgesics.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsOpioidAnalgesics.add(item);
            }
        }

        itemsAntiAnginoses = new ArrayList<Item>();

        string = getString(R.string.medication_anti_anginoses);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsAntiAnginoses.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsAntiAnginoses.add(item);
            }
        }

        itemsAntiArrhythmics = new ArrayList<Item>();

        string = getString(R.string.medication_anti_arrhythmics);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsAntiArrhythmics.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsAntiArrhythmics.add(item);
            }
        }

        itemsDiabetes = new ArrayList<Item>();

        string = getString(R.string.medication_diabetes);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsDiabetes.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsDiabetes.add(item);
            }
        }

        itemsHypertension = new ArrayList<Item>();

        string = getString(R.string.medication_hypertension);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsHypertension.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsHypertension.add(item);
            }
        }

        itemsAntithrombotics = new ArrayList<Item>();

        string = getString(R.string.medication_antithrombotics);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsAntithrombotics.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsAntithrombotics.add(item);
            }
        }

        itemsPsychotropics = new ArrayList<Item>();

        string = getString(R.string.medication_psychotropics);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsPsychotropics.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsPsychotropics.add(item);
            }
        }

        itemsAntihistamines = new ArrayList<Item>();

        string = getString(R.string.medication_antihistamines);
        arrayText = string.split(";");

        for(int i=0 ; i<arrayText.length ; i++){
            String text =  arrayText[i];
            String[] arrayTextSubs = text.split(":");
            if(arrayTextSubs.length == 1){
                Item item = new Item(arrayTextSubs[0], "", false);
                itemsAntihistamines.add(item);
            }else{
                Item item = new Item(arrayTextSubs[0], arrayTextSubs[1], false);
                itemsAntihistamines.add(item);
            }
        }

    }

    /**
     * Checks each activity list and sends it to the server
     * @param v For call from interface
     */
    public void finish(View v){
        String str = "Check items:\n";
        LinkedList<String> list = new LinkedList<String>();

        for (int i = 0; i< itemsNonOpioidAnalgesics.size(); i++){
            if (itemsNonOpioidAnalgesics.get(i).isChecked()){
                str += i + ": " + itemsNonOpioidAnalgesics.get(i).ItemStringTile+  "\n";
                //Take the string in the list. Waning with the translation language
                list.addLast(itemsNonOpioidAnalgesics.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsOpioidAnalgesics.size(); i++){
            if (itemsOpioidAnalgesics.get(i).isChecked()){
                str += i + ": " + itemsOpioidAnalgesics.get(i).ItemStringTile+  "\n";
                list.addLast(itemsOpioidAnalgesics.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsAntiAnginoses.size(); i++){
            if (itemsAntiAnginoses.get(i).isChecked()){
                str += i + ": " + itemsAntiAnginoses.get(i).ItemStringTile+  "\n";
                list.addLast(itemsAntiAnginoses.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsAntiArrhythmics.size(); i++){
            if (itemsAntiArrhythmics.get(i).isChecked()){
                str += i + ": " + itemsAntiArrhythmics.get(i).ItemStringTile+  "\n";
                list.addLast(itemsAntiArrhythmics.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsDiabetes.size(); i++){
            if (itemsDiabetes.get(i).isChecked()){
                str += i + ": " + itemsDiabetes.get(i).ItemStringTile+  "\n";
                list.addLast(itemsDiabetes.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsHypertension.size(); i++){
            if (itemsHypertension.get(i).isChecked()){
                str += i + ": " + itemsHypertension.get(i).ItemStringTile+  "\n";
                list.addLast(itemsHypertension.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsAntithrombotics.size(); i++){
            if (itemsAntithrombotics.get(i).isChecked()){
                str += i + ": " + itemsAntithrombotics.get(i).ItemStringTile+  "\n";
                list.addLast(itemsAntithrombotics.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsPsychotropics.size(); i++){
            if (itemsPsychotropics.get(i).isChecked()){
                str += i + ": " + itemsPsychotropics.get(i).ItemStringTile+  "\n";
                list.addLast(itemsPsychotropics.get(i).ItemStringTile);
            }
        }

        for (int i = 0; i< itemsAntihistamines.size(); i++){
            if (itemsAntihistamines.get(i).isChecked()){
                str += i + ": " + itemsAntihistamines.get(i).ItemStringTile+  "\n";
                list.addLast(itemsAntihistamines.get(i).ItemStringTile);
            }
        }

        if(list.isEmpty()){

            textViewError.setVisibility(View.VISIBLE);

        }else{
            Bundle bundle = this.getIntent().getExtras();

            DataManager.getInstance().sendDataMedicationSurvey(list, bundle.getInt("type"));

            Intent intent = new Intent(SurveyMedication.this, MainActivity.class);
            startActivity(intent);
        }

    }

    /**
     * Remove the scroll in the list
     */
    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
