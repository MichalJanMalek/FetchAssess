package com.example.fetchassess;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.BreakIterator;
import java.util.ArrayList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter{
    private ArrayList<SomeData> dataArrayList = new ArrayList<>();

    public CustomAdapter(ArrayList<SomeData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    /*
    @NonNull
    public CustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
     */

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public void onBindViewHolder(@NonNull CustomAdapter holder, int position) {

    }

    public int getItemCount() {return dataArrayList.size();}
}
