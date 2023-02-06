package com.example.agristore.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.agristore.AgriStoreApplication
import com.example.agristore.data.entities.Item
import com.example.agristore.databinding.FragmentAddItemBinding
import com.example.agristore.viewmodel.ItemViewModel
import com.example.agristore.viewmodel.ItemViewModelFactory

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    lateinit var item: Item

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: ItemViewModel by activityViewModels {
        ItemViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao()
        )
    }

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemQuantity.text.toString(),
                binding.itemPrice.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToHomeFragment()
            findNavController().navigate(action)
            viewModel.changed()
        }
    }

    /**
     * Binds views with the passed in [item] information.
     */

    private fun bind(item: Item) {
        binding.apply {
            itemName.setText(item.name, TextView.BufferType.SPANNABLE)
            itemPrice.setText(item.price.toString(), TextView.BufferType.SPANNABLE)
            itemQuantity.setText(item.quantity.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemQuantity.text.toString(),
                this.binding.itemPrice.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToHomeFragment()
            findNavController().navigate(action)
            viewModel.changed()
        }
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemQuantity.text.toString(),
        )
    }

    /*
    * Called before fragment is destroyed.
    */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}


