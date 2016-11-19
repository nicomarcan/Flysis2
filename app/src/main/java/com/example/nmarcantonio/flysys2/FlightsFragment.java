package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by nmarcantonio on 17/11/16.
 */
public class FlightsFragment extends Fragment {
    AppCompatActivity context;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myView = inflater.inflate(R.layout.flights_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_flights);
            return myView;
        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity)getActivity();
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Vuelos");
        }

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.flight_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FlightActivity.class);

                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                startActivity(intent);
            }
        });
    }
}

