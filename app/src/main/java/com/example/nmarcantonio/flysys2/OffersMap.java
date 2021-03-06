package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nmarcantonio on 17/11/16.
 */
public class OffersMap extends AppCompatActivity  {


    private GoogleMap mMap;
    private Double firstPrice=Double.MIN_VALUE;
    private Double seventhPrice=Double.MAX_VALUE;
    private Double secondPrice;
    private Double thirdPrice;
    private Double fourthPrice;
    private Double fifthPrice;
    private Double sixthPrice;


    View myView;
    final static String DEALS_NAME = "deals";
    private Activity context;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> values;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private Integer selected;
    private String srcId;
    private double ratio;
    private HashMap<String,String> nameToId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_map);
        context=this;
        srcId = getIntent().getStringExtra("srcId");
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_map);
        }
        new HttpGetTask().execute();

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class HttpGetTask extends AsyncTask<Void, Void, String> implements OnMapReadyCallback {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getlastminuteflightdeals&from=BUE");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch (Exception exception) {
                exception.printStackTrace();
                return "Unexpected Error";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.has(MainActivity.DEALS_NAME))
                    return ;
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Deal>>() {
                }.getType();

                String jsonFragment = obj.getString(MainActivity.DEALS_NAME);


                ArrayList<Deal> dealList = gson.fromJson(jsonFragment, listType);
              //  final RecyclerView view = (RecyclerView) findViewById(R.id.offer_view);

              //  if (view != null) {
                    values = new ArrayList<Product>();

                    for (int j = 0; j <dealList.size(); j++) {
                        ratio = getIntent().getDoubleExtra("ratio",1);
                        double price = dealList.get(j).getPrice()*ratio;
                        nameToId.put(dealList.get(j).getName().split(",")[0],dealList.get(j).getId());
                        values.add (j, new Product(dealList.get(j).getId(), dealList.get(j).getName(), price,dealList.get(j).getLatitude(),dealList.get(j).getLongitude() ));
                        if(price < seventhPrice)
                            seventhPrice = price;
                        if(price > firstPrice)
                           firstPrice= price;
                    }
                   fourthPrice = (firstPrice + seventhPrice)/2;
                Double aux = (firstPrice + fourthPrice)/2;
                secondPrice = (aux+firstPrice)/2;
                thirdPrice = (aux+fourthPrice)/2;
                aux = (fourthPrice+seventhPrice)/2;
                fifthPrice = (aux+fourthPrice)/2;
                sixthPrice = (aux + seventhPrice)/2;


                new GetPhotosToCache(context,this,dealList,0,findViewById(android.R.id.content)).execute();







            } catch (Exception exception) {
                //  resultTextView.append(new Integer("10").toString());
            }
            ;
        }

        private String readStream(InputStream inputStream) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int i = inputStream.read();
                while (i != -1) {
                    outputStream.write(i);
                    i = inputStream.read();
                }
                return outputStream.toString();

            } catch (IOException e) {
                return "";
            }
        }

        @Override
        public void onMapReady(final GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);



            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker arg0) {
                    View view = getLayoutInflater().inflate(R.layout.offer_map_selected_item, null);

                    CardView image = (CardView) view.findViewById(R.id.offer_map_image);
                    //new GetFlickrPhotoTask(context,image).execute("BuenosAires","LON");
                    image.setBackground(new BitmapDrawable(CacheImages.getInstance().getImagesMap().get(arg0.getTitle().split("&")[2]) ));
                    TextView title = (TextView) view.findViewById(R.id.offer_map_name);
                    title.setText(arg0.getTitle().split("&")[0]);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


                    TextView price = (TextView) view.findViewById(R.id.offer_map_price);
                    price.setText("$"+prefs.getString("money_list","USD")+" "+arg0.getTitle().split("&")[1]);

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        public void onInfoWindowClick(Marker marker) {
                            String destId = nameToId.get(marker.getTitle().split("&")[0]);
                            Double offerPrice = new Double(marker.getTitle().split("&")[3]);

                            Intent intent = new Intent(context, OfferResults.class);

                            intent.putExtra("currentCity", srcId);
                            intent.putExtra("destCity", destId);
                            intent.putExtra("offerPrice",offerPrice);
                            intent.putExtra("ratio",ratio);
                            intent.putExtra("dest",marker.getTitle().split("&")[0]);
                           // Toast.makeText(context, srcId+" "+destId+" "+offerPrice+" "+ratio, Toast.LENGTH_SHORT).show();
                            PendingIntent pendingIntent =
                                    TaskStackBuilder.create(context)
                                            // add all of DetailsActivity's parents to the stack,
                                            // followed by DetailsActivity itself
                                            .addNextIntentWithParentStack(intent)
                                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                            builder.setContentIntent(pendingIntent);
                            startActivity(intent);
                        }

                    });

                    return view;
                }
            });

            LatLng a;
            int i = 0;
            for(Product p : values){
                a =   new LatLng(p.getLatitude(),p.getLongitude());
                float color;
                if(p.getPrice() >= (firstPrice+secondPrice)/2)
                    color = BitmapDescriptorFactory.HUE_RED;
                else if(p.getPrice() >= (secondPrice+thirdPrice)/2)
                    color = 15;
                else if(p.getPrice() >= (thirdPrice+fourthPrice)/2)
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                else if(p.getPrice() >= (fifthPrice+fourthPrice)/2)
                    color = 45;
                else if(p.getPrice() >= (fifthPrice+sixthPrice)/2)
                    color = BitmapDescriptorFactory.HUE_YELLOW;
                else if(p.getPrice() >= (sixthPrice+seventhPrice)/2)
                    color =75;
                else
                    color = 105;
                markers.add(i,mMap.addMarker(new MarkerOptions().position(a).title(p.getName().split(",")[0]+"&"+ String.format ("%.2f", p.getPrice())+"&"+p.getId()+"&"+p.getPrice()).icon(BitmapDescriptorFactory.defaultMarker(color))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(a));
                i++;
            }
                Product p = values.get(0);

            float zoomLevel = (float)3.0; //This goes up to 21
         //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), zoomLevel));
            ((TextView)findViewById(R.id.first_price)).setText(new Integer(firstPrice.intValue()).toString());
            ((TextView)findViewById(R.id.second_price)).setText(new Integer(secondPrice.intValue()).toString());
            ((TextView)findViewById(R.id.third_price)).setText(new Integer(thirdPrice.intValue()).toString());
            ((TextView)findViewById(R.id.fourth_price)).setText(new Integer(fourthPrice.intValue()).toString());
            ((TextView)findViewById(R.id.fifth_price)).setText(new Integer(fifthPrice.intValue()).toString());
            ((TextView)findViewById(R.id.sixth_price)).setText(new Integer(sixthPrice.intValue()).toString());
            ((TextView)findViewById(R.id.seventh_price)).setText(new Integer(seventhPrice.intValue()).toString());

            ((LinearLayout) context.findViewById(R.id.loading)).setVisibility(View.GONE);
            ((LinearLayout) context.findViewById(R.id.loading_blur)).setVisibility(View.GONE);

        }
    }






}