package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BillItemDao {
    @Query("select * from billitem order by id desc")
    fun getBillItems(): Flow<List<BillItem>>
    @Query("select * from billitem where id=:id")
    suspend fun getBill(id:Int):BillItem
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg billItem: BillItem)

    @Update
    suspend fun update(billItem: BillItem)

    @Delete
    suspend fun delete(billItem: BillItem)


}