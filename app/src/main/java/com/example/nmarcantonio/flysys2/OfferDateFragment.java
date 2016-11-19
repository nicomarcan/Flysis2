package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nmarcantonio on 18/11/16.
 */
public class OfferDateFragment extends Fragment {

    View myView;
    private AppCompatActivity context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.offer_search_date, container, false);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = (AppCompatActivity)getActivity();
        TableLayout tab = (TableLayout)context.findViewById(R.id.on);



        int count = tab.getChildCount();
        for(int i = 0; i < count; i++){
            TableRow t =(TableRow) tab.getChildAt(i);

            t.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView)(((TableRow)v).getChildAt(1))).getText();
                    Toast.makeText(context,text,Toast.LENGTH_LONG).show();
                    // TODO: do your logic here

                }
            });

            }




    }
}
