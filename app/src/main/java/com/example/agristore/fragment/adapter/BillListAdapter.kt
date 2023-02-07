package com.example.agristore.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.databinding.ItemListBillBinding
import com.example.agristore.utillity.PersianDateConvertor
import java.text.SimpleDateFormat

class BillListAdapter(private val actionBill: (Bill) -> Unit) :
    ListAdapter<BillWithBillItemAndItem, BillListAdapter.BillViewHolder>(
        DiffCallback
    ) {
    class BillViewHolder(private var binding: ItemListBillBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(billWithBillItemAndItem: BillWithBillItemAndItem, actionBill: (Bill) -> Unit) {
            binding.billCode.text = billWithBillItemAndItem.bill.id.toString()
            val date = billWithBillItemAndItem.bill.date
            binding.billDate.text = PersianDateConvertor().convertDateToPersianDate(date)
                .toString() + " " + SimpleDateFormat("HH:mm:ss").format(date)
            binding.billOff.text = billWithBillItemAndItem.bill.off.toString()
            binding.billPayment.text = billWithBillItemAndItem.bill.payment.toString()
            val total = billWithBillItemAndItem.billItemWithItems.map { it.billItem.quantity * it.item.price }.sum()
            binding.billTotalPrice.text = (total - billWithBillItemAndItem.bill.off - billWithBillItemAndItem.bill.payment).toString()
            binding.itemDetailsAction.setOnClickListener {
                actionBill(billWithBillItemAndItem.bill)
            }
        }
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BillWithBillItemAndItem>() {
            override fun areItemsTheSame(
                oldItem: BillWithBillItemAndItem,
                newItem: BillWithBillItemAndItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: BillWithBillItemAndItem,
                newItem: BillWithBillItemAndItem
            ): Boolean {
                return oldItem.bill == newItem.bill
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        return BillViewHolder(ItemListBillBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, actionBill)
    }


}