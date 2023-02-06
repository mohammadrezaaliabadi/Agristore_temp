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
import com.example.agristore.data.entities.relations.ProductWithBillItemAndBillAndCustomer

import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ProductDao {

    @Query("SELECT * from product ORDER BY name ASC")
    fun all(): List<Product>

    @Query("SELECT * from product ORDER BY name ASC")
    fun getItems(): Flow<List<Product>>

    @Query("SELECT * from product WHERE id = :id")
    fun getItem(id: Int): Flow<Product>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("select * from product join product_fts on product.id = product_fts.rowid where product_fts match :query")
    suspend fun search(query: String): List<Product>

    @Query("select * from item where id= :id")
    fun getProductWithCustomer(id: Int): Flow<ProductWithBillItemAndBillAndCustomer>

}