package com.example.to_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    public static final String TABLE_NAME = "task_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TASK";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "EXPIRY";
    public static final String COL_5 = "PENDING";
    public static final String COL_6 = "COMPLETED";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +  " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, DESCRIPTION TEXT, EXPIRY TEXT, PENDING TEXT, COMPLETED TEXT)");

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
        contentValues.put(COL_5,"Y");
        contentValues.put(COL_6,"-");
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getListData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * FROM " + TABLE_NAME + " where PENDING='Y' ",null);
        return res;
    }

    public Integer deleteData(String ids){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] {ids});
    }

    public boolean completeData(String ids, String task, String description, String expiry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ids);
        contentValues.put(COL_2,task);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,expiry);
        contentValues.put(COL_5,"N");
        String date;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now().toString();
        }else {
            date = "NaN";
        }
        contentValues.put(COL_6,date);
        Integer result = db.update(TABLE_NAME,contentValues, "ID = ?", new String[]{ ids });
        if(result >0){
            return true;
        }else {
            return false;
        }
    }

    public boolean editData(String ids, String task, String description, String expiry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ids);
        contentValues.put(COL_2,task);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,expiry);
        contentValues.put(COL_5,"Y");
        db.update(TABLE_NAME,contentValues, "ID = ?", new String[]{ ids });
        return true;
    }

    public Cursor getCompleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * FROM " + TABLE_NAME + " where PENDING='N' ",null);
        return res;
    }

}
