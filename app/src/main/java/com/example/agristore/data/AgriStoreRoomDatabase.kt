package com.example.agristore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.agristore.data.dao.*
import com.example.agristore.data.entities.*
import com.example.agristore.data.entities.typeconverter.DateTypeConvertor
import com.example.agristore.data.entities.typeconverter.ImageTypeConverter

@Database(
    entities = [Customer::class, CustomerFTS::class, Item::class,ItemFTS::class, Product::class, ProductFTS::class, Bill::class, BillItem::class, Image::class, ItemUnit::class, Company::class, Store::class],
    version = 15
)
@TypeConverters(value = [ImageTypeConverter::class, DateTypeConvertor::class])
abstract class AgriStoreRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun billItemDao(): BillItemDao
    abstract fun billDao(): BillDao
    abstract fun customerDao(): CustomerDao
    abstract fun imageDao(): ImageDao


    companion object {
        @Volatile
        private var INSTANCE: AgriStoreRoomDatabase? = null

        fun getDatabase(context: Context): AgriStoreRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgriStoreRoomDatabase::class.java,
                    "agristore_database"
                ).allowMainThreadQueries()
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}