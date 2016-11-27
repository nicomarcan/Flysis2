package com.example.nmarcantonio.flysys2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusDescription implements Serializable {
    CustomDateInterval.SignificantTimeInterval timeInterval;
    FlightStatus.FlightStatusState state;
    Date nextRelevantDate;
    long nextTimeZone;
    String descriptionHeader;
    String posDateDescription = null;

    public FlightStatusDescription(FlightStatus fi) {
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
                        /* HAY DELAY EN ABORDAJE*/
                        departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);

                        posDateDescription = "(hay un retraso de " + CustomDateInterval.longInterval(departureTime, departureTimeZone, currentTime, currentTimeZone) + ")";

                    }
                    else {
                        /* NO HAY DELAY EN ABORDAJE */
                        departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                    }
                    if (currentTime.compareTo(departureTime) < 0) {
                        /* TODAVIA NO SE ABORDO */
                        state = FlightStatus.FlightStatusState.SCHEDULED;
                        descriptionHeader = "Abordaje comienza en ";
                    }
                    else {
                        state = FlightStatus.FlightStatusState.BOARDING;
                        if (fi.departure.actual_time != null && !fi.departure.actual_time.equals(fi.departure.scheduled_time)) {
                            departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_time);
                            posDateDescription = "(hay un retraso de " + CustomDateInterval.longInterval(departureTime, departureTimeZone, currentTime, currentTimeZone) + ")";
                        }
                        else {
                            departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                        }
                        descriptionHeader = "Despega en ";

                    }
                    nextRelevantDate = departureTime;
                    nextTimeZone = departureTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(departureTime, departureTimeZone, currentTime, currentTimeZone);
                    break;
                case "A":
                    /* ACTIVO */
                    Date arrivalTime;
                    if (fi.arrival.actual_time != null) {
                        /* HAY DELAY EN ABORDAJE*/
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                    }
                    else {
                        /* NO HAY DELAY EN ABORDAJE */
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.FLYING;
                    descriptionHeader = "Aterrizando en ";
                    nextRelevantDate = arrivalTime;
                    nextTimeZone = arrivalTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(currentTime, currentTimeZone, arrivalTime, arrivalTimeZone);
                    break;
                case "R":
                    /* DESVIO */
                    Date atime;
                    if (fi.arrival.actual_time != null) {
                        /* HAY DELAY EN ABORDAJE*/
                        atime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                    }
                    else {
                        /* NO HAY DELAY EN ABORDAJE */
                        atime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.DIVERT;
                    descriptionHeader = "Aterrizando en ";
                    nextRelevantDate = atime;
                    nextTimeZone = arrivalTimeZone;
                    timeInterval = CustomDateInterval.significantInterval(currentTime, currentTimeZone, atime, arrivalTimeZone);
                    break;
                case "L":
                    /* Aterrizado*/
                    Date landTime;
                    if (fi.arrival.actual_time != null) {
                        /* HAY DELAY EN ABORDAJE*/
                        landTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.actual_time);
                    }
                    else {
                        /* NO HAY DELAY EN ABORDAJE */
                        landTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.arrival.scheduled_time);
                    }
                    state = FlightStatus.FlightStatusState.LANDED;
                    nextRelevantDate = landTime;
                    nextTimeZone = arrivalTimeZone;
                    descriptionHeader = "Aterrizado hace ";
                    timeInterval = CustomDateInterval.significantInterval(currentTime, currentTimeZone, landTime, arrivalTimeZone);
                    break;
                case "C":
                    /* CANCELADO */
                    state = FlightStatus.FlightStatusState.CANCELLED;
                    descriptionHeader = "El vuelo se cancelo.";
                    nextRelevantDate = null;
                    nextTimeZone = 0;
                    timeInterval = CustomDateInterval.SignificantTimeInterval.ZERO;
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildDescription(Date currentDate) {
        String ret = "";
        ret += descriptionHeader;
        long currentTimeZone = TimeZone.getDefault().getRawOffset();
        if (!state.equals(FlightStatus.FlightStatusState.CANCELLED)) {
            ret += CustomDateInterval.longInterval(currentDate, currentTimeZone, nextRelevantDate, nextTimeZone);
            if (posDateDescription != null) {
                ret += posDateDescription;
            }
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
