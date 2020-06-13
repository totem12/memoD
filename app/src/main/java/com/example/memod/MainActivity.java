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
import java.util.HashMap;
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

        final ArrayList<HashMap<String, String>> List = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cs = db.rawQuery("select uuid, body, title from MEMO_TABLE order by id", null);
            final ArrayList<ListItem> data = new ArrayList<>();
            boolean first = cs.moveToFirst();
            while (first) {
                ListItem item = new ListItem();

                item.setUuid(cs.getString(0));
                item.setBody(cs.getString(1));
                item.setTitle(cs.getString(2));

                data.add(item);

                first = cs.moveToNext();
            }
            adapter = new MyListAdapter(this, data,R.layout.list_item);
            listView = findViewById(R.id.List);
            listView.setAdapter(adapter);
            cs.close();
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