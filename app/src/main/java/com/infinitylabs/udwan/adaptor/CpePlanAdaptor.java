package com.infinitylabs.udwan.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinitylabs.udwan.R;

import com.infinitylabs.udwan.model.dashboard.PlanData;

import java.util.List;

public class CpePlanAdaptor extends RecyclerView.Adapter<CpePlanAdaptor.MyViewHolder> {

    private Context mContext;
    private List<PlanData> cpePlanlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cpe_subcription_price, cpe_subcription_plan;
        public TextView _cpetype;

        public MyViewHolder(View view) {
            super(view);
            cpe_subcription_price = (TextView) view.findViewById(R.id.cpe_subcription_price);
            cpe_subcription_plan = (TextView) view.findViewById(R.id.cpe_subcription_plan);
            _cpetype = (TextView) view.findViewById(R.id._cpetype);

        }
    }


    public CpePlanAdaptor(Context mContext) {
        this.mContext = mContext;

    }

    public void setValue(List<PlanData> cpePlanlist) {
        this.cpePlanlist = cpePlanlist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cpe_plan_item, parent, false);
            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cpe_plan_item_selected, parent, false);
            return new MyViewHolder(itemView);
        }


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PlanData plan = cpePlanlist.get(position);
        holder.cpe_subcription_price.setText("$ " + plan.getPrice());
        holder.cpe_subcription_plan.setText(plan.getDuration() + " Month" + " + " + plan.getPlan().getSpeed());
        String value = "Ethernet + WIFI + LTE";
        if (plan.getPlan().getEth0().equalsIgnoreCase("0")) {
            value.replace("Ethernet +", "");
        }
        if (plan.getPlan().getWifi().equalsIgnoreCase("0")) {
            value.replace("+ WIFI", "");
        }
        if (plan.getPlan().getLte().equalsIgnoreCase("0")) {
            value.replace("+ LTE", "");
        }

        holder._cpetype.setText(value);

        // loading album cover using Glide library

    }

    @Override
    public int getItemCount() {
        return cpePlanlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (cpePlanlist.get(position).isSelected()) {
            viewType = 1;
        } else {
            viewType = 0;
        }
        return viewType;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}
