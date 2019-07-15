package com.wukangjie.swipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(var context: Context, var data: List<Menu>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onLongClick: OnLongClick? = null
        get() = field
        set(value) {
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.main_item, parent, false));
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = data[position]
        holder.textView.text = menu.menuName
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, menu.icon))
        holder.itemView.setOnLongClickListener {
            onLongClick?.onClick(position, holder)!!
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.text)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
    }

    interface OnLongClick {
        fun onClick(position: Int, viewHolder: ViewHolder): Boolean
    }
}