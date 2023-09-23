package com.citi.cloudlab.dao.repository

/**
 *
 *@author Loki
 *@date 2023/4/20
 */
interface BaseRepository<T>  {
    suspend fun save(entity: T)
    suspend fun save(entity: T, incremental: Boolean)
    suspend fun save(entities: List<T>, incremental: Boolean)
    suspend fun loadAtomicId(): String
    suspend fun findById(id: String): T
    suspend fun findByIds(ids: List<String>): List<T>
    suspend fun findAll(lastEvaluatedKey: String?): List<T>
    suspend fun delete(id: String): T
    suspend fun update(entity: T): T

}