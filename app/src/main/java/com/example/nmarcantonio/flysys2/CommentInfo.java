package com.example.nmarcantonio.flysys2;

import java.io.Serializable;

/**
 * Created by traies on 20/11/16.
 */

public class CommentInfo implements Serializable{
    int page;
    int page_size;
    int total;
    ReviewInfo[] reviews;

    public CommentInfo(int page, int page_size, int total, ReviewInfo[] reviews) {
        this.page = page;
        this.page_size = page_size;
        this.total = total;
        this.reviews = reviews;
    }

    class ReviewInfo implements Serializable{
        FlightReviewInfo flight;
        RatingInfo rating;
        boolean yes_recommend;
        String comments;

        public ReviewInfo(FlightReviewInfo flight, RatingInfo rating, boolean yes_recommend, String comments) {
            this.flight = flight;
            this.rating = rating;
            this.yes_recommend = yes_recommend;
            this.comments = comments;
        }
    }
    class FlightReviewInfo implements Serializable{
        int number;
        AirlineFlightInfo airline;

        public FlightReviewInfo(int number, AirlineFlightInfo airline) {
            this.number = number;
            this.airline = airline;
        }
    }
    class AirlineFlightInfo implements Serializable{
        String id;

        public AirlineFlightInfo(String id) {
            this.id = id;
        }
    }
    class RatingInfo implements Serializable{
        int overall;
        int friendliness;
        int food;
        int punctuality;
        int mileage_program;
        int comfort;
        int quality_price;
        public RatingInfo(int overall, int friendliness, int food, int punctuality, int mileage_program, int comfort, int quality_price) {
            this.overall = overall;
            this.friendliness = friendliness;
            this.food = food;
            this.punctuality = punctuality;
            this.mileage_program = mileage_program;
            this.comfort = comfort;
            this.quality_price = quality_price;
        }
    }
}
