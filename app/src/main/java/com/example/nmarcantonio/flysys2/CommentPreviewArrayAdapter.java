package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by traie_000 on 11/27/2016.
 */

public class CommentPreviewArrayAdapter extends ArrayAdapter<Comment> {
    public CommentPreviewArrayAdapter(Context context, List<Comment> list) {
        super(context, R.layout.flight_commentary_preview_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItemHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_commentary_preview_item, parent, false);
            holder = new CommentItemHolder();
            holder.description = (TextView) convertView.findViewById(R.id.flight_comment_preview_description);
            holder.overall = (RatingBar) convertView.findViewById(R.id.flight_comment_preview_stars);
            holder.recommend = (ImageView) convertView.findViewById(R.id.flight_comment_preview_thumbs);
            convertView.setTag(holder);
        } else {
            holder = (CommentItemHolder) convertView.getTag();
        }

        Comment comment = getItem(position);
        holder.description.setText(comment.description);
        holder.overall.setRating(comment.overall / 2);
        if (comment.recommended) {
            holder.recommend.setImageResource(R.drawable.ic_thumb_up_green_24dp);
        }
        else {
            holder.recommend.setImageResource(R.drawable.ic_thumb_down_red_24dp);
        }
        return convertView;
    }
}
