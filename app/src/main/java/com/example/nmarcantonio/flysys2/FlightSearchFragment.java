package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Nicolas on 11/26/2016.
 */

public class FlightSearchFragment extends Fragment {

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.flight_search, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (AppCompatActivity)getActivity()).getSupportActionBar().show();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().remove(FlightsFragment.flightSearchFragment).commit();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FlightsFragment.searching=false;
            }
        });


        EditText airline = ((EditText)getActivity().findViewById(R.id.airline));
        int options = airline.getImeOptions();
        ((EditText)getActivity().findViewById(R.id.airline)).setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        airline.setFocusableInTouchMode(true);
        airline.requestFocus();

        //open keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(airline, InputMethodManager.SHOW_IMPLICIT);

        options = ((EditText)getActivity().findViewById(R.id.flight)).getImeOptions();
        ((EditText)getActivity().findViewById(R.id.flight)).setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        getActivity().findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = ((EditText)getActivity().findViewById(R.id.airline)).getText().toString().toUpperCase();
                String number =  ((EditText)getActivity().findViewById(R.id.flight)).getText().toString();
                Intent intent = new Intent(getActivity(), FlightActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("number",number);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(getActivity())
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                builder.setContentIntent(pendingIntent);
                startActivity(intent);

            }
        });

    }


}
