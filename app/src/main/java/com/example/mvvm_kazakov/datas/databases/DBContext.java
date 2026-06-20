package com.example.mvvm_kazakov.datas.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBContext extends SQLiteOpenHelper {
    public static SQLiteDatabase sqLiteDatabase;
    public DBContext(Context context){
        super(context,"DBWeather",null,1);
        sqLiteDatabase = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(
                "CREATE TABLE Days("+
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "Name TEXT NOT NULL,"+
                        "Temp INTEGER NOT NULL,"+
                        "Condition TEXT NOT NULL);"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

    }
}