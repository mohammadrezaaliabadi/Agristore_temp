package com.example.agristore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.data.entities.Customer
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.databinding.FragmentCustomerDetailBinding
import com.example.agristore.fragment.adapter.BillListAdapter
import com.example.agristore.viewmodel.CustomerViewModel
import com.example.agristore.viewmodel.CustomerViewModelFactory

class CustomerDetailFragment:Fragment (){
    private val navigationArgs: CustomerDetailFragmentArgs by navArgs()
    lateinit var customer:Customer

    private val viewModel: CustomerViewModel by activityViewModels {
        CustomerViewModelFactory(
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }

    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindCustomer(customer: Customer){
        binding.headerLayout.apply {
            customerName.text = "${customer.firstName} ${customer.lastName}"
            customerNationalNumber.text = customer.nationalNumber.toString()
            customerTel.text = customer.tel

        }
    }

    private fun bindBillsDetails(billWithBillItemAndItems:List<BillWithBillItemAndItem>){
        binding.headerLayout.apply {
            val offs = billWithBillItemAndItems.sumOf { it.bill.off }
            val payments = billWithBillItemAndItems.sumOf { it.bill.payment }
            val total = billWithBillItemAndItems.sumOf { it.billItemWithItems.sumOf { it.item.price*it.billItem.quantity } }
            val tempTotal = total -offs - payments
            billOff.text = offs.toString()
            billPayment.text = payments.toString()
            billTotalPrice.text = tempTotal.toString()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BillListAdapter{
            val action = CustomerDetailFragmentDirections.actionCustomerDetailFragmentToBillItemListFragment(billId = it.id)
            this.findNavController().navigate(action)
        }
        binding.rvBill.layoutManager = LinearLayoutManager(context)
        binding.rvBill.adapter = adapter
        viewModel.getCustomersWithBillAndBillItemsById(navigationArgs.customerId).observe(viewLifecycleOwner){
            it.let {
                bindCustomer(it.customer)
                adapter.submitList(it.billWithBillItemAndItems)
                bindBillsDetails(it.billWithBillItemAndItems)
            }
        }

        binding.editCustomer.setOnClickListener {
            val action = CustomerDetailFragmentDirections.actionCustomerDetailFragmentToAddCustomerFragment(R.string.edit_item.toString(), customerId = navigationArgs.customerId)
            this.findNavController().navigate(action)
        }

    }

}