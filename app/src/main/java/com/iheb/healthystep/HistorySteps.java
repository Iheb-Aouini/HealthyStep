package com.iheb.healthystep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HistorySteps extends AppCompatActivity {

    private Button but ;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db ;
    private Context c;
    private TextView dateview;
    private SimpleCursorAdapter adapter;
  //  final String[] from = new String[] { DatabaseHelper.calories, DatabaseHelper.date, DatabaseHelper.step_val,DatabaseHelper.meters };
  //  final int[] to = new int[] { R.id.id, R.id.title, R.id.desc };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

       String test="";

       dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Cursor cursor = db.query("data_steps", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * from data_steps",null);
        if (cursor != null ) {
            int i=0;
            if  (cursor.moveToFirst()) {
                do {
                    i++;
                    String cal = cursor.getString(cursor.getColumnIndexOrThrow("calories"));
                    String meters = cursor.getString(cursor.getColumnIndexOrThrow("meters"));
                    String steps = cursor.getString(cursor.getColumnIndexOrThrow("step_val"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));


                    test=test +"- DAY : "+i+"  "+ "ON THE DATE : "+date +" STEPS DONE = "+steps+" , METERS CROSSED = "+meters+" ,CALORIES BURNT = "+cal+"\n ";
                }while (cursor.moveToNext());
            }
        }

        /*while (cursor.moveToNext()) {
            //int id = cursor.getInt(cursor.getColumnIndex(""));
            String name = cursor.getString(cursor.getColumnIndex("date"));
            //String email = cursor.getString(cursor.getColumnIndex("email"));
            test=name;
         //   Log.d("SQLITE", "ID: " + id + " Name: " + name + " Email: " + email);
        }*/

      //  adapter = new SimpleCursorAdapter(this, R.layout.activity_history, cursor, from, to, 0);
        //adapter.notifyDataSetChanged();


        cursor.close();
        db.close();

        dateview = (TextView) findViewById(R.id.dateview);

        dateview.setText(test);
        but= (Button)findViewById(R.id.suivant);
        but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                step_activity();
            }
        });

    }

    protected void step_activity(){
        Intent intent = new Intent(this,StepCount.class);
        startActivity(intent);
    }



}
