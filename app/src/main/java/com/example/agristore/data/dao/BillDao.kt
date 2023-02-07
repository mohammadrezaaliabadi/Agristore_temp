package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.relations.BillAndCustomerWithBillItemAndItem
import com.example.agristore.data.entities.relations.BillWithBillItemAndItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("select * from bill order by id desc")
    fun getBills(): Flow<List<Bill>>

    @Query("select * from bill where id=:id")
    fun getBill(id: Long): Flow<Bill>

    @Query("select * from bill where billCode=:billCode")
    fun getBillByBillCode(billCode: String): Bill

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bill: Bill):Long

    @Update
    suspend fun update(bill: Bill)

    @Delete
    suspend fun delete(bill: Bill)

    @Transaction
    @Query("select * from bill where bill.id=:id")
    fun getBillWithBillItemAndItems(id: Long): Flow<BillWithBillItemAndItem>


    @Transaction
    @Query("select * from bill order by date desc")
    fun getBillAndCustomerWithBillItemAndItems(): Flow<List<BillAndCustomerWithBillItemAndItem>>


    @Query("select * from bill order by date desc")
    suspend fun all(): List<BillAndCustomerWithBillItemAndItem>

    @Query("select * from bill where billCode like :query order by date desc")
    suspend fun search(query: String): List<BillAndCustomerWithBillItemAndItem>

    @Query("select * from bill where id like :query order by date desc")
    suspend fun searchById(query: String): List<BillAndCustomerWithBillItemAndItem>
}