package com.example.agristore.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.agristore.data.dao.ItemDao
import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.relations.ItemWithBillItemAndBillAndCustomer
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the ItemRepository repository and an up-to-date list of all items.
 *
 */
class ItemViewModel(private val itemDao: ItemDao) : ViewModel() {
    private val _changeResults = MutableLiveData<String>()
    val changeResults : LiveData<String>
        get() = _changeResults
    private val _searchResults = MutableLiveData<List<Item>>()
    val searchResult: LiveData<List<Item>>
        get() = _searchResults
    // Cache all items form the database using LiveData.
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

    private fun fetchAllItem() {
        viewModelScope.launch {
            itemDao.all().let {
                _searchResults.postValue(it)
            }
        }
    }

    /**
     * Inserts the new Item into database.
     */

    fun addNewItem(itemName: String, itemQuantity: String, itemPrice: String) {
        val newItem = getNewItemEntry(itemName, itemQuantity, itemPrice)
        insertItem(newItem)
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }

    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }



    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }


    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(itemName: String, itemQuantity: String, itemPrice: String): Item {
        return Item(
            name = itemName,
            quantity = itemQuantity.toLong(),
            price = itemPrice.toLong()
        )
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemQuantity: String,
        itemPrice: String
    ): Item {
        return Item(
            id = itemId,
            name = itemName,
            quantity = itemQuantity.toLong(),
            price = itemPrice.toLong()
        )
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(itemName: String, itemQuantity: String, itemPrice: String): Boolean {
        if (itemName.isBlank() || itemQuantity.isBlank() || itemPrice.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns true if stock is available to sell, false otherwise.
     */

    fun itemSearch(query: String) {
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                itemDao.all().let {
                    _searchResults.postValue(it)
                }
            } else {
                itemDao.search("*$query*").let {
                    _searchResults.postValue(it)

                }
            }
        }
    }

    fun findItemAndCustomerById(itemId:Int): LiveData<ItemWithBillItemAndBillAndCustomer> {
        return itemDao.getItemWithCustomer(itemId).asLiveData()
    }


}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class ItemViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}