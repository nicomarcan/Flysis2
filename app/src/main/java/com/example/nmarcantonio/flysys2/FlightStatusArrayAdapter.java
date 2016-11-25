package com.example.nmarcantonio.flysys2;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusArrayAdapter extends ArraySwipeAdapter<FlightStatus> {
    private Context context;
    private List<FlightStatus> flights;

    public FlightStatusArrayAdapter(Context context, List<FlightStatus> objects) {
        super(context, R.layout.flights_status_item, objects);
        this.flights=objects;
        this.context = context;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.flights_list_view;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FlightStatus flightStatus = (FlightStatus)getItem(position);
        SwipeLayout swipeLayout = null;
        FlightStatusHolder holder;
        if (convertView == null) {
            swipeLayout = (SwipeLayout)LayoutInflater.from(getContext()).inflate(R.layout.flight_status_item_swipeable,parent,false);
            holder = new FlightStatusHolder(flightStatus.airline.id,flightStatus.number);
            holder.header = (TextView) swipeLayout.findViewById(R.id.flights_card_header);
            holder.origin = (TextView) swipeLayout.findViewById(R.id.flights_card_origin);
            holder.destintation = (TextView) swipeLayout.findViewById(R.id.flights_card_destination);
            holder.description = (TextView) swipeLayout.findViewById(R.id.flights_card_description);
            swipeLayout.setTag(holder);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                    FlightStatusHolder holder = (FlightStatusHolder)layout.getTag();
                    String s = holder.getId()+"-"+holder.getNumber();
                    int i;
                    for(i = 0; i < flights.size(); i++) {
                        FlightStatus f = flights.get(i);
                        if (f.airline.id.equals(holder.getId()) && f.number == holder.getNumber()) {
                            flights.remove(i);
                            break;
                        }
                    }
                    PreferencesHelper.updatePreferences((ArrayList)flights, context);
                    layout.getSurfaceView().setOnClickListener(null);
//                    layout.removeAllViews();
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });
        }
        else {
            holder = (FlightStatusHolder) convertView.getTag();
            swipeLayout = (SwipeLayout)convertView;
        }


        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightStatus flightStatus = (FlightStatus)getItem(position);
                Intent intent = new Intent(context, FlightActivity.class);
                intent.putExtra("id",flightStatus.airline.id);
                intent.putExtra("number",new Integer(flightStatus.number).toString());

                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                context.startActivity(intent);
            }
        });




        holder.header.setText("Vuelo " + flightStatus.number);
        holder.origin.setText(flightStatus.departure.airport.id);
        holder.destintation.setText(flightStatus.arrival.airport.id);
        holder.description.setText("Arribando dentro de 30 minutos");
        return swipeLayout;
    }
}
