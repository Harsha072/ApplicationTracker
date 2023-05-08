package ie.wit.applicationtracker.ui.checklist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.models.ItemList
import ie.wit.applicationtracker.ui.checklist.ChecklistFragment
import ie.wit.applicationtracker.utils.ItemViewHolder

class ItemAdapter(itemList: MutableList<String>?, checklistFragment: ChecklistFragment) : RecyclerView.Adapter<ItemViewHolder>() {
    private val itemList = ItemList(mutableListOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemText.text = itemList.itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.itemList.size
    }

    fun getItems(): ItemList {
        return itemList
    }

    fun addItem(item: String) {
        itemList.itemList.add(item)
        notifyItemInserted(itemList.itemList.size - 1)
    }
}