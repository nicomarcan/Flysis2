package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FlightInfoDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_info_detail_activity);
        FlightStatus fi = (FlightStatus) getIntent().getSerializableExtra("flight_info");
        fi.setDescription(this);
        String statusString;
        switch (fi.flightStatusDescription.state) {
            case LANDED:
                statusString = getResources().getString(R.string.flight_info_status_landed);
                break;
            case BOARDING:
                statusString = getResources().getString(R.string.flight_info_status_boarding);
                break;
            case SCHEDULED:
                statusString = getResources().getString(R.string.flight_info_status_scheduled);
                break;
            case FLYING:
                statusString = getResources().getString(R.string.flight_info_status_flying);
                break;
            case DIVERT:
                statusString = getResources().getString(R.string.flight_info_status_divert);;
                break;
            case CANCELLED:
                statusString = getResources().getString(R.string.flight_info_status_cancelled);;
                break;
            default:
                statusString = getResources().getString(R.string.flight_info_status_unknown);;
                break;
        }
        setText(this, R.id.flight_info_status_content, statusString);
        setText(this, R.id.flight_info__airline_content, fi.airline.name + " (" + fi.airline.id + ")");
        setText(this, R.id.flight_info_origin_content, fi.departure.airport.city.name );
        setText(this, R.id.flight_info_destination_content, fi.arrival.airport.city.name );
        setText(this, R.id.flight_info_departure_time_content, formatTime(fi.departure.actual_time, fi.departure.airport.time_zone));
        setText(this, R.id.flight_info_scheduled_departure_time_content, formatTime(fi.departure.scheduled_time, fi.departure.airport.time_zone));
        setText(this, R.id.flight_info_boarding_time_content, formatTime(fi.departure.actual_gate_time, fi.departure.airport.time_zone));
        setText(this, R.id.flight_info_scheduled_boarding_time_content,formatTime(fi.departure.scheduled_gate_time, fi.departure.airport.time_zone));
        setText(this, R.id.flight_info_arrival_time_content,formatTime(fi.arrival.actual_time, fi.arrival.airport.time_zone));
        setText(this, R.id.flight_info_scheduled_arrival_time_content,formatTime(fi.arrival.scheduled_time, fi.arrival.airport.time_zone));
        setText(this, R.id.flight_info_departure_airport_terminal_content,formatTime(fi.arrival.scheduled_time, fi.departure.airport.time_zone));
        setText(this, R.id.flight_info_origin_airport_content, fi.departure.airport.description.split(",")[0] +
                " (" + fi.departure.airport.id + ")");
        setText(this, R.id.flight_info_destination_airport_content, fi.arrival.airport.description.split(",")[0] +
                " (" + fi.arrival.airport.id + ")");
        setText(this, R.id.flight_info_departure_date_content, formatDate(fi.departure.scheduled_time));
        String gate_departure, gate_arrival;
        String flight_descr = getResources().getString(R.string.flight);
        setTitle(flight_descr + " " + String.valueOf(fi.number));
        if (fi.departure.airport.terminal == null || fi.departure.airport.gate == null) {
            gate_departure = "-";
        }
        else {
            gate_departure = fi.departure.airport.terminal + " " + fi.departure.airport.gate;
        }
        setText(this, R.id.flight_info_departure_airport_terminal_content, gate_departure);

        if (fi.arrival.airport.terminal == null || fi.arrival.airport.gate == null) {
            gate_arrival = "-";
        }
        else {
            gate_arrival = fi.arrival.airport.terminal + " " + fi.arrival.airport.gate;
        }
        setText(this, R.id.flight_info_arrival_airport_terminal_content, gate_arrival);

    }

    private static String formatTime(String og, String timeZone) {
        String formated = "";
        if (og == null) {
            formated = "-";
            return formated;
        }
        try {
            Date time;
            time = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "AR")).parse(og);
            formated = new SimpleDateFormat("kk:mm", new Locale("es", "AR")).format(time);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formated + " (UTC " + timeZone + ")" ;
    }

    private static String formatDate(String og) {
        String formated = "";
        try {
            Date date;
            date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "AR")).parse(og);

            String language = Locale.getDefault().getDisplayLanguage();
            if ("es".equals(language)) {
                formated = new SimpleDateFormat(" dd 'de' MMMM 'de' yyyy", new Locale("es", "AR")).format(date);
            }
            else {
                formated = new SimpleDateFormat(" MMMM dd ',' yyyy", new Locale("en", "US")).format(date);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formated;
    }

    private static void setText(Activity activity, int id, String str) {
        if (str == null) {
            str = "-";
        }
        ((TextView) activity.findViewById(id))
                .setText(str);
    }
}
