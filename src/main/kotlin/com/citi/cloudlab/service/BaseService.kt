package com.citi.cloudlab.service


interface BaseService<T> {
    suspend fun findAll(lastEvaluatedKey: String?): List<T>
    suspend fun findById(id: String): T
    suspend fun save(entity: T): T
    suspend fun update(entity: T)
    suspend fun delete(id: String)
}