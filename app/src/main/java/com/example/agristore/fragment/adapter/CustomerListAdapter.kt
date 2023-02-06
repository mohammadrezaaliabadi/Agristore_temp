package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Customer
import com.example.agristore.databinding.ItemListCustomerBinding

class CustomerListAdapter(private val onCustomerClicked: (Customer, View) -> Unit) :
    ListAdapter<Customer, CustomerListAdapter.CustomerViewHolder>(DiffCallback) {
    class CustomerViewHolder(private var binding: ItemListCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer, onCustomerClicked: (Customer, View) -> Unit) {
            binding.customerName.text = customer.firstName + " " + customer.lastName
            binding.customerNationalNumber.text = customer.nationalNumber.toString()
            binding.customerTel.text = customer.tel
            binding.customerListPopupMenu.setOnClickListener {
                onCustomerClicked(customer,it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder(ItemListCustomerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        var current = getItem(position)

        holder.bind(current,onCustomerClicked)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Customer>() {
            override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}
