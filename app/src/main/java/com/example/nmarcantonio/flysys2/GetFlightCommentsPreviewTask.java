package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by traie_000 on 11/27/2016.
 */

public class GetFlightCommentsPreviewTask extends AsyncTask<String, Void, String> {

    View commentView;
    Context context;

    GetFlightCommentsPreviewTask(View commentView, Context context) {
        super();
        this.commentView = commentView;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        String ret = null, order;
        try {
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("hci.it.itba.edu.ar")
                    .appendPath("v1")
                    .appendPath("api")
                    .appendPath("review.groovy")
                    .appendQueryParameter("method", "getairlinereviews")
                    .appendQueryParameter("airline_id", strings[0])
                    .appendQueryParameter("flight_number", strings[1])
                    .appendQueryParameter("sort_key", "rating")
                    .appendQueryParameter("sort_order", "desc")
                    .appendQueryParameter("page", "0")
                    .appendQueryParameter("page_size", "4")
                    .build();
            conn = (HttpURLConnection) new URL(uri.toString()).openConnection();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            ret = readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            if (!obj.has("reviews")) {
                return;
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<CommentInfo>() {
                }.getType();
                CommentInfo ci = gson.fromJson(obj.toString(), listType);
                ArrayList<Comment> comments = new ArrayList<>();
                for (int i = 0; i < ci.reviews.length; i++) {
                    CommentInfo.ReviewInfo ri = ci.reviews[i];
                    if (ri.comments == null) {
                        ri.comments = "null";
                    }
                    comments.add(new Comment(
                            Uri.decode(StringEscapeUtils.unescapeHtml4(ri.comments)),
                            ri.yes_recommend,
                            ri.rating.overall,
                            ri.rating.friendliness,
                            ri.rating.food,
                            ri.rating.punctuality,
                            ri.rating.mileage_program,
                            ri.rating.comfort,
                            ri.rating.quality_price
                    ));
                }
                CommentPreviewArrayAdapter adapter = new CommentPreviewArrayAdapter(context, comments);
                final ListView listView = (ListView) commentView.findViewById(R.id.flight_comments_preview_list);
                listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                View p = (View) parent.getParent();
                                p.performClick();
                            }
                        }
                );
                listView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listView);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String readStream(InputStream inputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int i = inputStream.read();
            while(i != -1 ) {
                outputStream.write(i);
                i = inputStream.read();
            }
            return outputStream.toString();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void setText(Activity activity, int id, String str) {
        if (str == null) {
            str = "-";
        }
        ((TextView) activity.findViewById(id))
                .setText(str);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, FrameLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
