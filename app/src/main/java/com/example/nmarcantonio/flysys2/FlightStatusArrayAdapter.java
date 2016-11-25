package com.example.nmarcantonio.flysys2;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusArrayAdapter extends ArrayAdapter<FlightStatus> {
    private Context context;

    public FlightStatusArrayAdapter(Context context, List<FlightStatus> objects) {
        super(context, R.layout.flights_status_item, objects);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FlightStatusHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flights_status_item, parent, false);
            holder = new FlightStatusHolder();
            holder.header = (TextView) convertView.findViewById(R.id.flights_card_header);
            holder.origin = (TextView) convertView.findViewById(R.id.flights_card_origin);
            holder.destintation = (TextView) convertView.findViewById(R.id.flights_card_destination);
            holder.description = (TextView) convertView.findViewById(R.id.flights_card_description);
            convertView.setTag(holder);
        }
        else {
            holder = (FlightStatusHolder) convertView.getTag();
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightStatus flightStatus = getItem(position);
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



        FlightStatus flightStatus = getItem(position);
        holder.header.setText("Vuelo " + flightStatus.number);
        holder.origin.setText(flightStatus.departure.airport.id);
        holder.destintation.setText(flightStatus.arrival.airport.id);
        holder.description.setText("Arribando dentro de 30 minutos");
        return convertView;
    }
}
