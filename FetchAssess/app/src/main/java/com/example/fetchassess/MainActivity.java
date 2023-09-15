package com.example.fetchassess;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.squareup.moshi.Moshi;



import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    //variables
    private static final String TAG = "MainActivity:";
    private String queryCall;
    static ArrayList<SomeData> originArrayList = new ArrayList<>();
    private ArrayList<SomeData> dataArrayList = new ArrayList<>();
    private ArrayList<SomeData> newArray = new ArrayList<>();
    private ArrayList<SomeData> filteredArray = new ArrayList<>();
    private ListView listRV;
    private CustomAdapter adapter;
    public ToggleButton tog;
    private SearchView searchView;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle the splash screen.
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        // Define cache directory and size
        File cacheDirectory = new File(getCacheDir(), "http_cache");
        long cacheSize = 10 * 1024 * 1024; // 10 MB

        // Initialize OkHttp with cache
        Cache cache = new Cache(cacheDirectory, cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();

        // Initialize Moshi and Retrofit with OkHttp client
        Moshi moshi = new Moshi.Builder().build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fetch-hiring.s3.amazonaws.com/").addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).build();

        ApiService apiService = retrofit.create(ApiService.class);

        //query data
        getData(apiService);

        //initialize adapter
        listRV = findViewById(R.id.itemListView);
        adapter = new CustomAdapter(dataArrayList, MainActivity.this, R.layout.list);
        listRV.setAdapter(adapter);
        listRV.setTextFilterEnabled(true);

        //toggles the sort to reverse the list and un-reverse
        tog = findViewById(R.id.toggle);
        tog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //reverse list went clicking either option
                if (b) {
                    reverse();
                }else {
                    reverse();
                }
            }
        });

        //find our search view and watch our search bar for text change
        searchView = findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            //query text change listener
            @Override
            public boolean onQueryTextChange(String s) {
                // Apply filter for non-empty search text
                adapter.getFilter().filter(s);
                return false;
            }
        });


    }
    private void getData(ApiService apiService) {
        queryCall = "hiring.json";

        Call<List<SomeData>> call = apiService.getItems();

        call.enqueue(new Callback<List<SomeData>>() {
            public void onResponse(Call<List<SomeData>> call, retrofit2.Response<List<SomeData>> response){
                if (response.isSuccessful()) {
                    List<SomeData> items = response.body();
                    if (items != null) {
                        // Filter out items with blank names
                        List<SomeData> filteredItems = new ArrayList<>();
                        for (SomeData item : items) {
                            if (item.getSomeName() != null && !item.getSomeName().isEmpty()) {
                                filteredItems.add(item);
                            }
                        }

                        // update dataArrayList with filtered items
                        dataArrayList.clear();
                        dataArrayList.addAll(filteredItems);

                        // group up the list
                        groupUp(dataArrayList);
                        originArrayList.clear();
                        originArrayList.addAll(dataArrayList);

                        //update the adapter
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Handle the error
                    Log.e(TAG, "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<SomeData>> call, Throwable t) {
                // Handle the network request failure
                Log.e(TAG, "Network request failed", t);
            }
        });
    }
    public void groupUp(ArrayList<SomeData> list){

        int l = list.size();
        max = 0;

        //find max value of listID
        for (int i = 0; i < l; i++) {
            String lNum = list.get(i).getSomeListId();
            if (max < Integer.valueOf(lNum)) {
                max = Integer.valueOf(lNum);
            }
        }

        //check if we getting max value from listID
        Log.d("Max Value", String.valueOf(max));

        //Compares listID in the list to then organize
        Collections.sort(list, new Comparator<SomeData>() {
            public int compare(SomeData lt, SomeData rt) {
                return lt.someListId.compareTo(rt.someListId);
            }
        });

        //split up array and added to filtered array which will group everything starting with listID 1, then listID 2, etc.....
        for (int i = 1; i <= max; i++) {
            for (int j = 0; j < l; j++) {
                if (i == Integer.valueOf(list.get(j).getSomeListId())) {
                    if(list.get(j).getSomeName().equals("")){
                        Log.e(TAG, "blank");
                    }else {
                        //values we need to get from the list
                        String id = list.get(j).getSomeId();
                        String listId = list.get(j).getSomeListId();
                        String name = list.get(j).getSomeName();

                        //String all = id + " " + s + " " + t;
                        //Log.d(TAG, all);

                        newArray.add(new SomeData(id, listId, name));
                    }
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
    public void reverse(){
        //reverses the list
        Collections.reverse(dataArrayList);
        Collections.reverse(originArrayList);

        //update adapter
        adapter.notifyDataSetChanged();
    }
}