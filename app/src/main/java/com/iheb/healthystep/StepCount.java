package com.iheb.healthystep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.CircularArray;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class StepCount extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView stepCounterTextView;
    private TextView calories;
    private TextView meters;
    private Button but;
    private boolean running = false;
    private int stepCount = 0;
    private DatabaseHelper dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        dbHandler = new DatabaseHelper(StepCount.this);


        stepCounterTextView = (TextView) findViewById(R.id.stepcounter);
        calories = (TextView) findViewById(R.id.calories);
        meters = (TextView) findViewById(R.id.meters);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCount=getStepCache();

        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
       if (getdayCache()!=day){
        //stepCounterTextView.setText(String.valueOf(day));
            // SQL QUERY STORE DATA

           int year = c.get(Calendar.YEAR);
           int month = c.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-indexed
           String date=day+"/"+month+"/"+year;
           double cal=(double)stepCount/20.0;
           double met=(double)stepCount/1.3123;
           dbHandler.addNewSteps(stepCount, date, cal, met);
           Toast.makeText(StepCount.this, "les Steps de ce jour sont enregistrés.", Toast.LENGTH_SHORT).show();


           stepCount=0;
           dayCache(day);

       }

        but= (Button)findViewById(R.id.history);
        but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                history_activity();
            }
        });

    }

    protected void dayCache(int day) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("day", day);

        editor.apply();
    }


    protected int getdayCache(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

// Get the value from the SharedPreferences object
        int value = sharedPreferences.getInt("day",0 );
        return value;
    }

        //store data in cache
    protected void stepCache(){
        // Get a reference to the SharedPreferences object

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

// Get a reference to the SharedPreferences editor

        SharedPreferences.Editor editor = sharedPreferences.edit();

// Put the value in the SharedPreferences object
        editor.putInt("counts", stepCount);

// Commit the changes
        editor.apply();
    }

    //get value from  cache
    protected int getStepCache(){
        // Get a reference to the SharedPreferences object
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

// Get the value from the SharedPreferences object
        int value = sharedPreferences.getInt("counts",0 );
        return value;
    }



    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            stepCounterTextView.setText("Step counter sensor not available!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        // if you unregister the last listener, the hardware will stop detecting step events
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            stepCount++;
            double cal=(double)stepCount/20.0;
            double met=(double)stepCount/1.3123;
            calories.setText(String.valueOf(cal)+" Calories burnt");
            meters.setText(String.valueOf(String.format("%.2f", met))+" Meters Crossed");

            stepCounterTextView.setText(String.valueOf(stepCount));
            stepCache();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }







    protected void history_activity(){
        Intent intent = new Intent(this,HistorySteps.class);
        startActivity(intent);
    }


}
