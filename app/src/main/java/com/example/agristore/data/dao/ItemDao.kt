package com.example.agristore.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.agristore.data.entities.Item
import com.example.agristore.data.entities.Product
import com.example.agristore.data.entities.relations.ItemWithBillItemAndBillAndCustomer

import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ItemDao {

    @Query("SELECT * from item ORDER BY name ASC")
    fun all(): List<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("select * from item join item_fts on item.id = item_fts.rowid where item_fts match :query")
    suspend fun search(query: String): List<Item>

    @Query("select * from item where id= :id")
    fun getItemWithCustomer(id: Int): Flow<ItemWithBillItemAndBillAndCustomer>
}