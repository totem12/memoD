package com.example.memod;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreatePage extends AppCompatActivity {

    private MemoHelper helper = null;
    private String id ="";
    private EditText body;
    private EditText title;
    private boolean isBodyEmpty = false;

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
                String bodyField = cs.getString(0);
                body = findViewById(R.id.text_body);
                body.setText(bodyField);

                String titleField = cs.getString(1);
                title = findViewById(R.id.editTitle);
                title.setText(titleField);

                eol = cs.moveToNext();
            }
            cs.close();
        } finally {
            db.close();
        }

        if(body.length() == 0){
            isBodyEmpty = true;
        }

        //戻る
        findViewById(R.id.return_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBodyEmpty){
                    helper.deleteMemo(id);
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

                String date = getNowDate();

                helper.updateMemo(data_body, data_title, id, date);

                finish();
            }
        });
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

}