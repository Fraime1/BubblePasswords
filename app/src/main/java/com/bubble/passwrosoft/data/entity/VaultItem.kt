package com.bubble.passwrosoft.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vault_items")
data class VaultItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val service: String,
    val username: String,
    val password: String,
    val favorite: Boolean = false,
    val useCount: Int = 0,
)


