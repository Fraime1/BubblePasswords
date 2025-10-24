package com.bubble.passwrosoft.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bubble.passwrosoft.data.dao.VaultDao
import com.bubble.passwrosoft.data.entity.VaultItem

@Database(entities = [VaultItem::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vaultDao(): VaultDao
}


