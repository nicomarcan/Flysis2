package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FlightCommentsActivity extends AppCompatActivity{
    final String TAG = "FlightCommentsActivity";
    @Override
    public void onCreate(Bundle savedInstancceState) {
        super.onCreate(savedInstancceState);
        setContentView(R.layout.flight_comments_activity);
        setTitle("Vuelo 1");

        new GetCommentsTask(findViewById(R.id.flight_comments_list_view), this).execute("AR", "5260");
    }
}
