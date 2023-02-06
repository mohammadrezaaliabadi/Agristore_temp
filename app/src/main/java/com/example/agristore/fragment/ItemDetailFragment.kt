package com.example.agristore.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.UUIDUtil
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.data.entities.Image
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.getFormattedPrice
import com.example.agristore.data.entities.relations.BillItemWithBillAndCustomer
import com.example.agristore.databinding.FragmentItemDetailBinding
import com.example.agristore.fragment.adapter.BillItemWithBillAndCustomerAdapter
import com.example.agristore.viewmodel.ImageViewModel
import com.example.agristore.viewmodel.ImageViewModelFactory
import com.example.agristore.viewmodel.ItemViewModel
import com.example.agristore.viewmodel.ItemViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.net.URI
import java.net.URL
import java.util.UUID

class ItemDetailFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    lateinit var item: Item
    private val pickImage = 100

    private val viewModel: ItemViewModel by activityViewModels {
        ItemViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao()
        )
    }

    private val imageViewModel: ImageViewModel by activityViewModels {
        ImageViewModelFactory(
            (activity?.application as AgriStoreApplication).database.imageDao()
        )
    }
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(item: Item) {
        binding.apply {
            itemName.text = item.name
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantity.toString()
            deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener { editItem() }
        }
    }

    /**
     * Navigate to the Edit item screen.
     */
    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            item.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                run {
                    imageViewModel.getImage(imageId).observe(viewLifecycleOwner) {
                        binding.imageView.setImageBitmap(it.bitmap)
                    }
                }
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                openSomeActivityForResult()
                //deleteItem()
            }
            .show()
    }

    val imageId = UUID.randomUUID().toString()
    @RequiresApi(Build.VERSION_CODES.P)
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result?.data
                val imageSource =
                    result?.data?.data?.let {
                        ImageDecoder.createSource(
                            context?.contentResolver!!,
                            it
                        )
                    }
                val bitmap = imageSource?.let { ImageDecoder.decodeBitmap(it) }
                bitmap?.let { Image(imageId, "", it) }
                    ?.let { imageViewModel.insertImage(it) }
            }
        }

    fun openSomeActivityForResult() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            resultLauncher.launch(intent)
        }
    }


    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }

        bindingItemBillCustomer(id)

    }

    private fun bindingItemBillCustomer(itemId: Int) {
        val adapter =
            BillItemWithBillAndCustomerAdapter { billItemWithBillAndCustomer, view ->
                showMenu(view, R.menu.bill_item_customer_menu, billItemWithBillAndCustomer)
            }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.findItemAndCustomerById(itemId = itemId).observe(this.viewLifecycleOwner) {
            it.let {
                adapter.submitList(it.billItemWithBillAndCustomers)
            }
        }

    }

    @SuppressLint("RestrictedApi")
    private fun showMenu(
        v: View,
        @MenuRes menuRes: Int,
        billItemWithBillAndCustomer: BillItemWithBillAndCustomer
    ) {
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
                R.id.action_bill_item_customer_show_bill -> {
                    val action =
                        ItemDetailFragmentDirections.actionItemDetailFragmentToBillItemListFragment(
                            billItemWithBillAndCustomer.billAndCustomer.bill.id
                        )
                    this.findNavController().navigate(action)
                }
                R.id.action_bill_item_customer_show_customer -> {
                    val action =
                        ItemDetailFragmentDirections.actionItemDetailFragmentToCustomerDetailFragment(
                            billItemWithBillAndCustomer.billAndCustomer.customer.id
                        )
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

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}