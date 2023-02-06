package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.Store
import kotlinx.coroutines.flow.Flow

@Dao

interface StoreDao{
    @Query("SELECT * from store ORDER BY name ASC")
    fun all(): List<Store>

    @Query("SELECT * from store ORDER BY name ASC")
    fun getStores(): Flow<List<Store>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(store: Store)

    suspend fun update(store: Store)

    @Delete
    suspend fun delete(store: Store)
}
