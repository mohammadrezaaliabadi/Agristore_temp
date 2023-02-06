package com.example.agristore.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.agristore.AgriStoreApplication
import com.example.agristore.data.entities.Customer
import com.example.agristore.databinding.FragmentAddCustomerBinding
import com.example.agristore.viewmodel.CustomerViewModel
import com.example.agristore.viewmodel.CustomerViewModelFactory


class AddCustomerFragment : Fragment() {
    private val navigationArgs: CustomerDetailFragmentArgs by navArgs()
    lateinit var customer: Customer

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: CustomerViewModel by activityViewModels {
        CustomerViewModelFactory(
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.customerId
        if (id > 0) {
            viewModel.retrieveCustomer(id).observe(this.viewLifecycleOwner) { selectedCustomer ->
                customer = selectedCustomer
                bind(customer)
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
            viewModel.addNewCustomer(
                binding.customerFirstName.text.toString(),
                binding.customerLastName.text.toString(),
                binding.customerNationalNumber.text.toString(),
                binding.customerTel.text.toString()
            )
            val action = AddCustomerFragmentDirections.actionAddCustomerFragmentToHomeFragment()
            findNavController().navigate(action)
            viewModel.changed()
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.customerFirstName.text.toString(),
            binding.customerLastName.text.toString(),
            binding.customerNationalNumber.text.toString(),
            binding.customerTel.text.toString()
            )

    }

    private fun bind(customer: Customer){
        binding.apply {
            customerFirstName.setText(customer.firstName, TextView.BufferType.SPANNABLE)
            customerLastName.setText(customer.lastName, TextView.BufferType.SPANNABLE)
            customerNationalNumber.setText(customer.nationalNumber.toString(), TextView.BufferType.SPANNABLE)
            customerTel.setText(customer.tel, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }

        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateCustomer(
                this.navigationArgs.customerId,
                binding.customerFirstName.text.toString(),
                binding.customerLastName.text.toString(),
                binding.customerNationalNumber.text.toString(),
                binding.customerTel.text.toString()
            )
            val action = AddCustomerFragmentDirections.actionAddCustomerFragmentToHomeFragment()
            findNavController().navigate(action)
            viewModel.changed()
        }
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