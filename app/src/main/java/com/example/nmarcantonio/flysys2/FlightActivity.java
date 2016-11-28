package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FlightActivity extends Activity{
    private static String TAG = "FlightActivity";

    ArrayList<FlightStatus> flights;
    Map<TextView, Tuple> map = new HashMap<>();
    private Timer timer;
    private TimerTask timerTask;
    String number;
    String airline;
    FlightStatus flightStatus;
    DialogFragment dialogFragment;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            FlightStatus flightStatus = (FlightStatus) bundle.get(FlightsIntentService.FLIGHT_STATUS);
            if (flightStatus.airline.id.equals(airline) && number.equals(String.valueOf(flightStatus.number))) {
                updateFlightStatus(flightStatus);
                abortBroadcast();
            }
        }
    };
    public class Tuple {
        String header;
        Date date;

        public Tuple(String header, Date date) {
            this.header = header;
            this.date = date;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FlightsIntentService.ACTION_GET_FLIGHT);
        intentFilter.setPriority(2);
        registerReceiver(broadcastReceiver, intentFilter);

        /*
        try {
            Log.d(TAG, "onResume: a");
            timer = new Timer();
            timerTask = new UpdateIntervalTimerTask(map, this);
            timer.schedule(timerTask, 0, 1000);
            Log.d(TAG, "onResume: b");
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        */
        /* updateAllFlights(); */

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flight_info_activity);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.flight_info_map);
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.flight_info_collapsible_toolbar);
        ctl.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.flight_info_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }
        );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_flight_fab);
        fab.hide();
        if (savedInstanceState != null) {
            dialogFragment = (DialogFragment) getFragmentManager().getFragment(savedInstanceState, "CommentDialogFragment");
            airline = savedInstanceState.getString("airline");
            number = savedInstanceState.getString("number");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            airline = bundle.getString("id");
            number = bundle.getString("number");
        }
        if (number != null) {
            String flightDescr = getResources().getString(R.string.flight);
            ctl.setTitle(flightDescr + " " + number);
        }

        final Activity context = this;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                flights = PreferencesHelper.getFlights(context);

                new GetFlightInfoTask(
                        new FlightInfoCallback(findViewById(R.id.flight_info_coordination), mapFragment, context, flights)
                ).execute(airline, number);



            }
        };
        new Handler().post(r);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogFragment = null;
    }
    private void updateFlightStatus(FlightStatus fi) {
        View flightView = findViewById(R.id.flight_info_coordination);
        ((TextView)flightView.findViewById(R.id.flight_origin_content)).setText(fi.departure.airport.city.name.split(",")[0]);
        ((TextView)flightView.findViewById(R.id.flight_destination_content_2)).setText(fi.arrival.airport.city.name.split(",")[0]);
        String statusString;
        String statusDescription = "";
        int statusColor;
        Date arrivalTime = null;
        Date currentTime = new Date();
        String stringHeader = null;
        fi.setDescription(this);
        FlightStatusDescription fd = fi.flightStatusDescription;
        switch (fd.state) {
            case SCHEDULED:
                statusString = getResources().getString(R.string.flight_info_status_scheduled);
                statusColor = flightView.getResources().getColor(R.color.colorGreen);
                break;
            case BOARDING:
                statusString = getResources().getString(R.string.flight_info_status_boarding);
                statusColor = flightView.getResources().getColor(R.color.colorGreen);
                break;
            case FLYING:
                statusString = getResources().getString(R.string.flight_info_status_flying);
                statusColor = flightView.getResources().getColor(R.color.colorGreen);
                break;
            case DIVERT:
                statusString = getResources().getString(R.string.flight_info_status_divert);
                statusColor = flightView.getResources().getColor(R.color.colorRed);
                break;
            case CANCELLED:
                statusString = getResources().getString(R.string.flight_info_status_cancelled);
                statusColor = flightView.getResources().getColor(R.color.colorRed);
                break;
            case LANDED:
                statusString = getResources().getString(R.string.flight_info_status_landed);
                statusColor = flightView.getResources().getColor(R.color.colorGreen);
                break;
            default:
                statusString = getResources().getString(R.string.flight_info_status_unknown);
                statusColor = flightView.getResources().getColor(R.color.colorRed);
                break;
        }

        ((TextView)flightView.findViewById(R.id.flight_info_status)).setTextColor(statusColor);
        ((TextView)flightView.findViewById(R.id.flight_info_status)).setText(statusString);
        TextView dateText = (TextView) flightView.findViewById(R.id.flight_info_status_description);
        dateText.setText(fi.flightStatusDescription.buildDescription(new Date(), this));
    }

    void showPostCommentDialog(Boolean thumbs, String airline, String number) {
        if (dialogFragment == null || !((DialogFragment)dialogFragment).isResumed() ) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentById(R.id.flight_comment_dialog);
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }
            fragmentTransaction.addToBackStack(null);
            if (dialogFragment == null ) {
                dialogFragment = CommentDialogFragment.newInstance(thumbs, airline, number );
            }
            ((CommentDialogFragment) dialogFragment).setThumbs(thumbs);


            dialogFragment.show(fragmentTransaction, "dialog");

        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (dialogFragment != null && dialogFragment.isResumed()) {
            getFragmentManager().putFragment(bundle, "CommentDialogFragment", dialogFragment);
        }
        if (number != null && airline != null) {
            bundle.putString("number", number);
            bundle.putString("airline", airline);
        }
        super.onSaveInstanceState(bundle);
    }
}
