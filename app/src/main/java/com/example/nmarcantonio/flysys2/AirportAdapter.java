package com.example.nmarcantonio.flysys2;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nmarcantonio on 18/11/16.
 */

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import static com.example.nmarcantonio.flysys2.R.id.imageView;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.AirportViewHolder> {

    private List<Airport> airportList;
    private Location loc;
    private Context c;

    public class AirportViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView distance;

        public AirportViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.airport_info);
            distance = (TextView) view.findViewById(R.id.airport_distance_info);
        }
    }


    public AirportAdapter(List<Airport> airportList, Location loc, Context c) {
        this.airportList = airportList;
        this.loc = loc;
        this.c = c;
    }

    @Override
    public AirportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.airport_view_item, parent, false);

        return new AirportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AirportViewHolder holder, int position) {
        Airport airport = airportList.get(position);
        Location locB = new Location(loc);
        locB.setLatitude(airport.getLatitude());
        locB.setLongitude(airport.getLongitude());
        double distance = loc.distanceTo(locB);

        String [] splitdescr = airport.getDescription().split(", ");

        holder.distance.setText((int)(distance/1000) + " km" );
        holder.name.setText(splitdescr[0] + '\n' +
                                    splitdescr[1] + ", " + splitdescr[2] + '\n');

    }

    @Override
    public int getItemCount() {
        return airportList.size();
    }
}



