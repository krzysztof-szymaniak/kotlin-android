package com.example.galleryapp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast


class MyItemRecyclerViewAdapter(
    private val context: Context,
    private val activityParent: MainActivity
)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private lateinit var values: MutableList<PhotoItem>

    fun setData(data: MutableList<PhotoItem>){
        values = data
    }
    fun setItem(item: PhotoItem, pos: Int) {
        values[pos] = item
        values.sortByDescending { it.rates }
        notifyDataSetChanged()
    }
    fun getData() = values as ArrayList<PhotoItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val resId = context.resources.getIdentifier(item.src, "drawable", context.packageName)
        holder.photo.setImageResource(resId)
        holder.description.text = item.description
        holder.ratingBar.rating = item.rates
        holder.itemView.setOnClickListener{
            activityParent.startEditActivity(item, position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.description)
        val photo : ImageView = view.findViewById(R.id.photo)
        val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)
    }
}