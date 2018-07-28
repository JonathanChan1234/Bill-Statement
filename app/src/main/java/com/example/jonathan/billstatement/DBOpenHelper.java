package com.example.jonathan.billstatement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan on 27/8/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context) {
        super(context, "demo4.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Info.DATABASE_CREATETABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    }
}
