package com.example.memod;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class CreatePage extends AppCompatActivity {

    private MemoHelper helper = null;
    private String id ="";
    private EditText body;
    private EditText title;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);

        helper = new MemoHelper(CreatePage.this);
        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor cs = db.rawQuery("select body, title from MEMO_TABLE where uuid = '"+ id +"'", null);
            boolean eol = cs.moveToFirst();
            while (eol) {
                String Body = cs.getString(0);
                body = findViewById(R.id.text_body);
                body.setText(Body);

                String Title = cs.getString(1);
                title = findViewById(R.id.editTitle);
                title.setText(Title);

                eol = cs.moveToNext();
            }
            cs.close();
        } finally {
            db.close();
        }

        if(body.length() == 0){
            flag = true;
        }

        //戻る
        findViewById(R.id.return_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    SQLiteDatabase db = helper.getWritableDatabase();
                    try {
                        db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '" + id + "'");
                    } finally {
                        db.close();
                    }
                }
                finish();
            }
        });

        //登録
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body = findViewById(R.id.text_body);
                String data_body = body.getText().toString();

                title = findViewById(R.id.editTitle);
                String data_title = title.getText().toString();

                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    db.execSQL("update MEMO_TABLE set body = '"+ data_body +"', title = '"+ data_title+"' where uuid = '"+id+"'");
                } finally{
                    db.close();
                }

                finish();
            }
        });
    }

}