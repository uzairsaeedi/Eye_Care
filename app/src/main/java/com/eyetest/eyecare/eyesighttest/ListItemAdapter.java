package com.eyetest.eyecare.eyesighttest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private Context context;
    private List<ListItem> list;

    public ListItemAdapter( Context context, List<ListItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIconRes());

        holder.itemView.setOnClickListener(v -> {
            String exerciseType = mapTitleToKey(item.getTitle());

            Intent intent = new Intent(context, SnellenChartActivity.class);
            intent.putExtra("exerciseType", exerciseType);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("iconRes", item.getIconRes());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String mapTitleToKey(String title) {
        if (title == null) return "default";
        return title.trim().toLowerCase().replaceAll("[^a-z0-9]+", "_");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvListTitle);
            subtitle = itemView.findViewById(R.id.tvListSubtitle);
            icon = itemView.findViewById(R.id.imgListIcon);
        }
    }
}

