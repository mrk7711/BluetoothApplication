package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//I want to use buuton so i write next line
import android.widget.Button;

public class Second_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        Button b1= findViewById(R.id.start_button);

        Button b2= findViewById(R.id.start2_button);

        Button b3=findViewById(R.id.start3_but);

        Button b4=findViewById(R.id.start4_but);

        Button b5=findViewById(R.id.button2);




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Second_Page.this.startActivity(new Intent(Second_Page.this,MainActivity.class));
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Second_Page.this.startActivity(new Intent(Second_Page.this,Enterpage.class));
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Second_Page.this.startActivity(new Intent(Second_Page.this,Calculator.class));
            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Second_Page.this.startActivity(new Intent(Second_Page.this, Bluetooth_.class));
            }
        });


        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Second_Page.this.startActivity(new Intent(Second_Page.this, PuzzleGame.class));
            }
        });

    }
}


