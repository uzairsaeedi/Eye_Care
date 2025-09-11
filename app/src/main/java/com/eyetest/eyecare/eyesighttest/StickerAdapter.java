package com.eyetest.eyecare.eyesighttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.VH> {

    private final List<Integer> list;
    private final OnStickerClickListener listener;

    public interface OnStickerClickListener { void onStickerClick(int resId); }

    public StickerAdapter(List<Integer> list, OnStickerClickListener l) {
        this.list = list; this.listener = l;
    }

    @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(VH holder, int position) {
        int res = list.get(position);
        holder.img.setImageResource(res);
        holder.img.setOnClickListener(v -> listener.onStickerClick(res));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        VH(View itemView) { super(itemView); img = itemView.findViewById(R.id.ivStickerImage); }
    }
}
