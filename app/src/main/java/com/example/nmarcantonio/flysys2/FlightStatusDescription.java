package com.example.nmarcantonio.flysys2;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusDescription implements Serializable {
    String timeInterval;
    FlightStatus.FlightStatusState state;
    Date nextRelevantDate;
    long nextTimeZone;
    String descriptionHeader;
    String posDateDescription = null;

    public FlightStatusDescription(FlightStatus fi, Context context) {
        Date currentTime = new Date();
        long departureTimeZone = getTimeZone(fi.departure.airport.time_zone);
        long arrivalTimeZone = getTimeZone(fi.arrival.airport.time_zone);
        long currentTimeZone = TimeZone.getDefault().getRawOffset();
        try {
            switch(fi.status) {
                case "S":
                    /* PROGRAMADO */
                    Date departureTime;
                    if (fi.departure.actual_gate_time != null && !fi.departure.scheduled_gate_time.equals(fi.departure.actual_gate_time)) {
                        /* Hay DELAY EN ABORDAJE*/
                        departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.actual_gate_time);
                        Date departureScheduled = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                        posDateDescription = String.format(context.getString(R.string.flight_description_delay), CustomDateInterval.longInterval(departureScheduled, departureTimeZone, departureTime, departureTimeZone,  context));
                    }
                    else {
                        /* NO Hay DELAY EN ABORDAJE */
                        departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                    }
                    if (currentTime.getTime() - currentTimeZone < departureTime.getTime() - departureTimeZone) {
                        /* TODAVIA NO SE ABORDO */
                        state = FlightStatus.FlightStatusState.SCHEDULED;
                        descriptionHeader = context.getResources().getString(R.string.flight_description_header_scheduled);
                    }
                    else {
                        state = FlightStatus.FlightStatusState.BOARDING;
                        if (fi.departure.actual_time != null && !fi.departure.actual_time.equals(fi.departure.scheduled_time)) {
                            departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.actual_time);
                            Date departureScheduled = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_time);
                            posDateDescription = String.format(context.getString(R.string.flight_description_delay), CustomDateInterval.longInterval(departureScheduled, departureTimeZone, departureTime, departureTimeZone,  context));
                        }
                        else {
                            departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_time);
                            posDateDescription = "";
                        }
                        descriptionHeader = context.getResources().getString(R.string.flight_description_header_boarding);

                    }
                    nextRelevantDate = departureTime;
                    nextTimeZone = departureTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(departureTime, departureTimeZone, currentTime, currentTimeZone);
                    break;
                case "A":
                    /* ACTIVO */
                    Date arrivalTime;
                    if (fi.arrival.actual_time != null && !fi.arrival.actual_time.equals(fi.arrival.scheduled_time)) {
                        /* Hay DELAY EN ABORDAJE*/
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                        Date arrivalScheduled = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                        posDateDescription = String.format(context.getString(R.string.flight_description_delay), CustomDateInterval.longInterval(arrivalScheduled, arrivalTimeZone, arrivalTime, arrivalTimeZone,  context));
                    }
                    else {
                        /* NO Hay DELAY EN ABORDAJE */
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.FLYING;
                    descriptionHeader = context.getString(R.string.flight_description_header_flying);;
                    nextRelevantDate = arrivalTime;
                    nextTimeZone = arrivalTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(currentTime, currentTimeZone, arrivalTime, arrivalTimeZone);
                    break;
                case "R":
                    /* DESVIO */
                    Date atime;
                    if (fi.arrival.actual_time != null && !fi.arrival.actual_time.equals(fi.arrival.scheduled_time)) {
                        /* Hay DELAY EN ABORDAJE*/
                        atime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                        Date arrivalScheduled = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                        posDateDescription = String.format(context.getString(R.string.flight_description_delay), CustomDateInterval.longInterval(arrivalScheduled, arrivalTimeZone, atime, arrivalTimeZone,  context));
                    }
                    else {
                        /* NO Hay DELAY EN ABORDAJE */
                        atime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.DIVERT;
                    descriptionHeader = context.getString(R.string.flight_description_header_flying);
                    nextRelevantDate = atime;
                    nextTimeZone = arrivalTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(currentTime, currentTimeZone, atime, arrivalTimeZone);
                    break;
                case "L":
                    /* Aterrizado*/
                    Date landTime;
                    if (fi.arrival.actual_time != null) {
                        /* Hay DELAY EN ABORDAJE*/
                        landTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                    }
                    else {
                        /* NO Hay DELAY EN ABORDAJE */
                        landTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.LANDED;
                    nextRelevantDate = landTime;
                    nextTimeZone = arrivalTimeZone;
                    descriptionHeader = context.getString(R.string.flight_description_header_landed);
                    timeInterval = CustomDateInterval.significantInterval(landTime, arrivalTimeZone, currentTime, currentTimeZone);
                    break;
                case "C":
                    /* CANCELADO */
                    state = FlightStatus.FlightStatusState.CANCELLED;
                    descriptionHeader = context.getString(R.string.flight_descriptin_header_cancelled);
                    nextRelevantDate = null;
                    nextTimeZone = 0;
                    timeInterval = CustomDateInterval.ZERO;
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildDescription(Date currentDate, Context context) {
        String ret = "";
        long currentTimeZone = TimeZone.getDefault().getRawOffset();
        if (state.equals(FlightStatus.FlightStatusState.LANDED)) {
            ret += String.format(descriptionHeader, CustomDateInterval.longInterval(nextRelevantDate, nextTimeZone, currentDate, currentTimeZone, context));
        }
        else if (!state.equals(FlightStatus.FlightStatusState.CANCELLED)) {
            ret += String.format(descriptionHeader, CustomDateInterval.longInterval(currentDate, currentTimeZone, nextRelevantDate, nextTimeZone, context));
            if (posDateDescription != null && posDateDescription != "" ) {
                ret += "\n " + posDateDescription;
            }
            else if (currentDate.getTime() - currentTimeZone  > nextRelevantDate.getTime() - nextTimeZone) {
                ret += "\n" + String.format(context.getString(R.string.flight_description_delay), CustomDateInterval.longInterval(nextRelevantDate, nextTimeZone, currentDate, currentTimeZone, context));
            }
        }
        else {
            ret += descriptionHeader;
        }
        return ret;
    }

    private long getTimeZone(String timeZoneString) {
        if (timeZoneString == null) {
            return 0;
        }
        String hoursString = timeZoneString.split(":")[0];
        return Integer.valueOf(hoursString) * 60 * 60 * 1000;
    }
}
