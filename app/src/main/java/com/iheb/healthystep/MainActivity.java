package com.iheb.healthystep;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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