package com.example.nmarcantonio.flysys2;

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

    public class AirportViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView cityTextView;
        public TextView distanceTextView;

        public AirportViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.airport_info);
            cityTextView = (TextView) view.findViewById(R.id.airport_info_2);
            distanceTextView = (TextView) view.findViewById(R.id.airport_distance);
        }
    }


    public AirportAdapter(List<Airport> airportList,Location loc) {
        this.airportList = airportList;
        this.loc = loc;
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

        String [] splitdescr = airport.getDescription().split(",");

        holder.nameTextView.setText(splitdescr[0]);
        holder.cityTextView.setText(splitdescr[1] + ", " + splitdescr[2]);
        holder.cityTextView.setText("@id@+id/airport_distance" + ": " + distance);

    }

    @Override
    public int getItemCount() {
        return airportList.size();
    }
}



