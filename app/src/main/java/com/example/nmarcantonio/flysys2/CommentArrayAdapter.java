package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by traies on 20/11/16.
 */

public class CommentArrayAdapter extends ArrayAdapter<Comment>{

    public CommentArrayAdapter(Activity context, Comment[] objects) {
        super(context, R.layout.flight_comment_view_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItemHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_comment_view_item, parent, false);
            holder = new CommentItemHolder();
            holder.description = (TextView) convertView.findViewById(R.id.flight_comment_view_description);
            holder.overall = (RatingBar) convertView.findViewById(R.id.flight_comment_view_stars);
            holder.friendliness = (RatingBar) convertView.findViewById(R.id.flight_comment_friendliness_stars);
            holder.food = (RatingBar) convertView.findViewById(R.id.flight_comment_food_stars);
            holder.punctuality = (RatingBar) convertView.findViewById(R.id.flight_comment_punctuality_stars);
            holder.comfort = (RatingBar) convertView.findViewById(R.id.flight_comment_comfort_stars);
            holder.quality_price = (RatingBar) convertView.findViewById(R.id.flight_comment_quality_stars);
            holder.recommend = (ImageView) convertView.findViewById(R.id.flight_comment_view_thumbs);
            holder.mileage_program = (RatingBar) convertView.findViewById(R.id.flight_comment_mileage_stars);
            convertView.setTag(holder);
        } else {
            holder = (CommentItemHolder) convertView.getTag();
        }

        Comment comment = getItem(position);
        holder.description.setText(comment.description);
        holder.overall.setRating(comment.overall / 2);
        holder.friendliness.setRating(comment.friendliness / 2);
        holder.food.setRating(comment.food / 2);
        holder.mileage_program.setRating(comment.mileage_program / 2);
        holder.punctuality.setRating(comment.punctuality / 2);
        holder.comfort.setRating(comment.comfort / 2);
        holder.quality_price.setRating(comment.quality_price / 2);
        if (comment.recommended) {
            holder.recommend.setImageResource(R.drawable.ic_thumb_up_green_24dp);
        }
        else {
            holder.recommend.setImageResource(R.drawable.ic_thumb_down_red_24dp);
        }
        return convertView;
    }
}
