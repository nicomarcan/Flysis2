package com.example.nmarcantonio.flysys2;

import java.io.Serializable;

/**
 * Created by traies on 20/11/16.
 */

public class Comment implements Serializable{
    String description;
    boolean recommended;
    float overall;
    float friendliness;
    float food;
    float punctuality;
    float mileage_program;
    float comfort;
    float quality_price;

    public Comment(String description, boolean recommended, float overall, float friendliness, float food, float punctuality, float mileage_program, float comfort, float quality_price) {
        this.description = description;
        this.recommended = recommended;
        this.overall = overall;
        this.friendliness = friendliness;
        this.food = food;
        this.punctuality = punctuality;
        this.mileage_program = mileage_program;
        this.comfort = comfort;
        this.quality_price = quality_price;
    }
}
