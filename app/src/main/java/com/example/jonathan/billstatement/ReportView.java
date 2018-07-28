package com.example.jonathan.billstatement;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportView extends AppCompatActivity {
    ListView lv_report;
    Button leftButtonYear;
    Button rightButtonYear;
    Button leftButtonMonth;
    Button rightButtonMonth;
    Button leftButtonCategory;
    Button rightButtonCategory;
    Spinner yearSpinner;
    Spinner monthSpinner;
    Spinner categoriesSpinner;

    int year;
    int month;
    int categoryPosition;

    String[] yearArray;
    String[] monthArray;
    String[] categoriesArray;

    SQLiteDatabase db;

    ArrayList<String> reportlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        yearArray = getResources().getStringArray(R.array.yearArray);
        monthArray = getResources().getStringArray(R.array.monthArray);
        categoriesArray = getResources().getStringArray(R.array.categoriesArray);

        lv_report = (ListView) findViewById(R.id.lv_report);
        leftButtonYear = (Button) findViewById(R.id.leftButtonYear);
        rightButtonYear = (Button) findViewById (R.id.rightButtonYear);
        leftButtonMonth = (Button) findViewById(R.id.leftButtonMonth);
        rightButtonMonth = (Button) findViewById(R.id.rightButtonMonth);
        leftButtonCategory = (Button) findViewById(R.id.leftButtonCategory);
        rightButtonCategory = (Button) findViewById(R.id.rightButtonCategory);

        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        categoriesSpinner = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(ReportView.this, R.array.yearArray, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        yearSpinner.setAdapter(adapter1);
        yearSpinner.setOnItemSelectedListener(updateYear);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(ReportView.this, R.array.monthArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        monthSpinner.setAdapter(adapter2);
        monthSpinner.setOnItemSelectedListener(updateMonth);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(ReportView.this, R.array.categoriesArray, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        categoriesSpinner.setAdapter(adapter3);
        categoriesSpinner.setOnItemSelectedListener(updateCategories);

        leftButtonMonth.setOnClickListener(monthDecrement);
        rightButtonMonth.setOnClickListener(monthIncrement);

        leftButtonYear.setOnClickListener(yearDecrement);
        rightButtonYear.setOnClickListener(yearIncrement);

        leftButtonCategory.setOnClickListener(categoriesDecrement);
        rightButtonCategory.setOnClickListener(categoriesIncrement);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        categoryPosition = 0;

    }

    public void onResume(){
        super.onResume();
        monthSpinner.setSelection(getMonthPosition(month));
        yearSpinner.setSelection(getYearPosition(year));
        categoriesSpinner.setSelection(categoryPosition);

        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        reportlist = Info.getReport(db, month, year, categoriesArray[categoryPosition]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportlist);
        lv_report.setAdapter(adapter);

    }

    AdapterView.OnItemSelectedListener updateYear = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            year = Integer.parseInt(yearArray[i]);
            updateList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener updateMonth = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(monthArray[i].equalsIgnoreCase("All")) month = 13;
            else month = Integer.parseInt(monthArray[i]);
            updateList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener updateCategories = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            categoryPosition = i;
            updateList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener yearIncrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            year++;
            if(year>2037) year = 2017;
            yearSpinner.setSelection(getYearPosition(year));
            updateList();
        }
    };

    View.OnClickListener yearDecrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            year--;
            if(year<2017) year = 2037;
            yearSpinner.setSelection(getYearPosition(year));
            updateList();
        }
    };

    View.OnClickListener monthIncrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            month++;
            if (month > 13) month = 1;
            monthSpinner.setSelection(getMonthPosition(month));
            updateList();
        }
    };

    View.OnClickListener monthDecrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            month--;
            if(month<1) month = 13;
            monthSpinner.setSelection(getMonthPosition(month));
            updateList();
        }
    };

    View.OnClickListener categoriesIncrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            categoryPosition = (categoryPosition + 1) % 6;
            categoriesSpinner.setSelection(categoryPosition);
            updateList();
        }
    };

    View.OnClickListener categoriesDecrement = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            categoryPosition = (categoryPosition - 1);
            if (categoryPosition < 0) categoryPosition = 5;
            categoriesSpinner.setSelection(categoryPosition);
            updateList();
        }
    };


    public void updateList(){
        reportlist = Info.getReport(db, month, year, categoriesArray[categoryPosition]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportView.this, android.R.layout.simple_list_item_1, reportlist);
        lv_report.setAdapter(adapter);
    }


    private int getYearPosition(int year){
        String mYear = year + "";
        for(int i=0; i<yearArray.length; ++i){
            if(mYear.equalsIgnoreCase(yearArray[i])){
                return i;
            }
        }
        return 0;
    }

    private int getMonthPosition(int month){
        if(month == 13) return 12;
        String mMonth = month + "";
        for(int i=0; i<monthArray.length; ++i){
            if(mMonth.equalsIgnoreCase(monthArray[i])){
                return i;
            }
        }
        return 0;
    }

    private int getCategoryPosition(String category){
        for(int i=0; i<categoriesArray.length; ++i){
            if(category.equalsIgnoreCase(categoriesArray[i]))   return i;
        }
        return 0;
    }

}
