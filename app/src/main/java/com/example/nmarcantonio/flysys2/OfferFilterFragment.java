package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nicolas on 11/22/2016.
 */

public class OfferFilterFragment extends Fragment {

    View myView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.selected_date_layout, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView cross = (ImageView)getActivity().findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OffersFragment.filter = 0;
                getActivity().onBackPressed();
            }
        });
        TextView textView = (TextView)getActivity().findViewById(R.id.filter_text);
        textView.setText(getArguments().getString("Text"));
    }


}
