package com.example.fetchassess;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("hiring.json")
    Call<List<SomeData>> getItems();
}
