package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FlightActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flight_info_activity);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.flight_info_map);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.flight_info_collapsible_toolbar);
        ctl.setTitle("Vuelo 1");
        ctl.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.flight_info_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        final Activity context = this;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     finish();
                                                 }
                                             }
        );


        new GetFlightInfoTask(findViewById(R.id.flight_info_coordination), mapFragment, this)
                .execute("AR", "5260");
    }
}
