package com.bubble.passwrosoft.data.repository

import com.bubble.passwrosoft.data.dao.VaultDao
import com.bubble.passwrosoft.data.entity.VaultItem
import kotlinx.coroutines.flow.Flow

class VaultRepository(private val dao: VaultDao) {
    fun observeAll(): Flow<List<VaultItem>> = dao.observeAll()
    fun observeById(id: Long): Flow<VaultItem?> = dao.observeById(id)
    suspend fun add(service: String, username: String, password: String) {
        dao.insert(VaultItem(service = service, username = username, password = password))
    }
    suspend fun update(item: VaultItem) = dao.update(item)
    suspend fun delete(item: VaultItem) = dao.delete(item)
}


