package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.getFormattedCurrency
import com.example.agristore.data.entities.getFormattedOff
import com.example.agristore.data.entities.getFormattedPrice
import com.example.agristore.data.entities.getLocationFormat
import com.example.agristore.databinding.ItemListBillItemBinding
import com.example.agristore.model.ItemCardModel

class ItemCardListAdapter(private val onItemClicked: (ItemCardModel, View) -> Unit) :
    ListAdapter<ItemCardModel, ItemCardListAdapter.ItemCardViewHolder>(
        DiffCallback
    ) {

    class ItemCardViewHolder(private var binding: ItemListBillItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCardModel: ItemCardModel, addAction: (ItemCardModel, View) -> Unit) {
            binding.itemName.text = itemCardModel.item.name
            binding.itemPrice.text = itemCardModel.item.getFormattedPrice()
            binding.itemQuantity.text = itemCardModel.quantity.getLocationFormat()
            binding.itemOff.text = itemCardModel.item.getFormattedOff()
            binding.itemTotalPrice.text =
                ((itemCardModel.item.price-itemCardModel.item.off) * itemCardModel.quantity).getFormattedCurrency()
            binding.root.setOnClickListener {
                addAction(itemCardModel, binding.root)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemCardModel>() {
            override fun areItemsTheSame(oldItem: ItemCardModel, newItem: ItemCardModel): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemCardModel,
                newItem: ItemCardModel
            ): Boolean {
                return oldItem.item.id == newItem.item.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
        return ItemCardViewHolder(
            ItemListBillItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClicked)
    }
}