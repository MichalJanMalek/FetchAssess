package com.example.fetchassess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.BreakIterator;
import java.util.ArrayList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends ArrayAdapter<SomeData> {
    private int rL;
    private ArrayList<SomeData> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView idIdent;
        TextView listIdent;
        TextView nameIdent;
    }

    public CustomAdapter(ArrayList<SomeData> data, Context context, int rLayout) {
        super(context, rLayout, data);
        this.dataSet = data;
        this.mContext = context;
        this.rL = rLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(rL, null);
        }

        SomeData p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.identify);
            TextView tt2 = (TextView) v.findViewById(R.id.listIdentity);
            TextView tt3 = (TextView) v.findViewById(R.id.nameIdentity);

            if (tt1 != null) {
                tt1.setText(p.getSomeId());
            }

            if (tt2 != null) {
                tt2.setText(p.getSomeListId());
            }

            if (tt3 != null) {
                tt3.setText(p.getSomeName());
            }
        }

        return v;
    }

}
/*
public class CustomAdapter extends RecyclerView{
    private ArrayList<SomeData> dataArrayList = new ArrayList<>();

    public CustomAdapter(ArrayList<SomeData> dataArrayList, Context context) {
        super(this);
        this.dataArrayList = dataArrayList;
    }

    public void filter(ArrayList<SomeData> filterList){
        // below line is to add our filtered
        // list in our course array list.
        dataArrayList = filterList;
        // below line is to notify our adapter
        // as change in recycler view data.
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        SomeData data = dataArrayList.get(position);
        holder.ident.setText(data.getSomeId());
        holder.listIdent.setText(data.getSomeListId());
        holder.nameJ.setText(data.getSomeName());

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView ident;
        private final TextView listIdent;
        private final TextView nameJ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            ident = itemView.findViewById(R.id.identify);
            listIdent = itemView.findViewById(R.id.listIdentity);
            nameJ = itemView.findViewById(R.id.nameIdentity);
        }
    }

    public int getItemCount() {return dataArrayList.size();}
}
 */