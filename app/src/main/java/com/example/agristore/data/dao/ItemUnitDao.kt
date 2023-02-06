package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.ItemUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemUnitDao {
    @Query("SELECT * from item_unit ORDER BY name ASC")
    fun all(): List<ItemUnit>

    @Query("SELECT * from item_unit ORDER BY name ASC")
    fun getItemUnits(): Flow<List<ItemUnit>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemUnit: ItemUnit)

    suspend fun update(itemUnit: ItemUnit)

    @Delete
    suspend fun delete(itemUnit: ItemUnit)
}