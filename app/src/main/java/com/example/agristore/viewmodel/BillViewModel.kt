package com.example.agristore.viewmodel

import androidx.lifecycle.*
import androidx.room.Transaction
import com.example.agristore.data.dao.BillDao
import com.example.agristore.data.dao.BillItemDao
import com.example.agristore.data.dao.CustomerDao
import com.example.agristore.data.dao.ItemDao
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem
import com.example.agristore.data.entities.Customer
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.relations.BillAndCustomerWithBillItemAndItem
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.model.ItemCardModel
import kotlinx.coroutines.launch
import java.util.*

class BillViewModel(
    private val itemDao: ItemDao,
    private val billItemDao: BillItemDao,
    private val billDao: BillDao,
    private val customerDao: CustomerDao,
) :
    ViewModel() {
    private val _searchItemResults = MutableLiveData<List<Item>>()
    val searchItemResults: LiveData<List<Item>>
        get() = _searchItemResults

    private val _changeResults = MutableLiveData<String>()
    val changeResults: LiveData<String>
        get() = _changeResults

    var customerId = 0

    val newBill = MutableLiveData(false)




    // Cache all Customers form the database using LiveData.
    private val _searchResults = MutableLiveData<List<BillAndCustomerWithBillItemAndItem>>()
    val searchResult: LiveData<List<BillAndCustomerWithBillItemAndItem>>
        get() = _searchResults

    init {
        fetchAllItem()
        _changeResults.postValue("")
    }

    fun changed() {
        _changeResults.postValue("changed")
    }

    fun setDefaultChange() {
        _changeResults.postValue("")
    }

    private fun fetchAllItem() {
        viewModelScope.launch {
            billDao.all().let {
                _searchResults.postValue(it)
            }
        }
    }

    fun itemSearch(query: String) {
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                itemDao.all().let {
                    _searchItemResults.postValue(it)
                }
            } else {
                itemDao.search("*$query*").let {
                    _searchItemResults.postValue(it)
                }
            }
        }
    }

    fun billSearch(query: String) {
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                billDao.all().let {
                    _searchResults.postValue(it)
                }
            } else {
                billDao.search("%$query%").let {
                    _searchResults.postValue(it)
                }
            }
        }
    }

    var itemsBuy: MutableMap<Item, Long> = mutableMapOf()
    var cardItems = MutableLiveData<MutableList<ItemCardModel>>()

    fun resetCardItems(){
        cardItems.value.let {
            it?.removeAll { true }
        }
    }

    private lateinit var billCode: String
    var billId: Int = 0

    fun addItemToBuy(item: Item, count: Long) {
        itemsBuy[item] = count
    }

    fun addItemToCard(item: Item, count: Long) {
        if (cardItems.value.isNullOrEmpty()) {
            cardItems.value = mutableListOf(ItemCardModel(item, count))
        } else {
            val temp = cardItems.value?.find { item.id == it.item.id }
            if (temp != null) {
                temp.quantity = count
            } else {
                cardItems.value?.add(ItemCardModel(item, count))
            }
        }
    }

    fun removeItemInCard(itemCardModel: ItemCardModel) {
        if (cardItems.value.isNullOrEmpty()) {
            return
        }
        cardItems.value!!.remove(itemCardModel)
    }

    fun getBillAndCustomerWithBillItemAndItems(): LiveData<List<BillAndCustomerWithBillItemAndItem>> {
        return billDao.getBillAndCustomerWithBillItemAndItems().asLiveData()
        //return null!!
    }

    /**
     * Retrieve an Customer from the repository.
     */

    fun retrieveCustomer(id: Int): LiveData<Customer> {
        return customerDao.getCustomer(id).asLiveData()
    }

    fun retrieveBill(id: Int): LiveData<Bill> {
        return billDao.getBill(id).asLiveData()
    }

    fun addNewBill(customerId: Int, billPayment: String, billOff: String) {
        billCode = UUID.randomUUID().toString()
        val newBill = getNewBillEntry(billCode, customerId, billPayment.toLong(), billOff.toLong())
        insertBill(newBill)
        billId = retrieveBillByBillCode(billCode).id
    }

    private fun insertBill(bill: Bill) {
        viewModelScope.launch {
            billDao.insert(bill)
        }
    }

    private fun getNewBillEntry(
        billCode: String,
        customerId: Int,
        billPayment: Long,
        billOff: Long
    ): Bill {
        return Bill(
            billCode = billCode,
            date = Date(),
            payment = billPayment,
            off = billOff,
            customerId = customerId
        )
    }


    private fun retrieveBillByBillCode(billCode: String): Bill {
        return billDao.getBillByBillCode(billCode)
    }

    fun getBillWithBillItemAndItems(billId: Int): LiveData<BillWithBillItemAndItem> {
        return billDao.getBillWithBillItemAndItems(billId).asLiveData()
    }

    @Transaction
    fun submitBill() {
        viewModelScope.launch {
            for ((item, count) in itemsBuy.entries) {
                billItemDao.insert(
                    BillItem(
                        billId = billId,
                        itemId = item.id,
                        quantity = count,
                    )
                )
                val quantity = item.quantity - count
                itemDao.update(Item(item.id,item.name,item.price,quantity))
            }
            itemsBuy = mutableMapOf()
            newBill.postValue(true)
        }

    }

}

class BillViewModelFactory(
    private val itemDao: ItemDao,
    private val billItemDao: BillItemDao,
    private val billDao: BillDao,
    private val customerDao: CustomerDao
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BillViewModel(itemDao, billItemDao, billDao, customerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}