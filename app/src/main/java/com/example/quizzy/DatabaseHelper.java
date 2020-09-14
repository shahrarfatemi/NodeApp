package com.example.quizzy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String Database_Name = "info.db";
    private static final String Table_Name = "Users";
    private static final String Column_Id = "Id";
    private static final String Column_Name = "Name";
    private static final String Column_Email = "Email";
    private static final String Column_Token = "Token";

    private static final int version = 1;
    private static final String Create_Table = "CREATE TABLE "+Table_Name+
            "("+Column_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Column_Name+" VARCHAR(100),"+Column_Email+" VARCHAR(100) NOT NULL UNIQUE,"+Column_Token+" TEXT); ";

    private static final String Drop_Table = "DROP TABLE IF EXISTS "+Table_Name;
    private static final String Display_Table = "SELECT * FROM "+Table_Name;
    private static final String Find_Id_From_Email = "SELECT "+Column_Id+" FROM "+Table_Name+
            " WHERE "+Column_Email+" LIKE ";
    private static final String Find_Id_From_Token = "SELECT "+Column_Id+" FROM "+Table_Name+
            " WHERE "+Column_Token+" LIKE ";
    private static final String Find_Token = "SELECT "+Column_Token+" FROM "+Table_Name;

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

    public long insertToDatabase(String name, String email, String token){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Name,name);
        contentValues.put(Column_Email,email);
        contentValues.put(Column_Token,token);
        long rowId = sqLiteDatabase.insert(Table_Name,null,contentValues);
        return rowId;
    }

    public boolean updateDatabase(int id, String name, String email, String token){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_Id,id);
        contentValues.put(Column_Name,name);
        contentValues.put(Column_Email,email);
        contentValues.put(Column_Token,token);
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

    public int getIdFromEmail(String email){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = Find_Id_From_Email +"'"+email+"';";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            return id;
        }
        return -1;
    }

    public int getIdFromToken(String token){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = Find_Id_From_Token +"'"+token+"';";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            return id;
        }
        return -1;
    }

    public String findValidToken(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = Find_Token +" WHERE "+Column_Token+" IS NOT NULL; ";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String token = cursor.getString(0);
            return token;
        }
        return "";
    }
}
