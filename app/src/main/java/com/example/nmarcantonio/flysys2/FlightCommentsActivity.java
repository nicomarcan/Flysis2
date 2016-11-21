package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class FlightCommentsActivity extends AppCompatActivity{
    final String TAG = "FlightCommentsActivity";
    CommentArrayAdapter adapter;
    ArrayList<Comment>[] comments;
    ListView list;
    int[] lastPage;
    int sort = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final FlightCommentsActivity a = this;
        getMenuInflater().inflate(R.menu.comments_menu, menu);
        MenuItem desc = menu.findItem(R.id.orderDescending);
        desc.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            FlightCommentsActivity activity = a;
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (activity.getSortOrder() != "0") {
                    activity.setSort(0);
                    CommentArrayAdapter adapter = activity.getAdapter();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    list.smoothScrollToPosition(0);
                    if (activity.getComments() != null) {
                        adapter.addAll(activity.getComments());
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        new GetCommentsTask(findViewById(R.id.flight_comments_list_view), activity)
                                .execute("AR", "5260", "0", activity.getSortOrder());
                    }
                }
                return true;
            }
        });

        MenuItem asc = menu.findItem(R.id.orderAscending);
        asc.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            FlightCommentsActivity activity = a;
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (activity.getSortOrder() != "1") {
                    activity.setSort(1);
                    CommentArrayAdapter adapter = activity.getAdapter();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    list.smoothScrollToPosition(0);
                    if (activity.getComments() != null) {
                        adapter.addAll(activity.getComments());
                        adapter.notifyDataSetChanged();

                    } else {
                        new GetCommentsTask(findViewById(R.id.flight_comments_list_view), activity)
                                .execute("AR", "5260", "0", activity.getSortOrder());
                    }
                }
                return true;
            }
        });

        MenuItem refresh = menu.findItem(R.id.flight_comment_refresh_button);
        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            FlightCommentsActivity activity = a;
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.comments[0] = null;
                activity.comments[1] = null;
                activity.list.smoothScrollToPosition(0);
                activity.getAdapter().clear();
                activity.getAdapter().notifyDataSetChanged();
                activity.setAdapter(null);
                new GetCommentsTask(findViewById(R.id.flight_comments_list_view), activity)
                        .execute("AR", "5260", "0", activity.getSortOrder());
                return true;
            }
        });
        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_comments_activity);
        setTitle("Vuelo 1");
        final FlightCommentsActivity a = this;

        ListView list = (ListView) findViewById(R.id.flight_comments_list_view);
        this.list = list;
        adapter = null;
        comments = new ArrayList[2];
        comments[0] = null;
        comments[1] = null;


        list.setOnScrollListener(new EndlessScrollListener(1, 0, 0) {
            FlightCommentsActivity outer = a;
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                new GetCommentsTask(findViewById(R.id.flight_comments_list_view), outer)
                        .execute("AR", "5260", String.valueOf(page), outer.getSortOrder());
                return true;
            }
        });
        new GetCommentsTask(findViewById(R.id.flight_comments_list_view), a).execute("AR", "5260", "0", getSortOrder());
    }

    public CommentArrayAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CommentArrayAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<Comment> getComments() {
        return comments[sort];
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments[sort] = new ArrayList<>(comments);
    }

    public String getSortOrder(){ return String.valueOf(sort); }

    public void setSort(int sort) { this.sort = sort;}
}
