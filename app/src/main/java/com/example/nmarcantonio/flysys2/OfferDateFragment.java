package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by nmarcantonio on 18/11/16.
 */
public class OfferDateFragment extends Fragment {

    View myView;
    private AppCompatActivity context;

    private City currentCity;
    private String destId;


    private HashMap<String,String> nameToId = new HashMap<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.offer_search_date, container, false);
        setHasOptionsMenu(true);
        OfferSearch.filter = 0;
        //currentCity = (City)(getArguments().getSerializable("currentCity"));

        // nameToId = (HashMap<String,String>)(getArguments().getSerializable("nameToId"));
        Bundle b = getArguments();
        // Toast.makeText(getActivity(), currentCity.getName(), Toast.LENGTH_SHORT).show();
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = (AppCompatActivity)getActivity();
        TableLayout tab = (TableLayout)context.findViewById(R.id.on);


        int count = tab.getChildCount();
        for(int i = 0; i < count; i++){
            TableRow t =(TableRow) tab.getChildAt(i);

            t.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CharSequence text = ((TextView)(((TableRow)v).getChildAt(1))).getText();
                    if(text.toString().equals("Dentro de 1 mes")){
                        OfferSearch.filter = 1;
                    }else if(text.toString().equals("Dentro de 3 meses")){
                        OfferSearch.filter = 3;
                    }else if(text.toString().equals("Dentro de 6 meses")){
                        OfferSearch.filter = 6;
                    }else if(text.toString().equals("Dentro de 1 año")){
                        OfferSearch.filter = 12;
                    }else if(text.toString().equals("Después de 1 año")){
                        OfferSearch.filter = 13;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("Text",text.toString());
                    Fragment frag = new OfferFilterFragment();
                    frag.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame_search, frag)
                            .addToBackStack(null)
                            .commit();

                }
            });



        }

            setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.offer_search);
        final  SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if(OfferSearch.filter ==0)
                    context.onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return true;
    }
}
