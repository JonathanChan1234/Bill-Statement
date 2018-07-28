package com.example.jonathan.billstatement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RecordView extends AppCompatActivity {

    ListView lv_note;
    SQLiteDatabase db;
    ArrayList<String> recordlist;
    ArrayList<String> timeInfo;
    ArrayList<Bundle> bundles;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);
        lv_note = (ListView) findViewById(R.id.lv);
        lv_note.setOnItemClickListener(viewDetails);
    }

    protected void onResume(){
        super.onResume();
        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        recordlist = Info.getQueryInfo(db);

        timeInfo = Info.getTimeInfo(db);
        bundles = Info.getDetails(db);
        ids = Info.getIds(db);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordlist);
        lv_note.setAdapter(adapter);
    }

   AdapterView.OnItemClickListener viewDetails = new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           Intent intent = new Intent();
           intent.setClass(RecordView.this, Details.class);
           Bundle bundle = bundles.get(i);
           intent.putExtra("INFO", bundle);
           intent.putExtra("IDS", ids.get(i));
           intent.putExtra("TIMEINFO", timeInfo.get(i));
           startActivity(intent);
       }
   };
}
