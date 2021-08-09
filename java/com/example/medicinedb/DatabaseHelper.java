package com.example.medicinedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Medicinedatabase";
//    private static final String COL1 = "ID";
    private static final String COL1 = "Name";
    private static final String COL2 = "Date";
    private static final String COL3 = "Time";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public Boolean addData(String MDName,String time,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, MDName);
        contentValues.put(COL2, date);
        contentValues.put(COL3, time);

        Log.d("dbhelper","inserting data" + MDName + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

//    public Boolean updateData(String name,String time,String date){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL2, date);
//        contentValues.put(COL3, time);
//        Cursor cursor
//
//        long result = db.update(TABLE_NAME,contentValues,"name=?",new String[] {name});
//
//        if(result == -1){
//            return false;
//        }else{
//            return true;
//        }
//    }

    public Cursor getdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }
}
