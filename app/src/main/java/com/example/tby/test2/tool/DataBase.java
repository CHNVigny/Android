package com.example.tby.test2.tool;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by tby on 2016/12/10.
 */

public class DataBase {
    public static SQLiteDatabase db;
    public static void init(){

        db.execSQL("CREATE TABLE IF NOT EXISTS list2(_id INTEGER PRIMARY KEY AUTOINCREMENT,status text not null," +
                " title text not null, content text not null,url text not null,src text not null,imgurl text null, kind text not null,version INTEGER not null,imgnum INTEGER not null,img BLOB) ");
        if(db!=null){
            db.execSQL("delete from list2 where version=0");
            Log.d("DELETE","version");
        }
        db.execSQL("CREATE TABLE IF NOT EXISTS kind(_id INTEGER PRIMARY KEY,kind text not null)");
        db.execSQL("CREATE TABLE IF NOT EXISTS collection(_id INTEGER PRIMARY KEY AUTOINCREMENT,title text not null,content text not null" +
                ",src text not null,url text not null)");
    }

}
