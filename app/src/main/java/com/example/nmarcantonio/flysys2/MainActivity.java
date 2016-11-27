package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.phenotype.Configuration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = "MainActivity";
    TextView resultTextView;
    final static String DEALS_NAME = "deals";
    private Activity context;
    private static int currentSect = R.id.nav_flights;
    private Menu mMenu;
    private AirportsFragment af;




    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;


      //  resultTextView = (TextView) findViewById(R.id.result);

        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (currentSect == R.id.nav_flights) {
           // Toast.makeText(this,"CABE",Toast.LENGTH_LONG).show();
            fragmentManager.beginTransaction().replace(R.id.content_frame,new FlightsFragment()).commit();
        } else if (currentSect == R.id.nav_offers) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new OffersFragment()).addToBackStack("HOLA").commit();
        } else if (currentSect == R.id.nav_airports) {

        } else if (currentSect == R.id.nav_conversor) {

        } else if (currentSect == R.id.nav_bin) {

        } else if (currentSect == R.id.nav_configuration) {

        }
;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        new GetAirlinesLogos(this).execute();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setAlarm();

    }


    /**
     * Handle here the permissions obtained in the fragments
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        if(currentSect == R.id.nav_airports){
            af.afterLocationRequest();
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Menu getmMenu(){
        return mMenu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.



        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;


        return super.onCreateOptionsMenu(menu);
    }


    public void setCurrentSect(int num){
        currentSect = num;
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_flights) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame,new FlightsFragment()).commit();
        } else if (id == R.id.nav_offers) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new OffersFragment()).addToBackStack("HOLA").commit();
        } else if (id == R.id.nav_airports) {
            af = new AirportsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame,af).addToBackStack(null).commit();
        } else if (id == R.id.nav_conversor) {

        } else if (id == R.id.nav_bin) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new BinFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_configuration) {
            Intent intent = new Intent(this, SettingsActivity.class);

            PendingIntent pendingIntent =
                    TaskStackBuilder.create(this)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(intent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentIntent(pendingIntent);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmNotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, 0, i, 0
        );
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                60000,
                pi
        );
        Log.d("aaa", "onReceive: ");
    }


}
