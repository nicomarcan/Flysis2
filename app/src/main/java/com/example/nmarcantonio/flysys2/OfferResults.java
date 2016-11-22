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

        if(getIntent().getStringExtra("offerPrice")==null)
            new GetOffersToDestTask(this).execute(getIntent().getStringExtra("currentCity"),getIntent().getStringExtra("destCity"));
        else
            new GetOfferInfo(this,getIntent().getStringExtra("currentCity"),getIntent().getStringExtra("destCity"),new Double(getIntent().getStringExtra("offerPrice"))).execute(2);
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
