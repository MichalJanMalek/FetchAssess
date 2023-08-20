package com.example.fetchassess;

import android.os.Bundle;
import android.util.Log;
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


        //allows user to search listview
        searchView = findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }
            public boolean onQueryTextChange(String s) {

                ArrayList<SomeData> queryArray = new ArrayList<>();
                for (int i = 0; i < dataArrayList.size(); i++) {
                    SomeData data = dataArrayList.get(i);
                    if((data.getSomeName().toLowerCase().contains(s)) || (data.getSomeId().toLowerCase().contains(s))){
                        queryArray.add(data);
                    }
                }

                CustomAdapter newAdapt = new CustomAdapter(queryArray, MainActivity.this, R.layout.list);
                listRV.setAdapter(newAdapt);

                return false;
            }
        });
    }

    public void getData() {
        //ArrayList<SomeData> dataArrayList = new ArrayList<>();
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
                            //Log.d(TAG, "invalid");
                        } else {
                            dataArrayList.add(new SomeData(id, listId, name));
                            //Log.d(TAG, id);
                        }
                    }

                    filter(dataArrayList);

                    //initializing adapter class for recycler view
                    listRV = (ListView) findViewById(R.id.itemListView);
                    adapter = new CustomAdapter(dataArrayList, MainActivity.this, R.layout.list);
                    listRV.setAdapter(adapter);
                    listRV.setTextFilterEnabled(true);
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
    public void filter(ArrayList<SomeData> list){

        int l = list.size();
        int max = 0;

        //find max value of listID
        for (int i = 0; i < l; i++) {
            String lNum = list.get(i).getSomeListId();
            if (max < Integer.valueOf(lNum)) {
                max = Integer.valueOf(lNum);
            }
        }

        //check if we getting max value from listID
        Log.d("Max Value", String.valueOf(max));

        //Lists results by listID num
        Collections.sort(list, new Comparator<SomeData>() {
            public int compare(SomeData lt, SomeData rt) {
                return lt.someListId.compareTo(rt.someListId);
            }
        });

        //split up array and added to filtered array
        for (int i = 1; i <= max; i++) {
            for (int j = 0; j < l; j++) {
                if (i == Integer.valueOf(list.get(j).getSomeListId())) {
                    String id = list.get(j).getSomeId();
                    String listId = list.get(j).getSomeListId();
                    String name = list.get(j).getSomeName();

                    //String all = id + " " + s + " " + t;
                    //Log.d(TAG, all);

                    newArray.add(new SomeData(id, listId, name));
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
        list.clear();
        list.addAll(filteredArray);
    }
}