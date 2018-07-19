package com.example.nikhil.devopedia.About;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nikhil.devopedia.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( "About Us" );
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
