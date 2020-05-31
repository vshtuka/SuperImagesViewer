package com.example.androidproject;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    private Drawable[] drawables;

    RecyclerAdapter(Drawable[] drawables) {
        this.drawables = drawables;
    }

    protected static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;

        ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.photo.setImageDrawable(drawables[position]);
    }

    @Override
    public int getItemCount() {
        return drawables.length;
    }

}