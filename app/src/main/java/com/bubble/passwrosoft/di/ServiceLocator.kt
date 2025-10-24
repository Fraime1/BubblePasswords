package com.bubble.passwrosoft.di

import android.content.Context
import androidx.room.Room
import com.bubble.passwrosoft.data.db.AppDatabase
import com.bubble.passwrosoft.data.preferences.PreferencesManager
import com.bubble.passwrosoft.data.repository.VaultRepository

object ServiceLocator {
    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: VaultRepository? = null
    @Volatile private var prefs: PreferencesManager? = null

    fun repository(context: Context): VaultRepository {
        val currentRepo = repo
        if (currentRepo != null) return currentRepo
        synchronized(this) {
            val database = db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "bubble_passwords.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { db = it }
            val created = VaultRepository(database.vaultDao())
            repo = created
            return created
        }
    }

    fun preferences(context: Context): PreferencesManager {
        val currentPrefs = prefs
        if (currentPrefs != null) return currentPrefs
        synchronized(this) {
            val created = PreferencesManager(context.applicationContext)
            prefs = created
            return created
        }
    }
}


