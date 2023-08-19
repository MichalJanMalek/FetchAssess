package com.example.fetchassess;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import androidx.core.splashscreen.SplashScreen;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity:";
    private String queryCall;
    private ArrayList<SomeData> dataArrayList = new ArrayList<>();
    private ArrayList<SomeData> newArray = new ArrayList<>();
    private ArrayList<SomeData> filteredArray = new ArrayList<>();
    private ListView listRV;
    private CustomAdapter adapter;
    private ToggleButton tog;
    private int max = 0;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle the splash screen.
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        //initializing adapter class for recycler view
        listRV = (ListView) findViewById(R.id.itemListView);
        adapter = new CustomAdapter(dataArrayList, this, R.layout.list);
        listRV.setAdapter(adapter);
        listRV.setTextFilterEnabled(true);
        adapter.notifyDataSetChanged();

        //setting adapter class for recycler view
        getData();

        //toggles the sort to reverse the list and un-reverse
        tog = findViewById(R.id.toggle);
        tog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Collections.reverse(dataArrayList);
                    adapter.notifyDataSetChanged();
                }else {
                    Collections.reverse(dataArrayList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /*
        //allows user to search listview
        searchView = findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                MainActivity.this.adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
                return false;
            }
            public boolean onQueryTextChange(String newS){
                MainActivity.this.adapter.getFilter().filter(newS);
                adapter.notifyDataSetChanged();
                return false;
            }
        });*/
    }

    public void getData() {
        //query data from
        queryCall = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        RequestQueue requested = Volley.newRequestQueue(MainActivity.this);

        //see if I get here at least, ok I do now to get to the next level
        Log.d(TAG, "queryData");

        //JSONArray Query
        JsonArrayRequest jsonAr = new JsonArrayRequest(Request.Method.GET, queryCall, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                //dataArrayList.clear();

                try {
                    //strings we need to get from JSON file
                    String id;
                    String listId;
                    String name;

                    //get JSON array from internet
                    JSONArray allData = response;

                    //length of JSONArray
                    int length = allData.length();

                    //test to see if we get data from JSON
                    String test = allData.getJSONObject(0).getString("id");
                    Log.d(TAG, test);

                    //add from JSON file to our Java Class
                    for (int i = 0; i < length; i++) {
                        //variables to add to data class
                        id = allData.getJSONObject(i).getString("id");
                        listId = allData.getJSONObject(i).getString("listId");
                        name = allData.getJSONObject(i).getString("name");

                        //if empty, just TAG invalid, else add to array
                        if (name.equals("") || name.equals("null")) {
                            Log.d(TAG, "invalid");
                        } else {
                            dataArrayList.add(new SomeData(id, listId, name));
                            //Log.d(TAG, id);
                        }
                    }

                    filter();

                    listRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                // catch any JSON errors
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requested.add(jsonAr);
    }
    public void filter(){
        int l = this.dataArrayList.size();
        int max = 0;

        //find max value of listID
        for (int i = 0; i < l; i++) {
            String lNum = this.dataArrayList.get(i).getSomeListId();
            if (max < Integer.valueOf(lNum)) {
                max = Integer.valueOf(lNum);
            }
        }

        //check if we getting max value from listID
        Log.d("Max Value", String.valueOf(max));

        //Lists results by listID num
        Collections.sort(this.dataArrayList, new Comparator<SomeData>() {
            public int compare(SomeData lt, SomeData rt) {
                return lt.someListId.compareTo(rt.someListId);
            }
        });

        //split up array and added to filtered array
        for (int i = 1; i <= max; i++) {
            for (int j = 0; j < l; j++) {
                if (i == Integer.valueOf(dataArrayList.get(j).getSomeListId())) {
                    String f = dataArrayList.get(j).getSomeId();
                    String s = dataArrayList.get(j).getSomeListId();
                    String t = dataArrayList.get(j).getSomeName();
                    String all = f + " " + s + " " + t;
                    //Log.d(TAG, all);
                    newArray.add(new SomeData(f, s, t));
                }
            }

            //sort by id/name
            Collections.sort(newArray, new Comparator<SomeData>() {
                public int compare(SomeData lt, SomeData rt) {
                    return Integer.valueOf(lt.getSomeId()).compareTo(Integer.valueOf(rt.getSomeId()));
                }
            });

            //add sectioned out array into a new filter list
            filteredArray.addAll(newArray);
            newArray.clear();
        }

        //clear original array and replace it with filtered list
        dataArrayList.clear();
        dataArrayList.addAll(filteredArray);
    }

    public void seperatedLists(){
        int l = this.dataArrayList.size();

        //find max value of listID
        for (int i = 0; i < l; i++) {
            String lNum = this.dataArrayList.get(i).getSomeListId();
            if (max < Integer.valueOf(lNum)) {
                max = Integer.valueOf(lNum);
            }
        }
    }
}