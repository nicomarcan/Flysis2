package com.example.nmarcantonio.flysys2;

import android.content.Context;
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
    public FlightStatusArrayAdapter(Context context, List<FlightStatus> objects) {
        super(context, R.layout.flights_status_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        FlightStatus flightStatus = getItem(position);
        holder.header.setText("Vuelo " + flightStatus.number);
        holder.origin.setText(flightStatus.departure.airport.id);
        holder.destintation.setText(flightStatus.arrival.airport.id);
        holder.description.setText("Arribando dentro de 30 minutos");
        return convertView;
    }
}
