package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.Company
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
    @Query("SELECT * from company ORDER BY name ASC")
    fun all(): List<Company>

    @Query("SELECT * from company ORDER BY name ASC")
    fun getCompanies(): Flow<List<Company>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(company: Company)

    suspend fun update(company: Company)

    @Delete
    suspend fun delete(company: Company)

}