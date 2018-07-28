package com.example.jonathan.billstatement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditView extends AppCompatActivity {
    String[] purposeText;
    String[] typeText;
    EditText amount;
    EditText description;
    TextView date;
    Spinner spinnerPurpose;
    Spinner spinnerType;

    int year;
    int day;
    int month;
    int hour;
    int minute;
    int second;
    Double total = 0.0;
    String purposeFinal;
    String typeFinal;
    String descriptionFinal;
    Bundle bundle;

    SQLiteDatabase db;
    int notepos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
        purposeText = getResources().getStringArray(R.array.purposeArray);
        typeText = getResources().getStringArray(R.array.typeArray);

        spinnerPurpose = (Spinner) findViewById(R.id.purposeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditView.this, R.array.purposeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurpose.setAdapter(adapter);
        spinnerPurpose.setOnItemSelectedListener(purpose);

        spinnerType = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(EditView.this, R.array.typeArray, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        spinnerType.setAdapter(adapter1);
        spinnerType.setOnItemSelectedListener(type);

        amount = (EditText) findViewById(R.id.amountText);
        description = (EditText) findViewById(R.id.descriptionText);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(submission);

        Intent intent = getIntent();
        notepos = intent.getIntExtra("NOTEPOS", -1);
        descriptionFinal = intent.getStringExtra("DESCRIPTION");
        typeFinal = intent.getStringExtra("TYPE");
        total = intent.getDoubleExtra("AMOUNT", 0.0);
        purposeFinal = intent.getStringExtra("CATEGORIES");

    }

    public void onStart(){
        super.onStart();
        if(notepos == -1){
            purposeFinal = "Food";
            typeFinal = "Income";

            amount.setText(total + "");
            description.setText("");
            descriptionFinal = "";
        }
        else{
            spinnerPurpose.setSelection(getPosition(purposeFinal));
            spinnerType.setSelection(getPositionType(typeFinal));
            amount.setText(total + "");
            description.setText(descriptionFinal);
        }
    }

    public void onResume(){
        super.onResume();
        DBOpenHelper openHelper = new DBOpenHelper(this);
        db = openHelper.getWritableDatabase();
    }

    AdapterView.OnItemSelectedListener purpose = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            purposeFinal = purposeText[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener type = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            typeFinal = typeText[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener submission = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            total = Double.parseDouble(amount.getText().toString());
            descriptionFinal = description.getText().toString();

            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH) + 1;
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            second = c.get(Calendar.SECOND);

            if(notepos == -1){
                Info.addQuery(db, day, month, year, hour, minute, second, purposeFinal, descriptionFinal, total, typeFinal);
                Toast.makeText(EditView.this, "Your query has already added", Toast.LENGTH_SHORT).show();
            }
            else{
                Info.editQuery(db, day, month, year, hour, minute, second, purposeFinal, descriptionFinal, total, typeFinal, notepos);
                Toast.makeText(EditView.this, "Your query has been updated", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.setClass(EditView.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

                /*
                Intent intent = new Intent();
                intent.setClass(EditView.this, ReportView.class);
                intent.putExtra("NOTEPOS", -1);
                intent.putExtra("DAY" ,day);
                intent.putExtra("MONTH", month);
                intent.putExtra("PURPOSE", purposeFinal);
                intent.putExtra("TYPE", typeFinal);
                intent.putExtra("AMOUNT", total);
                startActivity(intent);
                */
                //finish();
        }
    };

    private int getPosition(String text){
        for(int i=0; i<purposeText.length; ++i){
            if(text.equalsIgnoreCase(purposeText[i])){
                return i;
            }
        }
        return 0;
    }

    private int getPositionType(String type){
        if(type.equalsIgnoreCase("Income")) return 0;
        return 1;
    }

}
