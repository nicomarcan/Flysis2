package com.example.nmarcantonio.flysys2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusDescription implements Serializable {
    CustomDateInterval.SignificantTimeInterval timeInterval;
    FlightStatus.FlightStatusState state;
    Date nextRelevantDate;
    String descriptionHeader;
    String posDateDescription = null;
    public FlightStatusDescription(FlightStatus fi) {
        Date currentTime = new Date();
        try {
            switch(fi.status) {
                case "S":
                    /* PROGRAMADO */
                    Date departureTime;
                    if (fi.departure.actual_gate_time != null && !fi.departure.scheduled_gate_time.equals(fi.departure.actual_gate_time)) {
                        /* HAY DELAY EN ABORDAJE*/
                        departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                        posDateDescription = "(hay un retraso de " + CustomDateInterval.longInterval(departureTime, currentTime) + ")";
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
                            posDateDescription = "(hay un retraso de " + CustomDateInterval.longInterval(departureTime, currentTime) + ")";
                        }
                        else {
                            departureTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "arg")).parse(fi.departure.scheduled_gate_time);
                        }
                        descriptionHeader = "Despega en ";

                    }
                    nextRelevantDate = departureTime;
                    timeInterval = CustomDateInterval.significantInterval(currentTime, departureTime);
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
                    timeInterval = CustomDateInterval.significantInterval(currentTime, arrivalTime);
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
                    timeInterval = CustomDateInterval.significantInterval(currentTime, atime);
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
                    descriptionHeader = "Aterrizado hace ";
                    timeInterval = CustomDateInterval.significantInterval(currentTime, landTime);
                    break;
                case "C":
                    /* CANCELADO */
                    state = FlightStatus.FlightStatusState.CANCELLED;
                    descriptionHeader = "El vuelo se cancelo.";
                    nextRelevantDate = null;
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
        if (!state.equals(FlightStatus.FlightStatusState.CANCELLED)) {
            ret += CustomDateInterval.longInterval(currentDate, nextRelevantDate);
            if (posDateDescription != null) {
                ret += posDateDescription;
            }
        }
        return ret;
    }
}
