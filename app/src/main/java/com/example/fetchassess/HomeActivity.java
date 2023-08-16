package com.example.fetchassess;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.example.fetchassess.databinding.ActivityHomeBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity:";
    private String queryCall;
    ArrayList<SomeData> dataArrayList;
    private RecyclerView listRV;
    CustomAdapter adapter;


private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //initialize our variable
        dataArrayList = new ArrayList<>();
        getData();
        /*

        //set layout manager to our adapter class
        listRV = findViewById(R.id.list);
        listRV.setHasFixedSize(true);
        listRV.setLayoutManager(newLinearLayoutManager(this));

        //initializing adapter class for recycler view
        adapter = new CustomAdapter(dataArrayList, this);
        listRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
         */

        //setting adapter class for recycler view

    }
    public void getData(){
        queryCall = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        RequestQueue requested = Volley.newRequestQueue(HomeActivity.this);

        //see if I get here at least, ok I do now to get to the next level
        Log.d(TAG, "queryData");

        //JSONArray Query
        JsonArrayRequest jsonAr = new JsonArrayRequest(Request.Method.GET, queryCall, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response){
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

                    for (int i = 0; i < length; i++) {
                        id = allData.getJSONObject(i).getString("id");
                        listId = allData.getJSONObject(i).getString("listId");
                        name = allData.getJSONObject(i).getString("name");

                        SomeData data = new SomeData(id, listId, name);
                        dataArrayList.add(data);

                    }

                    /*

                    listRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                     */

                }catch (JSONException e) {
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
}