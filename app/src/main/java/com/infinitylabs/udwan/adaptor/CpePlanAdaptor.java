package com.infinitylabs.udwan.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.infinitylabs.udwan.R;

import com.infinitylabs.udwan.model.dashboard.PlanData;

import java.util.List;

public class CpePlanAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PlanData> cpePlanlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cpe_subcription_price, cpe_subcription_plan;
        public TextView _cpetype;
        public ImageView current;

        public MyViewHolder(View view) {
            super(view);
            cpe_subcription_price = (TextView) view.findViewById(R.id.cpe_subcription_price);
            cpe_subcription_plan = (TextView) view.findViewById(R.id.cpe_subcription_plan);
            _cpetype = (TextView) view.findViewById(R.id._cpetype);
            current = (ImageView)view.findViewById(R.id.current);


        }
    }


    public CpePlanAdaptor(Context mContext ,List<PlanData> cpePlanlist) {
        this.mContext = mContext;
        this.cpePlanlist = cpePlanlist;
        Log.e("size",""+cpePlanlist.size());

    }

    public void setValue(List<PlanData> cpePlanlist) {
        this.cpePlanlist = cpePlanlist;
        Log.e("size",""+cpePlanlist.size());
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("viewType",""+viewType);
        if(viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cpe_plan_item, parent, false);
            return new MyViewHolder(itemView);
        }else if (viewType == 2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listplace_holder, parent, false);
            return new ShimmerHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cpe_plan_item_selected, parent, false);
            return new MyViewHolder(itemView);
        }


    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        Log.e("call","call");
        if (holder instanceof MyViewHolder) {
            PlanData plan = cpePlanlist.get(position);
            ((MyViewHolder) holder).cpe_subcription_price.setText("$" + plan.getPrice());
            ((MyViewHolder) holder).cpe_subcription_plan.setText(""+plan.getName());
            String value = "Ethernet + WIFI + LTE";
            StringBuilder sbuffer = new StringBuilder();
            if (plan.getPlan().getEth0().equalsIgnoreCase("1") || plan.getPlan().getEth1().equalsIgnoreCase("1")) {
               sbuffer.append("Ethernet");
               sbuffer.append("+");
            }
            if (plan.getPlan().getWifi().equalsIgnoreCase("1")) {
                sbuffer.append("WIFI");
                sbuffer.append("+");
            }
            if (plan.getPlan().getLte().equalsIgnoreCase("1")) {
                sbuffer.append("LTE");
            }
            value = sbuffer.toString();
            if(value.endsWith("+")) {
                value= value.substring(0, value.length() - 1);
            }
            if(plan.isFromServer()){
                ((MyViewHolder) holder).current.setVisibility(View.VISIBLE);
            }else{
                ((MyViewHolder) holder).current.setVisibility(View.GONE);
            }


            ((MyViewHolder) holder)._cpetype.setText(value);
        }else if (holder instanceof ShimmerHolder) {
            ((ShimmerHolder) holder).shimmer_view_container.startShimmer();
          }
        }
        // loading album cover using Glide library



    @Override
    public int getItemCount() {
        Log.e("getItemCount",""+cpePlanlist.size());
        return cpePlanlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (cpePlanlist.get(position).isSelected()) {
            viewType = 1;
        }else if(cpePlanlist.get(position).getViewType() == 2){
            viewType = 2;
        }
         else
         {
            viewType = 0;
        }
        return viewType;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
    class ShimmerHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmer_view_container;
        ShimmerHolder(View itemView) {
            super(itemView);
            shimmer_view_container = itemView.findViewById(R.id.shimmer_view_container);

        }
    }
}
