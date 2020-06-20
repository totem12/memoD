package com.example.memod;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoHelper extends SQLiteOpenHelper {

    static final private String DBName = "Memo_DB";
    static final private int VERSION = 1;

    MemoHelper(Context context){
        super(context, DBName, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE MEMO_TABLE (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT, " +
                "body TEXT," +
                "title TEXT)" );

        db.execSQL("CREATE TABLE DATE_TABLE (" +
                "uuid TEXT PRIMARY KEY , " +
                "date TEXT," +
                "date2 TEXT," +
                "date3 TEXT)" );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS MEMO_TABLE");
        db.execSQL("DROP TABLE IF EXISTS DATE_TABLE");
        onCreate(db);
    }
}
