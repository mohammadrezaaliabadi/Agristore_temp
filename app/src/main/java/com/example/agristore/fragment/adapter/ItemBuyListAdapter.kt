package com.example.agristore.fragment.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.getFormattedPrice
import com.example.agristore.databinding.ItemListItemBuyBinding

class ItemBuyListAdapter(private val onItemClicked: (Item, Long) -> Unit) :
    ListAdapter<Item, ItemBuyListAdapter.ItemBuyViewHolder>(
        DiffCallback
    ) {

    class ItemBuyViewHolder(private var binding: ItemListItemBuyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, addAction: (Item, Long) -> Unit) {
            binding.itemName.text = item.name
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemQuantity.text = item.quantity.toString()
            binding.itemCount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    if (!s.isNullOrEmpty() && s.toString().toInt() > item.quantity) {
                        s.clear()
                        s.insert(0,item.quantity.toString())
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    binding.itemAddAction.isEnabled = s.toString().isNotEmpty()

                }
            })
            binding.itemAddAction.setOnClickListener {
                addAction(item, binding.itemCount.text.toString().toLong())
                binding.itemCount.text?.clear()
            }
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBuyViewHolder {
        return ItemBuyViewHolder(
            ItemListItemBuyBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemBuyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClicked)
    }
}