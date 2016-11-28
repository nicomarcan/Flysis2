package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class OfferResults  extends AppCompatActivity{
    private View myView;
    private Activity act;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_results);
        findViewById(R.id.search_results_error).setVisibility(View.GONE);
        GetOfferInfo.values.clear();
        this.getSupportActionBar().setTitle(getResources().getString(R.string.offers_to)+" "+getIntent().getStringExtra("dest"));
        if(getIntent().getDoubleExtra("offerPrice",-1.0)==-1.0) {
            //Toast.makeText(this, getIntent().getStringExtra("currentCity")+" "+getIntent().getStringExtra("destCity")+" "+getIntent().getDoubleExtra("offerPrice",-1)+" "+getIntent().getDoubleExtra("ratio",1), Toast.LENGTH_SHORT).show();
            new GetOffersToDestTask(this, new Integer(getIntent().getStringExtra("filter")),getIntent().getDoubleExtra("ratio",1),findViewById(android.R.id.content)).execute(getIntent().getStringExtra("currentCity"), getIntent().getStringExtra("destCity"));
        }else {
         //   Toast.makeText(this, "JEJE", Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, getIntent().getStringExtra("currentCity")+" "+getIntent().getStringExtra("destCity")+" "+getIntent().getDoubleExtra("offerPrice",-1)+" "+getIntent().getDoubleExtra("ratio",1), Toast.LENGTH_SHORT).show();
            new GetOfferInfo(this, getIntent().getStringExtra("currentCity"), getIntent().getStringExtra("destCity"), getIntent().getDoubleExtra("offerPrice",-1),getIntent().getDoubleExtra("ratio",1),findViewById(android.R.id.content)).execute(2, 8);
        }



    }




    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
