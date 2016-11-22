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

import java.util.HashMap;

/**
 * Created by nmarcantonio on 18/11/16.
 */
public class OfferDateFragment extends Fragment {

    View myView;
    private AppCompatActivity context;

    private City currentCity;
    private String destId;

    private HashMap<String,String> nameToId = new HashMap<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.offer_search_date, container, false);
         //currentCity = (City)(getArguments().getSerializable("currentCity"));
       // destId = getArguments().getString("destId");
        //nameToId = (HashMap<String,String>)(getArguments().getSerializable("nameToId"));

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
                    Bundle bundle = new Bundle();
                    bundle.putString("Text",text.toString());
                    Fragment frag = new OfferFilterFragment();
                    frag.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame,frag).addToBackStack("JEJE").commit();
                    // TODO: do your logic here

                }
            });

            }




    }
}
