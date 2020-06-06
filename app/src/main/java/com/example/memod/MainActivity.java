package com.example.memod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    MemoHelper helper = null;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onResume() {
        helper = new MemoHelper(MainActivity.this);

        final ArrayList<HashMap<String, String>> List = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor data_set = db.rawQuery("select uuid, body from MEMO_TABLE order by id", null);
            boolean first = data_set.moveToFirst();
            while (first) {
                HashMap<String, String> data = new HashMap<>();
                data.put("id", data_set.getString(0));
                data.put("body", data_set.getString(1));
                List.add(data);

                first = data_set.moveToNext();
            }
            data_set.close();
        } finally {
            db.close();
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                List,
                android.R.layout.simple_list_item_2,
                new String[]{"body", "id"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        final ListView listView = findViewById(R.id.List);
        listView.setAdapter(simpleAdapter);

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
                TwoLineListItem two = (TwoLineListItem) view;
                TextView idTextView = two.getText2();
                String idStr = (String) idTextView.getText();

                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '" + idStr + "'");
                } finally {
                    db.close();
                }
                List.remove(position);
                simpleAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwoLineListItem two = (TwoLineListItem) view;
                TextView idTextView = two.getText2();
                String Id = (String) idTextView.getText();

                Intent intent = new Intent(MainActivity.this, CreatePage.class);
                intent.putExtra("id", Id);
                startActivity(intent);

            }
        });

        super.onResume();


    }


}