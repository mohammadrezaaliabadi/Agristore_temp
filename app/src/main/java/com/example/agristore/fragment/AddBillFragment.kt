package com.example.agristore.fragment

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.Customer
import com.example.agristore.databinding.FragmentAddBillBinding
import com.example.agristore.fragment.adapter.ItemBuyListAdapter
import com.example.agristore.viewmodel.BillViewModel
import com.example.agristore.viewmodel.BillViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddBillFragment(private val customerId: Int) : Fragment() {


    //private val navigationArgs: AddBillFragmentArgs by navArgs()
    lateinit var bill: Bill
    lateinit var customer: Customer

    private val viewModel: BillViewModel by activityViewModels {
        BillViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao(),
            (activity?.application as AgriStoreApplication).database.billItemDao(),
            (activity?.application as AgriStoreApplication).database.billDao(),
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }

    private var _binding: FragmentAddBillBinding? = null
    private val binding get() = _binding!!

    lateinit var menuHost: MenuHost

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBillBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun bindingCustomer(customer: Customer) {
        binding.apply {
            customerName.text = customer.firstName + " " + customer.lastName
            customerNationalNumber.text = customer.nationalNumber.toString()
        }
    }

    private fun bindingItems() {
        val adapter = ItemBuyListAdapter { item, count ->
            viewModel.addItemToCard(item, count)
            viewModel.addItemToBuy(item, count)
        }
        binding.rvItemsBuy.layoutManager = LinearLayoutManager(this.context)
        binding.rvItemsBuy.adapter = adapter
        viewModel.searchItemResults.observe(this.viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
        viewModel.itemSearch("")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuHost = activity as MenuHost
        if (viewModel.customerId != customerId) {
            viewModel.customerId = customerId
            viewModel.resetCardItems()
        }
        //val customerId = navigationArgs.customerId
        viewModel.retrieveCustomer(customerId)
            .observe(this.viewLifecycleOwner) { selectedCustomer ->
                customer = selectedCustomer
                bindingCustomer(customer)
            }
        bindingItems()


    }

    override fun onResume() {
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onResume()


    }

    override fun onPause() {
        menuHost.removeMenuProvider(menuProvider)
        super.onPause()
    }

    val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val menuItem = menu.findItem(R.id.action_bar_search)
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = "Search ..."
            searchView.isSubmitButtonEnabled = true
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // If the list contains the search query than filter the adapter
                    // using the filter method with the query as its argument
                    viewModel.itemSearch(query)


                    return true
                }

                // This method is overridden to filter the adapter according
                // to a search query when the user is typing search
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

            })

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_bar_search -> {
                    return true
                }
                else -> false
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}