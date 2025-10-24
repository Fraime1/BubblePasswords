package com.bubble.passwrosoft.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bubble.passwrosoft.data.entity.VaultItem
import kotlinx.coroutines.flow.Flow

@Dao
interface VaultDao {
    @Query("SELECT * FROM vault_items ORDER BY service ASC")
    fun observeAll(): Flow<List<VaultItem>>

    @Query("SELECT * FROM vault_items WHERE id = :id")
    fun observeById(id: Long): Flow<VaultItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: VaultItem): Long

    @Update
    suspend fun update(item: VaultItem)

    @Delete
    suspend fun delete(item: VaultItem)
}


