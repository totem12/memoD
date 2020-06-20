package com.example.memod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";
    private MemoHelper helper = null;
    private String id;
    private MyListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate()");

    }

    @Override
    protected void onResume() {
        helper = new MemoHelper(MainActivity.this);

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cs = db.rawQuery("select body, title, uuid from MEMO_TABLE order by id", null);
            Cursor dcs = db.rawQuery("select date, date2, date3 from DATE_TABLE", null);
            final ArrayList<ListItem> data = new ArrayList<>();
            boolean first = cs.moveToFirst();
            boolean second = dcs.moveToFirst();
            while (first && second) {
                ListItem item = new ListItem();

                item.setBody(cs.getString(0));
                item.setTitle(cs.getString(1));
                item.setUuid(cs.getString(2));

                item.setDate(dcs.getString(0));
                item.setDate2(dcs.getString(1));
                item.setDate3(dcs.getString(2));

                data.add(item);

                first = cs.moveToNext();
                second = dcs.moveToFirst();
            }
            adapter = new MyListAdapter(this, data,R.layout.list_item);
            listView = findViewById(R.id.List);
            listView.setAdapter(adapter);
            cs.close();
            dcs.close();
        } finally {
            db.close();
        }

        //新規作成
        findViewById(R.id.new_memo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = UUID.randomUUID().toString();
                Intent intent = new Intent(MainActivity.this, CreatePage.class);
                intent.putExtra("id", id);

                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    db.execSQL("insert into MEMO_TABLE(uuid, body) VALUES('"+ id +"', '"+ "')");
                    db.execSQL("insert into DATE_TABLE(uuid, date) VALUES('"+ id +"', '"+ "')");
                }finally {
                    db.close();
                }
                startActivity(intent);
            }
        });

        //削除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String idStr = adapter.getId(position);

                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '" + idStr + "'");
                    db.execSQL("DELETE FROM DATE_TABLE WHERE uuid = '" + idStr + "'");
                } finally {
                    db.close();
                }

                adapter.remove(position);
                Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        //タッチしたメモにとぶ
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Id = adapter.getId(position);

                Intent intent = new Intent(MainActivity.this, CreatePage.class);
                intent.putExtra("id", Id);
                startActivity(intent);

            }
        });

        super.onResume();


    }


}