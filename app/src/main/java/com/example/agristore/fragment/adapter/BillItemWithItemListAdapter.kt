package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.getFormattedCurrency
import com.example.agristore.data.entities.getFormattedOff
import com.example.agristore.data.entities.getFormattedPrice
import com.example.agristore.data.entities.relations.BillItemWithItem
import com.example.agristore.databinding.ItemListBillItemBinding

class BillItemWithItemListAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<BillItemWithItem, BillItemWithItemListAdapter.BillItemWithItemViewHolder>(
        DiffCallback
    ) {

    class BillItemWithItemViewHolder(private var binding: ItemListBillItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(billItemWithItem: BillItemWithItem) {
            binding.itemName.text = billItemWithItem.item.name
            binding.itemQuantity.text = billItemWithItem.billItem.quantity.toString()
            binding.itemPrice.text = billItemWithItem.billItem.getFormattedPrice()
            binding.itemOff.text = billItemWithItem.billItem.getFormattedOff()
            binding.itemTotalPrice.text =
                ((billItemWithItem.billItem.price -billItemWithItem.billItem.off ) * billItemWithItem.billItem.quantity).getFormattedCurrency()

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BillItemWithItem>() {
            override fun areItemsTheSame(
                oldItem: BillItemWithItem,
                newItem: BillItemWithItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: BillItemWithItem,
                newItem: BillItemWithItem
            ): Boolean {
                return oldItem.billItem.id == newItem.billItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillItemWithItemViewHolder {
        return BillItemWithItemViewHolder(ItemListBillItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BillItemWithItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current.item)
        }
        holder.bind(current)
    }
}