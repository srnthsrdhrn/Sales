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
        public TextView main_items,offer_items, discount_amt,per_discount,Schemename,Schemeid,freeitem;


        public MyViewHolder(View view) {
            super(view);
            main_items = (TextView) view.findViewById(R.id.main_items);
            offer_items = (TextView) view.findViewById(R.id.offer_items);
            discount_amt = (TextView) view.findViewById(R.id.discount_amt);
            per_discount = (TextView) view.findViewById(R.id.perdiscount);
            Schemename = (TextView) view.findViewById(R.id.Schemename);
            Schemeid = (TextView) view.findViewById(R.id.Schemeid);
            freeitem = (TextView) view.findViewById(R.id.freeitem);
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
        String amount = content.getDiscount_amt() + "";
        String perdiscount = content.getper_discount() + "";
        if (content.getDiscount_amt() == 0) {
            perdiscount = perdiscount + "%";
            holder.discount_amt.setText(perdiscount);
        } else if(content.getper_discount() == 0) {
            amount = "Rs." + amount;
            holder.per_discount.setText(amount);
        }

        holder.Schemename.setText(content.getScheme_name());
        // holder.Schemename.setText(content.getScheme_id());
        //  holder.Schemeid.setText(content.getScheme_id());
        holder.main_items.setText(content.getScheme_id());
        holder.offer_items.setText(content.getScheme_id());
        holder.per_discount.setText(amount);
        holder.discount_amt.setText(perdiscount);
        holder.freeitem.setText(content.getfreeitems());

    }


    @Override
    public int getItemCount() {
        return content_list.size();
    }
}