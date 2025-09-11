package com.eyetest.eyecare.eyesighttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.VH> {
    private List<PerformanceItem> list;
    public PerformanceAdapter(List<PerformanceItem> list){this.list=list;}
    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_performance,parent,false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(@NonNull VH holder,int position){
        PerformanceItem it=list.get(position);
        holder.tvTitle.setText(it.getTitle());
        holder.tvSubtitle.setText(it.getSubtitle());
        holder.tvPercent.setText(it.getPercent() + "%");
    }
    @Override public int getItemCount(){return list.size();}
    static class VH extends RecyclerView.ViewHolder{
        TextView tvTitle,tvSubtitle,tvPercent;
        VH(@NonNull View v){
            super(v);
            tvTitle=v.findViewById(R.id.tvTitle);
            tvSubtitle=v.findViewById(R.id.tvSubtitle);
            tvPercent=v.findViewById(R.id.tvPercent);
        }
    }
}
