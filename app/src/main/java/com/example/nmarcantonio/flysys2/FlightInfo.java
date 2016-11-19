package com.example.nmarcantonio.flysys2;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class FlightInfo {
    AirportInfo airport;
    String scheduled_time;
    String actual_time;
    String scheduled_gate_time;
    String actual_gate_time;
    String gate_delay;
    String estimate_runway_time;
    String actual_runway_time;
    String runway_delay;

    public FlightInfo(AirportInfo airport, String scheduled_time, String actual_time, String scheduled_gate_time, String actual_gate_time, String gate_delay, String estimate_runway_time, String actual_runway_time, String runway_delay) {
        this.airport = airport;
        this.scheduled_time = scheduled_time;
        this.actual_time = actual_time;
        this.scheduled_gate_time = scheduled_gate_time;
        this.actual_gate_time = actual_gate_time;
        this.gate_delay = gate_delay;
        this.estimate_runway_time = estimate_runway_time;
        this.actual_runway_time = actual_runway_time;
        this.runway_delay = runway_delay;
    }
}
