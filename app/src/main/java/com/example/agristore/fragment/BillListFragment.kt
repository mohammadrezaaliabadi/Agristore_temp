package com.example.agristore.fragment

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.databinding.FragmentBillListBinding
import com.example.agristore.databinding.FragmentItemListBinding
import com.example.agristore.fragment.adapter.BillAndCustomerWithBillItemAndItemListAdapter
import com.example.agristore.fragment.adapter.BillListAdapter
import com.example.agristore.viewmodel.BillViewModel
import com.example.agristore.viewmodel.BillViewModelFactory

class BillListFragment:Fragment() {
    private val viewModel: BillViewModel by activityViewModels {
        BillViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao(),
            (activity?.application as AgriStoreApplication).database.billItemDao(),
            (activity?.application as AgriStoreApplication).database.billDao(),
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }

    private var _binding: FragmentBillListBinding? = null
    private val binding get() = _binding!!
    lateinit var menuHost : MenuHost


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBillListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuHost = activity as MenuHost


        val adapter = BillAndCustomerWithBillItemAndItemListAdapter{
            val action = HomeFragmentDirections.actionHomeFragmentToBillItemListFragment(billId = it.id)
            this.findNavController().navigate(action)
        }
        binding.rvBill.layoutManager = LinearLayoutManager(this.context)
        binding.rvBill.adapter = adapter
        viewModel.searchResult.observe(viewLifecycleOwner){
            it.let {
                adapter.submitList(it)
            }
        }
    }
    override fun onResume() {
        menuHost.addMenuProvider(menuProvider,viewLifecycleOwner)
        super.onResume()


        viewModel.changeResults.observe(viewLifecycleOwner){
            if (it.equals("changed")){
                viewModel.billSearch("")
                viewModel.setDefaultChange()
            }
        }
    }

    override fun onPause() {
        menuHost.removeMenuProvider(menuProvider)
        super.onPause()
    }

    val menuProvider  = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
            val menuItem = menu.findItem(R.id.action_bar_search)
            val searchView = menuItem.actionView as SearchView
            searchView.queryHint = "Search ..."
            searchView.isSubmitButtonEnabled = true
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean {
                    // If the list contains the search query than filter the adapter
                    // using the filter method with the query as its argument
                    viewModel.billSearch(query)


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
}