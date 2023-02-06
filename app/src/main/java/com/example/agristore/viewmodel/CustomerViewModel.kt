package com.example.agristore.viewmodel

import androidx.lifecycle.*
import androidx.room.Query
import com.example.agristore.data.dao.CustomerDao
import com.example.agristore.data.entities.Customer
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.relations.BillAndCustomerWithBillItemAndItem
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.data.entities.relations.CustomerWithBillAndBillItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CustomerViewModel(private val customerDao: CustomerDao) : ViewModel() {
    // Cache all Customers form the database using LiveData.

    private val _changeResults = MutableLiveData<String>()
    val changeResults : LiveData<String>
        get() = _changeResults
    private val _searchResults = MutableLiveData<List<Customer>>()
    val searchResult: LiveData<List<Customer>>
        get() = _searchResults

    init {
        fetchAllItem()
        _changeResults.postValue("")
    }

    fun changed(){
        _changeResults.postValue("changed")
    }

    fun setDefaultChange(){
        _changeResults.postValue("")
    }
    private fun fetchAllItem(){
        viewModelScope.launch {
            customerDao.all().let{
                _searchResults.postValue(it)
            }
        }
    }

    fun customerSearch(query: String){
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                customerDao.all().let {
                    _searchResults.postValue(it)
                }
            } else {
                customerDao.search("*$query*").let {
                    _searchResults.postValue(it)

                }
            }
        }
    }

    /**
     * Inserts the new Customer into database.
     */

    fun addNewCustomer(
        customerFirstName: String,
        customerLastName: String,
        customerNationalNumber: String,
        customerTel: String
    ) {
        val newCustomer = getNewCustomerEntry(
            customerFirstName,
            customerLastName,
            customerNationalNumber,
            customerTel
        )
        insertCustomer(newCustomer)
    }

    /**
     * Launching a new coroutine to Insert an item in a non-blocking way
     */
    private fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.insert(customer)
        }
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(
        customerFirstName: String,
        customerLastName: String,
        customerNationalNumber: String,
        customerTel: String
    ): Boolean {
        if (customerFirstName.isBlank() || customerLastName.isBlank() || customerNationalNumber.isBlank() || customerTel.isBlank()) {
            return false
        }
        return true
    }

    private fun getNewCustomerEntry(
        customerFirstName: String,
        customerLastName: String,
        customerNationalNumber: String,
        customerTel: String
    ): Customer {
        return Customer(
            firstName = customerFirstName,
            lastName = customerLastName,
            nationalNumber = customerNationalNumber.toLong(),
            tel = customerTel
        )
    }

    fun updateCustomer(
        customerId: Int,
        customerFirstName: String,
        customerLastName: String,
        customerNationalNumber: String,
        customerTel: String
    ) {
        val updatedCustomer = getUpdatedCustomerEntry(
            customerId,
            customerFirstName,
            customerLastName,
            customerNationalNumber,
            customerTel
        )
        updateCustomer(updatedCustomer)
    }

    /**
     * Launching a new coroutine to update an customer in a non-blocking way
     */
    private fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.update(customer)
        }
    }

    /**
     * Launching a new coroutine to delete an customer in a non-blocking way
     */
    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.delete(customer)
        }
    }

    /**
     * Retrieve an Customer from the repository.
     */
    fun retrieveCustomer(id: Int): LiveData<Customer> {
        return customerDao.getCustomer(id).asLiveData()
    }

    /**
     * Updates an existing Customer in the database.
     */

    private fun getUpdatedCustomerEntry(
        customerId: Int,
        customerFirstName: String,
        customerLastName: String,
        customerNationalNumber: String,
        customerTel: String
    ): Customer {
        return Customer(
            id = customerId,
            firstName = customerFirstName,
            lastName = customerLastName,
            nationalNumber = customerNationalNumber.toLong(),
            tel = customerTel
        )
    }

    fun getCustomersWithBillAndBillItems(): List<CustomerWithBillAndBillItem> {
        return customerDao.getCustomersWithBillAndBillItems()
    }

    fun getCustomersWithBillAndBillItemsById(id: Int): LiveData<CustomerWithBillAndBillItem> {
        return customerDao.getCustomersWithBillAndBillItemsById(id).asLiveData()
    }


}

class CustomerViewModelFactory(private val customerDao: CustomerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}