package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.getFormattedCurrency
import com.example.agristore.data.entities.relations.BillAndCustomerWithBillItemAndItem
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.databinding.ItemListBillBinding
import com.example.agristore.databinding.ItemListBillCustomerBinding
import com.example.agristore.databinding.ItemListCustomerBinding
import com.example.agristore.utillity.PersianDateConvertor
import java.text.SimpleDateFormat

class BillAndCustomerWithBillItemAndItemListAdapter(private val actionBill: (Bill) -> Unit) :
    ListAdapter<BillAndCustomerWithBillItemAndItem, BillAndCustomerWithBillItemAndItemListAdapter.BillAndCustomerWithBillItemAndItemViewHolder>(
        DiffCallback
    ) {
    class BillAndCustomerWithBillItemAndItemViewHolder(private var binding: ItemListBillCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(b: BillAndCustomerWithBillItemAndItem, actionBill: (Bill) -> Unit) {
            binding.billCode.text = b.billAndCustomer.bill.id.toString()
            val date = b.billAndCustomer.bill.date
            binding.billDate.text = PersianDateConvertor().convertDateToPersianDate(date)
                .toString() + " " + SimpleDateFormat("HH:mm:ss").format(date)
            binding.billOff.text = b.billAndCustomer.bill.off.getFormattedCurrency()
            binding.billPayment.text = b.billAndCustomer.bill.payment.getFormattedCurrency()
            val total =
                b.billItemWithItems.map { it.billItem.quantity * (it.billItem.price - it.billItem.off) }
                    .sum()
            binding.billTotalPrice.text =
                (total - b.billAndCustomer.bill.off - b.billAndCustomer.bill.payment).getFormattedCurrency()
            binding.itemDetailsAction.setOnClickListener {
                actionBill(b.billAndCustomer.bill)
            }
        }
    }

    companion object {
        private val DiffCallback =
            object : DiffUtil.ItemCallback<BillAndCustomerWithBillItemAndItem>() {
                override fun areItemsTheSame(
                    oldItem: BillAndCustomerWithBillItemAndItem,
                    newItem: BillAndCustomerWithBillItemAndItem
                ): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(
                    oldItem: BillAndCustomerWithBillItemAndItem,
                    newItem: BillAndCustomerWithBillItemAndItem
                ): Boolean {
                    return oldItem.billAndCustomer.bill.id == newItem.billAndCustomer.bill.id
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillAndCustomerWithBillItemAndItemViewHolder {
        return BillAndCustomerWithBillItemAndItemViewHolder(
            ItemListBillCustomerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BillAndCustomerWithBillItemAndItemViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.bind(current, actionBill)
    }
}