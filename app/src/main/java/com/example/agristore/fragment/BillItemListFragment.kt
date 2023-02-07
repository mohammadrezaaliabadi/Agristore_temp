package com.example.agristore.fragment


import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agristore.AgriStoreApplication
import com.example.agristore.R
import com.example.agristore.data.entities.getFormattedCurrency
import com.example.agristore.databinding.FragmentBillItemListBinding
import com.example.agristore.fragment.adapter.BillItemWithItemListAdapter
import com.example.agristore.model.BillSendModel
import com.example.agristore.utillity.PersianDateConvertor
import com.example.agristore.viewmodel.BillViewModel
import com.example.agristore.viewmodel.BillViewModelFactory
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGenerator.XmlToPDFLifecycleObserver
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse
import java.text.SimpleDateFormat


class BillItemListFragment : Fragment() {

    lateinit var xmlToPDFLifecycleObserver: XmlToPDFLifecycleObserver

    private val navigationArgs: BillItemListFragmentArgs by navArgs()
    private val viewModel: BillViewModel by activityViewModels {
        BillViewModelFactory(
            (activity?.application as AgriStoreApplication).database.itemDao(),
            (activity?.application as AgriStoreApplication).database.billItemDao(),
            (activity?.application as AgriStoreApplication).database.billDao(),
            (activity?.application as AgriStoreApplication).database.customerDao()
        )
    }
    private val adapter: BillItemWithItemListAdapter = BillItemWithItemListAdapter { item ->
        val action =
            BillItemListFragmentDirections.actionBillItemListFragmentToItemDetailFragment(item.id)
        this.findNavController().navigate(action)
    }

    private var _binding: FragmentBillItemListBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBillItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingItems()

        xmlToPDFLifecycleObserver = XmlToPDFLifecycleObserver(requireActivity())
        lifecycle.addObserver(xmlToPDFLifecycleObserver)
        addMenu()
    }

    private fun pdfGenerator() {
        PdfGenerator.getBuilder()
            .setContext(activity)
            .fromViewSource()
            .fromView(binding.root)
            .setFileName("Bill")
            .setFolderNameOrPath("Bill-folder")
            .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
            .savePDFSharedStorage(xmlToPDFLifecycleObserver)
            .build(object : PdfGeneratorListener() {
                override fun onFailure(failureResponse: FailureResponse) {
                    super.onFailure(failureResponse)
                }

                override fun showLog(log: String) {
                    super.showLog(log);
                    Log.d("PDF-generation", log);
                }

                override fun onStartPDFGeneration() {
                    /*When PDF generation begins to start*/
                }

                override fun onFinishPDFGeneration() {
                    /*When PDF generation is finished*/
                }

                override fun onSuccess(response: SuccessResponse) {
                    super.onSuccess(response)
                }
            })
    }

    private fun bindingItems() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.getBillWithBillItemAndItems(billId = navigationArgs.billId)
            .observe(this.viewLifecycleOwner) { items ->
                items.let {

                    adapter.submitList(it.billItemWithItems)
                    val billTotalItemPrice =
                        it.billItemWithItems.map { it.billItem.quantity * (it.billItem.price - it.billItem.off

                                ) }.sum()
                    val billSendModel = BillSendModel(
                        billCode = it.bill.billCode,
                        billDate = PersianDateConvertor().convertDateToPersianDate(it.bill.date)
                            .toString() + " " + SimpleDateFormat("HH:mm:ss").format(it.bill.date),
                        billOff = it.bill.off.getFormattedCurrency(),
                        billPayment = it.bill.payment.getFormattedCurrency(),
                        billTotalItemPrice = billTotalItemPrice.getFormattedCurrency(),
                        billTotal = (billTotalItemPrice - it.bill.off - it.bill.payment).getFormattedCurrency()
                    )
                    binding.headerLayout.billCode.text = billSendModel.billCode
                    binding.headerLayout.billDate.text = billSendModel.billDate
                    binding.headerLayout.billOff.text = billSendModel.billOff
                    binding.headerLayout.billPayment.text = billSendModel.billPayment
                    binding.headerLayout.billPrice.text = billSendModel.billTotal
                    bindingCustomer(it.bill.customerId, billSendModel)
                }
            }

    }

    private fun bindingCustomer(customerId: Int, billSendModel: BillSendModel) {
        viewModel.retrieveCustomer(customerId).observe(this.viewLifecycleOwner) {
            it.let {

                if (viewModel.newBill.value == true) {
                    viewModel.newBill.postValue(false)
                    billSendModel.customerTel = it.tel
                    sendSMS(it.tel, billSendModel)
                }
                binding.headerLayout.customerName.text = it.firstName + " " + it.lastName
                binding.headerLayout.customerNationalNumber.text = it.nationalNumber.toString()
                binding.headerLayout.customerTel.text = it.tel
            }
        }
    }

    private fun addMenu() {
        val menuHost = activity as MenuHost
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.bill_item_list_menu, menu)

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_bill_item_list_print -> {
                    pdfGenerator()
                    return true
                }
                else -> false
            }
        }
    }

    private fun sendSMS(
        tel: String,
        billSendModel: BillSendModel
    ) {
        var smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
            tel,
            null,
            "AgriStore\nBill Code:${billSendModel.billCode}\nDate:${
                billSendModel.billDate
            }\nBill Off:${billSendModel.billOff}\nBill Payment:${billSendModel.billPayment}\nBill Item price:${billSendModel.billTotalItemPrice}\nTotal:${billSendModel.billTotal}",
            null,
            null
        )
    }

    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }


}


