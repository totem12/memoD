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

    public ArrayList<ListItem> getData(){
        final ArrayList<ListItem> data = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor cs = db.rawQuery("select body, title, uuid from MEMO_TABLE", null);
            boolean eol = cs.moveToFirst();

            while (eol) {
                data.add(getListItem(cs, db));
                eol = cs.moveToNext();
            }
            cs.close();
        } finally {
            db.close();
        }

        return data;
    }

    public void deleteMemo(String idStr){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '" + idStr + "'");
            db.execSQL("DELETE FROM DATE_TABLE WHERE uuid = '" + idStr + "'");
        } finally {
            db.close();
        }
    }

    public void createMemo(String id){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.execSQL("insert into MEMO_TABLE(uuid, body) VALUES('"+ id +"', '"+ "')");
            db.execSQL("insert into DATE_TABLE(uuid, date, date2) VALUES('"+ id +"', '"+ "', '"+ "')");
        }finally {
            db.close();
        }
    }

    public void updateMemo(String data_body, String data_title, String id, String date){
        String date2 = "";
        String date3 = "";

        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor cs = db.rawQuery("select date, date2 from DATE_TABLE where uuid = '"+ id +"'", null);
            boolean eol = cs.moveToFirst();

            while (eol) {
                date2 = cs.getString(0);
                date3 = cs.getString(1);

                eol = cs.moveToNext();
            }
            cs.close();
            db.execSQL("update MEMO_TABLE set body = '"+ data_body +"', title = '"+ data_title+"' where uuid = '"+id+"'");
            db.execSQL("update DATE_TABLE set date = '"+ date +"', date2 = '"+ date2 +"', date3 = '"+ date3 +"' where uuid = '"+ id +"'");
        } finally{
            db.close();
        }
    }
}
