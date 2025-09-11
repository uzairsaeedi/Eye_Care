package com.eyetest.eyecare.eyesighttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    private final List<String> categories;
    private final OnCategoryClick listener;
    private int selected = 0;

    public interface OnCategoryClick { void onCategoryClick(int index, String category); }

    public CategoryAdapter(List<String> cats, OnCategoryClick l) {
        categories = cats; listener = l;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.txt.setText(categories.get(position));
        holder.itemView.setSelected(position == selected);
        holder.itemView.setOnClickListener(v -> {
            int old = selected;
            selected = position;
            notifyItemChanged(old);
            notifyItemChanged(selected);
            listener.onCategoryClick(position, categories.get(position));
        });
    }

    @Override public int getItemCount() { return categories.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txt;
        VH(View v) { super(v); txt = v.findViewById(android.R.id.text1); txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); }
    }
}
