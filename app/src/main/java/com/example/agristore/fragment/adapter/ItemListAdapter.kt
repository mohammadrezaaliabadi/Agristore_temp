package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.getFormattedOff
import com.example.agristore.data.entities.getFormattedPrice
import com.example.agristore.data.entities.getLocationFormat
import com.example.agristore.databinding.CardItemBinding

class ItemListAdapter(private val onItemClicked: (Item) -> Unit):ListAdapter<Item, ItemListAdapter.ItemViewHolder>(
    DiffCallback
) {
    class ItemViewHolder(private var binding: CardItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Item,onItemClicked: (Item) -> Unit) {
            binding.itemName.text = item.name
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemQuantity.text = item.getLocationFormat()
            binding.itemOff.text = item.getFormattedOff()
            binding.itemDescription.text = item.description
            binding.buttonItemAction.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current,onItemClicked)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}