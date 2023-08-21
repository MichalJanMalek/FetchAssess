package com.example.fetchassess;

import android.content.Context;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<SomeData> implements Filterable {
    private ArrayList<SomeData> filter_list;
    private int rL;
    private ArrayList<SomeData> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView idIdent;
        TextView listIdent;
        TextView nameIdent;
    }

    //custom adapter constructor
    public CustomAdapter(ArrayList<SomeData> data, Context context, int rLayout) {
        super(context, rLayout, data);
        this.dataSet = data;
        this.mContext = context;
        this.rL = rLayout;
        this.filter_list = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //find view
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(rL, null);
        }

        //get position of array from SomeData
        SomeData p = getItem(position);


        if (p != null) {
            //TextView IDs
            TextView tv1 = (TextView) v.findViewById(R.id.identify);
            TextView tv2 = (TextView) v.findViewById(R.id.listIdentity);
            TextView tv3 = (TextView) v.findViewById(R.id.nameIdentity);

            //set 1st table column item to display ID
            if (tv1 != null) {
                tv1.setText(p.getSomeId());
            }

            //set 2nd table column item to display listID
            if (tv2 != null) {
                tv2.setText(p.getSomeListId());
            }

            //set 3rd table column item to display name
            if (tv3 != null) {
                tv3.setText(p.getSomeName());
            }
        }

        //return our view
        return v;
    }

    //filter our array when searching
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                //if charSequence is 0 then set filterResults to the original array
                if (charSequence.length() == 0) {
                    filterResults.count = MainActivity.originArrayList.size();
                    filterResults.values = MainActivity.originArrayList;
                } else {
                    String search = charSequence.toString().toLowerCase();
                    ArrayList<SomeData> result = new ArrayList<SomeData>();

                    //need this to reset the filter list to then filter through
                    filter_list=MainActivity.originArrayList;

                    //for size of the filter_list, get SomeName
                    for (int i = 0; i < filter_list.size(); i++) {

                        //values we want to filter through
                        String name = filter_list.get(i).getSomeName();
                        String name2 = filter_list.get(i).getSomeId();

                        //if Name or ID contains the query string, then add it to filtered list
                        if ((name.toLowerCase().contains(search)) || (name2.toLowerCase().contains(search))){
                            result.add(filter_list.get(i));
                            Log.d("Filtered", name);
                        }
                    }

                    //the filtered results to then output
                    filterResults.count = result.size();
                    filterResults.values = result;

                    //just checking if filterResult is working with a tag
                    int dude = filterResults.count;
                    Log.d("here", String.valueOf(dude));
                }

                //return our filtered results
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //doesn't work
                //filter_list = (ArrayList<SomeData>) filterResults.values; // Update filter_list

                // Clear the current dataset
                dataSet.clear();

                //set list View to show filtered results
                dataSet.addAll((ArrayList<SomeData>) filterResults.values);

                //update list view
                notifyDataSetChanged();
            }
        };
    }

    // Return size of filter_list
    @Override
    public int getCount() {return dataSet.size(); }
}