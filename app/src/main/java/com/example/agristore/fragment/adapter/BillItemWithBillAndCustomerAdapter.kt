package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.getLocationFormat
import com.example.agristore.data.entities.relations.BillItemWithBillAndCustomer
import com.example.agristore.databinding.ItemListItemCustomerBinding

class BillItemWithBillAndCustomerAdapter(private val onItemClicked: (BillItemWithBillAndCustomer, View) -> Unit) :
    ListAdapter<BillItemWithBillAndCustomer, BillItemWithBillAndCustomerAdapter.BillItemWithBillAndCustomerViewHolder>(
        DiffCallback
    ) {
    companion object {
        private val DiffCallback =
            object : DiffUtil.ItemCallback<BillItemWithBillAndCustomer>() {
                override fun areItemsTheSame(
                    oldItem: BillItemWithBillAndCustomer,
                    newItem: BillItemWithBillAndCustomer
                ): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(
                    oldItem: BillItemWithBillAndCustomer,
                    newItem: BillItemWithBillAndCustomer
                ): Boolean {
                    return oldItem.billItem.id == newItem.billItem.id
                }
            }
    }

    class BillItemWithBillAndCustomerViewHolder(private val binding: ItemListItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            i: BillItemWithBillAndCustomer,
            addAction: (BillItemWithBillAndCustomer, View) -> Unit
        ) {
            val customer = i.billAndCustomer.customer
            val bill = i.billAndCustomer.bill
            binding.customerName.text = "${customer.firstName} ${customer.lastName}"
            binding.billCode.text = bill.id.getLocationFormat()
            binding.billItemQuantity.text =
                i.billItem.quantity.getLocationFormat()
            binding.billItemCustomerMenu.setOnClickListener {
                addAction(i, it)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillItemWithBillAndCustomerViewHolder {
        return BillItemWithBillAndCustomerViewHolder(
            ItemListItemCustomerBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BillItemWithBillAndCustomerViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.bind(current, onItemClicked)
    }


}