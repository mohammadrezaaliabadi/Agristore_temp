package com.example.agristore.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.databinding.FragmentAddBillCardBinding
import com.example.agristore.fragment.adapter.ItemCardListAdapter
import com.example.agristore.model.ItemCardModel
import com.example.agristore.utillity.PersianDateConvertor
import com.example.agristore.viewmodel.BillViewModel
import com.example.agristore.viewmodel.BillViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class AddBillCardFragment(private val customerId: Int) : Fragment() {
    private val viewModel: BillViewModel by activityViewModels {
        BillViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao(),
            (activity?.application as AgriStoreApplication).database.billItemDao(),
            (activity?.application as AgriStoreApplication).database.billDao(),
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }
    private val adapter: ItemCardListAdapter = ItemCardListAdapter { itemCardModel, view ->
        showMenu(view, R.menu.card_item_menu, itemCardModel)

    }

    private var _binding: FragmentAddBillCardBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBillCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingItems()



        binding.floatingActionButton.setOnClickListener {
            if (viewModel.itemsBuy.isNotEmpty()) {
                var billPayment = binding.billPayment.text.toString()
                if (billPayment.isNullOrBlank()) {
                    billPayment = "0"
                }
                var billOff = binding.billOff.text.toString()
                if (billOff.isNullOrBlank()) {
                    billOff = "0"
                }

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.bill_dialog_title_add)
                    .setMessage(R.string.bill_dialog_message_add)
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        viewModel.addNewBill(
                            customerId,
                            billPayment = billPayment,
                            billOff = billOff
                        )
                        viewModel.submitBill()
                        viewModel.changed()
                        viewModel.newBill.postValue(true)
                        viewModel.resetCardItems()

                        val action =
                            AddBillHomeFragmentDirections.actionAddBillHomeFragmentToBillItemListFragment(
                                viewModel.billId
                            )
                        this.findNavController().navigate(action)


                    }
                    .show()
            } else {
                if (!binding.billPayment.text.isNullOrBlank()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.bill_dialog_title_add)
                        .setMessage(R.string.bill_dialog_message_add)
                        .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to neutral button press
                        }
                        .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                            // Respond to negative button press
                        }
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            var billOff = binding.billOff.text.toString()
                            if (billOff.isNullOrBlank()) {
                                billOff = "0"
                            }
                            viewModel.addNewBill(
                                customerId,
                                billPayment = binding.billPayment.text.toString(),
                                billOff = billOff
                            )
                            viewModel.submitBill()
                            viewModel.changed()
                            viewModel.newBill.postValue(true)
                            viewModel.resetCardItems()

                            val action =
                                AddBillHomeFragmentDirections.actionAddBillHomeFragmentToBillItemListFragment(
                                    viewModel.billId
                                )
                            this.findNavController().navigate(action)
                        }
                        .show()
                }
            }
        }
    }


    private fun bindingItems() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.cardItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private fun showMenu(v: View, @MenuRes menuRes: Int, itemCardModel: ItemCardModel) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        getString(R.string.margin_popup_menu_icon).toFloat(),
                        resources.displayMetrics
                    )
                        .toInt()
                if (item?.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
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
                R.id.action_delete_item_card -> {
                    viewModel.removeItemInCard(itemCardModel)
                    adapter.notifyDataSetChanged()
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
        adapter.notifyDataSetChanged()

        super.onResume()
    }


}


