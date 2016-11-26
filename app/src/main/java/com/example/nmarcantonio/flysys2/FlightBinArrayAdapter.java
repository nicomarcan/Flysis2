package com.example.nmarcantonio.flysys2;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by saques on 25/11/2016.
 */

public class FlightBinArrayAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<FlightShort> flights;



    public FlightBinArrayAdapter(Context context, List<FlightShort> objects){
        this.context = context;
        this.flights = objects;
        BinPreferencesHelper.registerFlightBinAdapter(this);
    }

    public void update(){
        flights = BinPreferencesHelper.getFlightsInBin(context);
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe1;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return (SwipeLayout)LayoutInflater.from(context).inflate(R.layout.flight_bin_item_swipeable,parent,false);
    }

    @Override
    public void fillValues(final int position, View convertView){
        FlightShort flight = (FlightShort) getItem(position);
        final SwipeLayout swipeLayout = (SwipeLayout)convertView;
        FlightBinHolder holder;

        holder = new FlightBinHolder();
        holder.header = (TextView) swipeLayout.findViewById(R.id.flights_bin_fulldescr);
        holder.origin = (TextView) swipeLayout.findViewById(R.id.flights_card_origin1);
        holder.destintation = (TextView) swipeLayout.findViewById(R.id.flights_card_destination1);
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
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        ViewTreeObserver.OnGlobalLayoutListener swipeGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                swipeLayout.close(false);
            }
        };

        swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(swipeGlobalLayoutListener);

        swipeLayout.findViewById(R.id.bin_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightShort flight = flights.get(position);
                String s = flight.getId()+"-"+flight.getNumber();
                BinPreferencesHelper.deleteFlight(flight,context);
                Toast.makeText(context,s+" "+context.getString(R.string.eliminated), Toast.LENGTH_SHORT).show();
                update();
            }
        });

        swipeLayout.findViewById(R.id.bin_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightShort flight = flights.get(position);
                String s = flight.getId()+"-"+flight.getNumber();
                BinPreferencesHelper.deleteFlight(flight,context);
                ArrayList<FlightStatus> statuses = PreferencesHelper.getFlights(context);
                statuses.add(flight.getStatus());
                PreferencesHelper.updatePreferences(statuses,context);
                Toast.makeText(context,s+" "+context.getString(R.string.undo), Toast.LENGTH_SHORT).show();
                update();
            }
        });

        swipeLayout.getSurfaceView().setOnClickListener(new SwipeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightShort flight = flights.get(position);
                Intent intent = new Intent(context, FlightActivity.class);
                intent.putExtra("id",flight.getId());
                intent.putExtra("number",new Integer(flight.getNumber()).toString());

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

        holder.header.setText(flight.getAirlineInfo().name + " - " + flight.getNumber());
        holder.origin.setText(flight.getDeparture().id);
        holder.destintation.setText(flight.getArrival().id);
    }

    @Override
    public int getCount() {
        return flights.size();
    }

    @Override
    public Object getItem(int position) {
        return flights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
