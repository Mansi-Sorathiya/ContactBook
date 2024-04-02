package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class MyDataBase extends SQLiteOpenHelper
{
    String TAG="MMM";
    public MyDataBase(@Nullable Context context) {
        super(context, "Contact", null, 1);
        Log.d(TAG, "MyDataBase: Database Created..");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table ContactBook(ID integer primary key autoincrement,NAME text,NUMBER text,IMG_PATH BLOB)";
        db.execSQL(query);
        Log.d(TAG, "onCreate: Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addData(String name, String number, Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Log.d("XXX", "addData: BITMAP in Add Function="+bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Could be Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.WEBP
        byte[] bytes = baos.toByteArray();

//        String query="insert into ContactBook(NAME,NUMBER,IMG_PATH) values('"+name+"','"+number+"','"+imgPath+"')";
        SQLiteDatabase db=getWritableDatabase();
//        db.execSQL(query);
        ContentValues values=new ContentValues();
        values.put("NAME",name);
        values.put("NUMBER",number);
        values.put("IMG_PATH",bytes);
        db.insert("ContactBook",null,values);
    }

    public Cursor viewData()
    {
        String query="select * from ContactBook";
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public void deleteData(Integer id) {

        String query="delete from ContactBook where ID="+id+"";
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL(query);
    }

    public void updatedata(Integer id,String name,String number,Bitmap image) {

//        String query = "update ContactBook set NAME='" + name + "', NUMBER='" + number + "', IMG_PATH='" + image + "' WHERE ID=" + id;
//
//        SQLiteDatabase db=getWritableDatabase();
//        db.execSQL(query);
        String query="update ContactBook set NAME='"+name+"',NUMBER='"+number+"',IMG_PATH='"+image+"', where ID="+id+"";
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL(query);

    }
}
