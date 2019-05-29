package com.example.to_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    public static final String TABLE_NAME = "task_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TASK";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "EXPIRY";
    public static final String COL_5 = "PENDING";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +  " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, DESCRIPTION TEXT, EXPIRY TEXT, PENDING TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String task, String description, String expiry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,task);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,expiry);
        contentValues.put(COL_5,"y");
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getListData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * FROM " + TABLE_NAME,null);
        return res;
    }

}
