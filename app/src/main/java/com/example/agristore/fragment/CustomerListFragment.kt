package com.example.agristore.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.PopupMenu
import android.widget.SearchView
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.data.entities.Customer
import com.example.agristore.databinding.FragmentCustomerListBinding
import com.example.agristore.fragment.adapter.CustomerListAdapter
import com.example.agristore.viewmodel.CustomerViewModel
import com.example.agristore.viewmodel.CustomerViewModelFactory


class CustomerListFragment : Fragment() {
    private val viewModel: CustomerViewModel by activityViewModels {
        CustomerViewModelFactory((activity?.application as AgriStoreApplication).database.customerDao())
    }

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!
    lateinit var menuHost: MenuHost


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost = activity as MenuHost


        val adapter = CustomerListAdapter { customer, view ->
            showPopUpCustomerItemMenu(view, R.menu.customer_item_menu, customer = customer)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.searchResult.observe(this.viewLifecycleOwner) { customers ->
            customers.let {
                adapter.submitList(it)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddCustomerFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

    }

    @SuppressLint("RestrictedApi")
    private fun showPopUpCustomerItemMenu(v: View, @MenuRes menuRes: Int, customer: Customer) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, getString(R.string.margin_popup_menu_icon).toFloat(), resources.displayMetrics)
                        .toInt()
                if (item?.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx,0)
                    } else {
                        item.icon =
                            object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.action_customer_buy -> {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToAddBillHomeFragment(customer.id)
                    this.findNavController().navigate(action)
                }
                R.id.action_customer_detail -> {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToCustomerDetailFragment(customer.id)
                    this.findNavController().navigate(action)
                }
            }

            return@setOnMenuItemClickListener false

        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }


        // Show the popup menu.
        popup.show()
    }


    override fun onResume() {
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onResume()

        viewModel.changeResults.observe(viewLifecycleOwner) {
            if (it.equals("changed")) {
                viewModel.customerSearch("")
                viewModel.setDefaultChange()
            }
        }
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
                    viewModel.customerSearch(query)



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