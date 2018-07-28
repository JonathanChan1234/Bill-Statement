package com.example.jonathan.billstatement;

        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int notepos;
    SQLiteDatabase db;
    private Boolean exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addQuery = (Button) findViewById(R.id.addQuery);
        addQuery.setOnClickListener(add);

        Button checkRecord = (Button) findViewById(R.id.checkRecord);
        checkRecord.setOnClickListener(check);

        Button getReport = (Button) findViewById(R.id.reportButton);
        getReport.setOnClickListener(report);

        Button clearRecord = (Button) findViewById(R.id.clearButton);
        clearRecord.setEnabled(false);
        clearRecord.setOnClickListener(clear);
    }

    View.OnClickListener add = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            notepos = -1;
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, EditView.class);
            intent.putExtra("NOTEPOS", notepos);
            startActivity(intent);
        }
    };

    View.OnClickListener check = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RecordView.class);
            startActivity(intent);
        }
    };

    View.OnClickListener clear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Info.deleteDatabase(db);
        }
    };

    View.OnClickListener report = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ReportView.class);
            startActivity(intent);
        }
    };

    public void onStart(){
        super.onStart();
        exit = false;
    }

    public void onResume(){
        super.onResume();
        DBOpenHelper openHelper = new DBOpenHelper(this);
        db = openHelper.getWritableDatabase();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
        }

    }
}
