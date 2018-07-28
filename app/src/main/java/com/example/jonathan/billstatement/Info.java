package com.example.jonathan.billstatement;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jonathan on 27/8/2017.
 */

public class Info {
    final static String TABLE = "data";
    final static String DATABASE_CREATETABLE = "create table " +
            TABLE + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Date INTEGER, Month INTEGER, Year INTEGER, Hour INTEGER, " +
            "Minute INTEGER, Second INTEGER, Purpose TEXT, Description TEXT, Amount REAL, Type TEXT);";
    static int id = 0;

    static void addQuery(SQLiteDatabase db, int date, int month, int year, int hour, int minute, int second, String purpose, String description, Double amount, String type){
        id++;
        Log.d("Data:", id+"");
        db.execSQL("insert into " + TABLE + "(Date, Month, Year, Hour, Minute, Second, Purpose, Description, Amount, Type) values('" + date + "','" + month + "','" + year + "','" + hour
                + "','" + minute + "','" + second + "','" + purpose + "','" + description + "','" + amount + "','" + type +"');");
    }

    static void editQuery(SQLiteDatabase db, int date, int month, int year, int hour, int minute, int second, String purpose, String description, Double amount, String type, int id){
        db.execSQL("update " + TABLE + " set  Date='" + date +"', Month='" + month +"', Year='" + year + "', Hour ='" + hour + "', Minute='" + minute
        + "',Second='" + second + "',Purpose='" + purpose + "',  Description='" + description + "', Amount = '" + amount + "',Type = '" + type + "' where Id = '" + id + "';");
    }

    static ArrayList<String> getQueryInfo(SQLiteDatabase db){
        ArrayList<String> recordList = new ArrayList<String>();
        Cursor c = db.rawQuery("select*from " + TABLE +  " order by Id desc", null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); ++i){
            Log.d("ID", c.getInt(c.getColumnIndex("Id")) + "");
            String msg = c.getInt(c.getColumnIndex("Year")) + "/" + c.getInt(c.getColumnIndex("Month")) + "/" + c.getInt(c.getColumnIndex("Date")) + "\t\t";

            int hour = c.getInt(c.getColumnIndex("Hour"));
            String hourInText;
            if(hour<10) hourInText = "0" + hour;
            else hourInText = hour + "";

            int minute = c.getInt(c.getColumnIndex("Minute"));
            String minuteInText;
            if(minute<10) minuteInText = "0" + minute;
            else minuteInText = minute + "";

            int second = c.getInt(c.getColumnIndex("Second"));
            String secondInText;
            if(second<10) secondInText = "0" + second;
            else secondInText = second + "";

            msg = msg + hourInText + ":" + minuteInText + ":" + secondInText + "\n";
            msg = msg + c.getString(c.getColumnIndex("Purpose")) + "\t\t";
            msg = msg + "$" + c.getDouble(c.getColumnIndex("Amount"));
            recordList.add(msg);
            c.moveToNext();
        }
        return recordList;
    }

    static ArrayList<String> getTimeInfo(SQLiteDatabase db){
        ArrayList<String> timeInfo = new ArrayList<String>();
        Cursor c = db.rawQuery("select*from " + TABLE +  " order by Id desc", null);
        c.moveToFirst();
        for(int i=0; i<c.getCount(); ++i){
            String msg = c.getInt(c.getColumnIndex("Year")) + "/" + c.getInt(c.getColumnIndex("Month")) + "/" + c.getInt(c.getColumnIndex("Date")) + "\t\t";

            int hour = c.getInt(c.getColumnIndex("Hour"));
            String hourInText;
            if(hour<10) hourInText = "0" + hour;
            else hourInText = hour + "";

            int minute = c.getInt(c.getColumnIndex("Minute"));
            String minuteInText;
            if(minute<10) minuteInText = "0" + minute;
            else minuteInText = minute + "";

            int second = c.getInt(c.getColumnIndex("Second"));
            String secondInText;
            if(second<10) secondInText = "0" + second;
            else secondInText = second + "";

            msg = msg + hourInText + ":" + minuteInText + ":" + secondInText + "\n";
            timeInfo.add(msg);
            c.moveToNext();
        }

        return timeInfo;
    }

    static ArrayList<Bundle> getDetails(SQLiteDatabase db){
        ArrayList<Bundle> bundlelist = new ArrayList<Bundle>();
        Cursor c = db.rawQuery("select*from " + TABLE +  " order by Id desc", null);
        c.moveToFirst();
        for(int i=0; i<c.getCount(); ++i){
            Bundle bundle = new Bundle();
            bundle.putString("DESCRIPTION", c.getString(c.getColumnIndex("Description")));
            bundle.putString("CATEGORIES", c.getString(c.getColumnIndex("Purpose")));
            bundle.putDouble("AMOUNT", c.getDouble(c.getColumnIndex("Amount")));
            bundle.putString("TYPE", c.getString(c.getColumnIndex("Type")));
            bundlelist.add(bundle);
            c.moveToNext();
        }

        return bundlelist;
    }
    static ArrayList<Integer> getIds(SQLiteDatabase db){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Cursor c = db.rawQuery("select*from " + TABLE +  " order by Id desc", null);
        c.moveToFirst();
        for(int i=0; i<c.getCount(); ++i){
            ids.add(c.getInt(c.getColumnIndex("Id")));
            c.moveToNext();
        }

        return ids;
    }

    static Bundle getElement(SQLiteDatabase db, int id){
        Cursor c = db.rawQuery("select*from " + TABLE +  " where Id=" + id, null);
        c.moveToFirst();
        Bundle bundle = new Bundle();
        bundle.putString("DESCRIPTION", c.getString(c.getColumnIndex("Description")));
        bundle.putString("CATEGORIES", c.getString(c.getColumnIndex("Purpose")));
        bundle.putDouble("AMOUNT", c.getDouble(c.getColumnIndex("Amount")));
        bundle.putString("TYPE", c.getString(c.getColumnIndex("Type")));
        return bundle;
    }

    static ArrayList<String> getReport(SQLiteDatabase db, int month, int year, String category){
        ArrayList<String> report = new ArrayList<String>();
        report.add("Income");
        Double incomeAmount = 0.0;
        Double outcomeAmount = 0.0;

        if(category.equalsIgnoreCase("All")){
            Cursor c1;
            if(month!=13){
                c1 = db.rawQuery("select*from " + TABLE +  " where Month=" + month + " And Year =" + year + " And Type= " + "'Income'", null);
                c1.moveToFirst();
                for(int i=0; i<c1.getCount(); ++i){
                    incomeAmount+=c1.getDouble(c1.getColumnIndex("Amount"));
                    c1.moveToNext();
                }
                report.add(incomeAmount + "");
            }
            else{
                c1 = db.rawQuery("select*from " + TABLE +  " where  Year =" + year + " And Type= " + "'Income'", null);
                c1.moveToFirst();
                for(int i=0; i<c1.getCount(); ++i){
                    incomeAmount+=c1.getDouble(c1.getColumnIndex("Amount"));
                    c1.moveToNext();
                }
                report.add(incomeAmount + "");
            }

            report.add("Outcome");

            Cursor c2;
            if(month!=13){
                c2 = db.rawQuery("select*from " + TABLE +  " where Month='" + month + "' And Year ='" + year + "' And Type='" + "Outcome" + "'", null);
                c2.moveToFirst();
                for(int i=0; i<c2.getCount(); ++i){
                    outcomeAmount+=c2.getDouble(c2.getColumnIndex("Amount"));
                    c2.moveToNext();
                }
                report.add(outcomeAmount + "");
            }
            else{
                c2 = db.rawQuery("select*from " + TABLE +  " where Year ='" + year + "' And Type='" + "Outcome" + "'", null);
                c2.moveToFirst();
                for(int i=0; i<c2.getCount(); ++i){
                    outcomeAmount+=c2.getDouble(c2.getColumnIndex("Amount"));
                    c2.moveToNext();
                }
                report.add(outcomeAmount + "");
            }
        }
        else{
            Cursor c3;
            if(month!=13){
                c3 = db.rawQuery("select*from " + TABLE +  " where Month=" + month + " And Year =" + year + " And Type= " + "'Income' And Purpose='" + category +"'", null);
                c3.moveToFirst();
                for(int i=0; i<c3.getCount(); ++i){
                    incomeAmount+=c3.getDouble(c3.getColumnIndex("Amount"));
                    c3.moveToNext();
                }
                report.add(incomeAmount + "");
            }
            else{
                c3 = db.rawQuery("select*from " + TABLE +  " where  Year =" + year + " And Type= " + "'Income' And Purpose='" + category + "'", null);
                c3.moveToFirst();
                for(int i=0; i<c3.getCount(); ++i){
                    incomeAmount+=c3.getDouble(c3.getColumnIndex("Amount"));
                    c3.moveToNext();
                }
                report.add(incomeAmount + "");
            }

            report.add("Outcome");

            Cursor c4;
            if(month!=13){
                c4 = db.rawQuery("select*from " + TABLE +  " where Month='" + month + "' And Year ='" + year + "' And Type='" + "Outcome" + "' And Purpose='" + category + "'", null);
                c4.moveToFirst();
                for(int i=0; i<c4.getCount(); ++i){
                    outcomeAmount+=c4.getDouble(c4.getColumnIndex("Amount"));
                    c4.moveToNext();
                }
                report.add(outcomeAmount + "");
            }
            else{
                c4 = db.rawQuery("select*from " + TABLE +  " where Year ='" + year + "' And Type='" + "Outcome" + "' And Purpose='" + category + "'", null);
                c4.moveToFirst();
                for(int i=0; i<c4.getCount(); ++i){
                    outcomeAmount+=c4.getDouble(c4.getColumnIndex("Amount"));
                    c4.moveToNext();
                }
                report.add(outcomeAmount + "");
            }
        }




        return report;
    }


    static void deleteSingleQuery(SQLiteDatabase db, int id){
        db.execSQL("delete from " + TABLE + " where Id=" + id);

    }

    static void deleteDatabase(SQLiteDatabase db){
        db.execSQL("delete from " + TABLE + ";");
    }


}
