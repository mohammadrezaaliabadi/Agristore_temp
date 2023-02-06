package com.example.agristore.data.dao

import androidx.room.*
import com.example.agristore.data.entities.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * from image WHERE id = :id")
    fun getImage(id: String): Flow<Image>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image)
    @Delete
    suspend fun delete(image: Image)

    @Query("SELECT * from image ORDER BY title ASC")
    fun all(): List<Image>

    @Query("SELECT * from image ORDER BY title ASC")
    fun getItems(): Flow<List<Image>>
}