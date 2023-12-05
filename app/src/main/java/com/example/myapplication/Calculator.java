package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {
    EditText    n1,n2;
    TextView    result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        init();

    }

    public void init()
    {
         n1=findViewById(R.id.editTextText4);
         n2 =findViewById(R.id.editTextText7);
         result=findViewById(R.id.textView12);
    }

    public void add(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double y = Double.parseDouble(n2.getText().toString());
        double z = x + y;
            result.setText(String.valueOf(z));
    }

    public void sub(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double y = Double.parseDouble(n2.getText().toString());
        double z = x - y;
        result.setText(String.valueOf(z));
    }

    public void mul(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double y = Double.parseDouble(n2.getText().toString());
        double z = x * y;
        result.setText(String.valueOf(z));
    }
    public void dev(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double y = Double.parseDouble(n2.getText().toString());
        if (y==0)
            result.setText("Second Number is zero and we cant devide that!!!");
        else {
            double z = x / y;
            result.setText(String.valueOf(z));
             }
    }

    public void sqrt(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double z = Math.sqrt(x);
        result.setText(String.valueOf(z));
    }

    public void pow(View v){
        double x =Double.parseDouble(n1.getText().toString());
        double y =Double.parseDouble(n2.getText().toString());
        double z = Math.pow(x,y);
        result.setText(String.valueOf(z));
    }

    public void db1(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double z = 20*Math.log10(x);
        result.setText(String.valueOf(z));
    }

    public void db2(View v){
        double x = Double.parseDouble(n1.getText().toString());
        double z = Math.pow(10,(x/20));
        result.setText(String.valueOf(z));
    }
}
