package com.example.with_you;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //initialize DB name and Table name
    private static final String DATABASE_NAME="database_name";
    private static final String TABLE_NAME="table_name";

    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Table
        String createTable="create table "+ TABLE_NAME+" (id INTEGER PRIMARY KEY,txt TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean addText(String text){
        //Get Writable Database
        SQLiteDatabase sqLiteDatabase =this.getWritableDatabase();
        //Create ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("txt",text);
        //Add Values into Database
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return true;

    }

    public ArrayList getAllText()
    {
        //get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList=new ArrayList<String>();
        //create cursor to select all values
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return arrayList;
    }
}
