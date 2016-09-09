package com.sakthisugars.salesandmarketing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by singapore on 28-06-2016.
 */
public class Scheme_data_adapter extends RecyclerView.Adapter<Scheme_data_adapter.MyViewHolder> {
    private List<Scheme_data> content_list;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_items,offer_items, discount;


        public MyViewHolder(View view) {
            super(view);
            main_items = (TextView) view.findViewById(R.id.main_items);
            offer_items = (TextView) view.findViewById(R.id.offer_items);
            discount = (TextView) view.findViewById(R.id.discount);
        }
    }
    public Scheme_data_adapter(List<Scheme_data> Scheme_data_list){
        this.content_list=Scheme_data_list;
    }
    @Override
    public Scheme_data_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheme_selection_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Scheme_data content = content_list.get(position);
        holder.main_items.setText(content.getMain_items());
        holder.offer_items.setText(content.getOffer_items());
        String text = content.getDiscount_value()+"";
        if(content.getDiscount_value_type()==1){
            text=text+"%";
        }
        else {
            text = "Rs." + text;
        }
        holder.discount.setText(text);
    }

    @Override
    public int getItemCount() {
        return content_list.size();
    }
}
