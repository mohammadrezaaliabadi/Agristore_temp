package com.example.agristore.fragment

import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
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
import com.example.agristore.databinding.FragmentItemListBinding
import com.example.agristore.fragment.adapter.ItemListAdapter
import com.example.agristore.viewmodel.ItemViewModel
import com.example.agristore.viewmodel.ItemViewModelFactory


class ItemListFragment : Fragment() {
    private val viewModel: ItemViewModel by activityViewModels {
        ItemViewModelFactory((activity?.application as AgriStoreApplication).database.itemDao())
    }
    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    lateinit var menuHost: MenuHost
    lateinit var adapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        menuHost = activity as MenuHost
        adapter = ItemListAdapter {
            val action =
                HomeFragmentDirections.actionHomeFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.searchResult.observe(this.viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
        binding.floatingActionButton.setOnClickListener {
//            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
//                getString(R.string.add_fragment_title)
//            )
            val action = HomeFragmentDirections.actionHomeFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

    }




    override fun onResume() {
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onResume()
        viewModel.changeResults.observe(viewLifecycleOwner) {
            if (it.equals("changed")) {
                viewModel.itemSearch("")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}