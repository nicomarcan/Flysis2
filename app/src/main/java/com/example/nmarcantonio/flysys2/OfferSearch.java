package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nmarcantonio on 18/11/16.
 */
public class OfferSearch extends AppCompatActivity {

    View myView;
    private AppCompatActivity context;


    private Menu mMenu;
    private Double ratio;
    private String srcId;
    private String destId;
    public static Integer filter=0;
    public static int asd = 0;




    private HashMap<String,String> nameToId = new HashMap<>();
    private ArrayList<String> autoCompStrings = new ArrayList<String>();





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_search);
        context = this;
        ratio = getIntent().getDoubleExtra("ratio",1);
       srcId = getIntent().getStringExtra("scrId");
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame_search,new OfferDateFragment()).commit();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.



        getMenuInflater().inflate(R.menu.offers_input, menu);
        mMenu = menu;
        MenuItem searchItem = menu.findItem(R.id.offer_search);
       final  SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.requestFocus();

        new GetCitiesTask(this,searchView).execute();
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                destId = nameToId.get(query.toLowerCase());
                Intent intent = new Intent(context, OfferResults.class);
                intent.putExtra("filter", filter.toString());
                intent.putExtra("currentCity", srcId);
                intent.putExtra("destCity", destId);
                intent.putExtra("ratio",ratio);
                //Toast.makeText(context, filter.toString()+" "+srcId+" "+destId+" "+ratio.toString(), Toast.LENGTH_SHORT).show();
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                searchView.clearFocus();
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });


        return super.onCreateOptionsMenu(menu);
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



    private class GetCitiesTask extends AsyncTask<String, Void, String> {


        private Context context;



        private SearchView searchView;

        public GetCitiesTask(Context context, SearchView searchView) {
            this.context = context;
            this.searchView = searchView;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            String ret = null, order;
            try {

                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcities&page_size=1000");
                conn = (HttpURLConnection) new URL(url.toString()).openConnection();

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
                if (!obj.has("cities")) {

                    return;
                }
                else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<City>>() {
                    }.getType();

                    String jsonFragment = obj.getString("cities");


                    ArrayList<City> cities = gson.fromJson(jsonFragment, listType);

                    for (City a : cities) {
                        autoCompStrings.add(a.getName().split(",")[0]);
                        nameToId.put(a.getName().split(",")[0].toLowerCase(),a.getId());
                    }

                        Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
                        set.addAll(autoCompStrings);
                        autoCompStrings = new ArrayList<String>(set);


                        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                        searchAutoComplete.setTextColor(Color.WHITE);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.autocomplete_layout, autoCompStrings);
                        searchAutoComplete.setAdapter(adapter);

                        SearchManager searchManager;

                        searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
                        searchView.setSearchableInfo(
                                searchManager.getSearchableInfo(getComponentName()));

                        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                            @Override
                            public boolean onSuggestionSelect(int position) {
                                return false;
                            }

                            @Override
                            public boolean onSuggestionClick(int position) {
                                searchView.setQuery(adapter.getItem(position), false);
                                return true;
                            }
                        });


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
                Log.d("err", "fallo la conexion");
                return null;
            }
        }


    }


}
