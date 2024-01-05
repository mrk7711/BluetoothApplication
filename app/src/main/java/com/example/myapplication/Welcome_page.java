package com.example.myapplication;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;

//This is our launcher page in this app
public class Welcome_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Welcome_page.this.startActivity(new Intent(Welcome_page.this,Bluetooth_.class));
                Welcome_page.this.finish();
            }
        },3000);

    }
}