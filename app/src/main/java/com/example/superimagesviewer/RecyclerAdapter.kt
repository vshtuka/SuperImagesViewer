package com.example.superimagesviewer

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.example.superimagesviewer.RecyclerAdapter.ImageViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import java.util.ArrayList

class RecyclerAdapter : RecyclerView.Adapter<ImageViewHolder>() {
    private var drawables: List<Drawable> = ArrayList()

    class ImageViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView = itemView.findViewById(R.id.photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mosaic_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.photo.setImageDrawable(drawables[position])
    }

    fun setDrawables(drawables: List<Drawable>) {
        this.drawables = drawables
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return drawables.size
    }

}
