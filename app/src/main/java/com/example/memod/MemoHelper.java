package com.example.memod;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import java.util.ArrayList;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT," +
                "date TEXT," +
                "date2 TEXT," +
                "date3 TEXT)" );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS MEMO_TABLE");
        db.execSQL("DROP TABLE IF EXISTS DATE_TABLE");
        onCreate(db);
    }

    public ListItem getListItem(Cursor cs, SQLiteDatabase db){
        ListItem item = new ListItem();

        item.setBody(cs.getString(0));
        item.setTitle(cs.getString(1));
        item.setUuid(cs.getString(2));

        Cursor dcs = db.rawQuery("select date, date2, date3 from DATE_TABLE where uuid = '"+ item.getUuid() +"'", null);
        dcs.moveToFirst();

        item.setDate(dcs.getString(0));
        item.setDate2(dcs.getString(1));
        item.setDate3(dcs.getString(2));

        dcs.close();
        return item;
    }

    public ArrayList<ListItem> getData(MemoHelper helper){
        final ArrayList<ListItem> data = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cs = db.rawQuery("select body, title, uuid from MEMO_TABLE", null);
            boolean eol = cs.moveToFirst();

            while (eol) {
                data.add(helper.getListItem(cs, db));
                eol = cs.moveToNext();
            }
            cs.close();
        } finally {
            db.close();
        }

        return data;
    }
}
