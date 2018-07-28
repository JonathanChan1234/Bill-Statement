package com.example.jonathan.billstatement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    SQLiteDatabase db;
    TextView editTime;
    TextView description;
    TextView categories;
    TextView amount;
    TextView type;
    Button back;

    Bundle info;
    String timeInfo;
    Button editButton;
    Button deleteButton;

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        editTime = (TextView) findViewById(R.id.dateDetail);
        description = (TextView) findViewById(R.id.description);
        categories = (TextView) findViewById(R.id.categories);
        amount = (TextView) findViewById(R.id.amount);
        type = (TextView) findViewById(R.id.type);

        back = (Button) findViewById(R.id.backToMainMenu);
        back.setOnClickListener(backToMainMenu);

        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(edit);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(delete);

        Intent intent = getIntent();
        timeInfo = intent.getStringExtra("TIMEINFO");
        info = intent.getBundleExtra("INFO");
        editTime.setText(timeInfo);
        description.setText(info.getString("DESCRIPTION"));
        categories.setText(info.getString("CATEGORIES"));
        amount.setText(info.getDouble("AMOUNT") + "");
        type.setText(info.getString("TYPE"));

        id = intent.getIntExtra("IDS", -1);
    }

    protected void onResume() {
        super.onResume();
        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();
    }

    View.OnClickListener backToMainMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(Details.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    View.OnClickListener edit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(Details.this, EditView.class);
            intent.putExtra("NOTEPOS", id);
            intent.putExtra("DESCRIPTION", info.getString("DESCRIPTION"));
            intent.putExtra("CATEGORIES", info.getString("CATEGORIES"));
            intent.putExtra("AMOUNT", info.getDouble("AMOUNT"));
            intent.putExtra("TYPE", info.getString("TYPE"));
            startActivity(intent);
        }
    };

    View.OnClickListener delete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Info.deleteSingleQuery(db, id);
            finish();
        }
    };

}
