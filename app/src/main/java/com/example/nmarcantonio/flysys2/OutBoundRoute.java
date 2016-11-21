package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/20/2016.
 */

public class OutBoundRoute {
    private String duration;
    private Segment[] segments;

    public OutBoundRoute(String duration,Segment[] segments){
        this.duration = duration;
        this.segments=segments;
    }

    public String getDuration() {
        return duration;
    }

    public Segment [] getSegments() {
        return segments;
    }
}
