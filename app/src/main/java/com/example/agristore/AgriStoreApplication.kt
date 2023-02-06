package com.example.agristore

import android.app.Application
import com.example.agristore.data.AgriStoreRoomDatabase

class AgriStoreApplication : Application() {
    val database: AgriStoreRoomDatabase by lazy { AgriStoreRoomDatabase.getDatabase(this) }
}