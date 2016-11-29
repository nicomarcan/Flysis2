package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.math.NumberUtils;

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
        getActivity().findViewById(R.id.back_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (AppCompatActivity)getActivity()).getSupportActionBar().show();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().remove(FlightsFragment.flightSearchFragment).commit();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getActivity().findViewById(R.id.back_search_button).setOnClickListener(null);

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
                id=  id.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                String number =  ((EditText)getActivity().findViewById(R.id.flight)).getText().toString();
                number = number.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                Intent intent = new Intent(getActivity(), FlightActivity.class);
                if(id.equals("")) {
                    Toast.makeText(getActivity(), R.string.air_identifier_missing, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(id.length() != 2 ||!id.matches("[a-zA-Z0-9][a-zA-Z0-9]")){
                    Toast.makeText(getActivity(), R.string.air_identifier_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(number.equals("")){
                    Toast.makeText(getActivity(), R.string.flight_missing, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!NumberUtils.isNumber(number)){
                    Toast.makeText(getActivity(), R.string.flight_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo == null || !networkInfo.isConnected()) {
                    Toast.makeText(getActivity(), R.string.no_internet_msg, Snackbar.LENGTH_LONG).show();
                    return;
                }

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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
