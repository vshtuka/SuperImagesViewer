package com.example.superimagesviewer;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    private List<Drawable> drawables = new ArrayList<>();

    protected static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;

        ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mosaic_item, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.photo.setImageDrawable(drawables.get(position));
    }

    public void setDrawables(List<Drawable> drawables) {
        this.drawables = drawables;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return drawables.size();
    }

}
