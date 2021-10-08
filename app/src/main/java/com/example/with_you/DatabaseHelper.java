package com.example.with_you;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    //initialize DB name and Table name
    public static final String DATABASE_NAME = "user_record.db";
    public static final String TABLE_NAME = "user_data";




    DatabaseHelper(Context context) {
        super(context, "user_record.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        //Create Table
         MyDB.execSQL("CREATE TABLE user_data (name TEXT PRIMARY KEY,username TEXT, email TEXT,mob01 TEXT," +
                 "mob02 TEXT,mob03 TEXT,keyword TEXT)"
       );

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        //Drop older table if exist
        MyDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean insert(String name, String username, String email, String mob01, String mob02, String mob03, String keyword) {
        //Get Writable Database
        SQLiteDatabase MyDB = this.getWritableDatabase();
        //Create ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("mob01", mob01);
        contentValues.put("mob02", mob02);
        contentValues.put("mob03", mob03);
        contentValues.put("keyword", keyword);

        //Add Values into Database
        long result = MyDB.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }

    }

    public Boolean checkUser(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
       Cursor cursor=MyDB.rawQuery("SELECT * FROM TABLE_NAME WHERE username= ?",new String[] {username});

        if(cursor.getCount()> 0){
            return true;
        }
        else {
            return false;
        }



    }

    public Boolean checkUserPassword(String username,String password){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM TABLE_NAME WHERE username = ? and password = ?",new String[] {username,password});
        if(cursor.getCount()> 0){
            return true;
        }
        else {
            return false;
        }

    }

}
