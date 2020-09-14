package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String Database_Name = "info.db";
    private static final String Table_Name = "Details";
    private static final String Column_Name = "Name";
    private static final String Column_Id = "Id";
    private static final String Column_Dept = "Department";
    private static final String Column_Age = "Age";

    private static final int version = 4;
    private static final String Create_Table = "CREATE TABLE "+Table_Name+
            "("+Column_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Column_Name+" VARCHAR(100),"+Column_Dept+" VARCHAR(50),"+Column_Age+" INTEGER); ";

    private static final String Drop_Table = "DROP TABLE IF EXISTS "+Table_Name;
    private static final String Display_Table = "SELECT * FROM "+Table_Name;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(Create_Table);
            Toast.makeText(context,"Table done",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context,"Exception :"+e,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(Drop_Table);
            onCreate(db);
            Toast.makeText(context,"Upgrade done",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context,"Exception :"+e,Toast.LENGTH_SHORT).show();
        }
    }

    public long insertToDatabase(String name, String dept, int age){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Name,name);
        contentValues.put(Column_Dept,dept);
        contentValues.put(Column_Age,age);
        long rowId = sqLiteDatabase.insert(Table_Name,null,contentValues);
        return rowId;
    }

    public boolean updateDatabase(int id, String name, String dept, int age){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Id,id);
        contentValues.put(Column_Name,name);
        contentValues.put(Column_Dept,dept);
        contentValues.put(Column_Age,age);
        Integer ID = new Integer(id);
        sqLiteDatabase.update(Table_Name,contentValues,Column_Id+" = ?",new String[]{ID.toString()});
        return true;
    }

    public int deleteDatabase(int id){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Integer ID = new Integer(id);
        return sqLiteDatabase.delete(Table_Name,Column_Id+" = ?",new String[]{ID.toString()});

    }

    public Cursor displayAll(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Display_Table,null);
        return cursor;
    }
}
