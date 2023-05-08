package ie.wit.applicationtracker.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.ItemLayoutBinding

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemText: TextView = itemView.findViewById(R.id.item_text)


}