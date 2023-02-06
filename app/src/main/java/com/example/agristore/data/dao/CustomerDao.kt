package com.example.agristore.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.agristore.data.entities.Customer
import com.example.agristore.data.entities.relations.BillAndCustomerWithBillItemAndItem
import com.example.agristore.data.entities.relations.BillWithBillItem
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import com.example.agristore.data.entities.relations.CustomerWithBillAndBillItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.selects.select

@Dao
interface CustomerDao {
    @Query("select * from customer order by first_name,last_name")
    fun getCustomers(): Flow<List<Customer>>

    @Query("select * from customer order by first_name,last_name")
    suspend fun all(): List<Customer>

    @Query("select * from customer where id=:id")
    fun getCustomer(id: Int): Flow<Customer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Transaction
    @Query("select * from customer")
    fun getCustomersWithBillAndBillItems(): List<CustomerWithBillAndBillItem>

    @Transaction
    @Query("select * from customer where id=:id")
    fun getCustomersWithBillAndBillItemsById(id: Int): Flow<CustomerWithBillAndBillItem>

    @Transaction
    @Query("select * from bill where bill.id=:id")
    fun getBillWithBillItemAndItems(id: Int): Flow<BillWithBillItemAndItem>


    @Query("select * from customer join customer_fts on customer.id = customer_fts.rowid where customer_fts match:query")
    suspend fun search(query: String):List<Customer>

}