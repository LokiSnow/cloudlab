package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.repository.BaseRepository
import com.citi.cloudlab.service.BaseService

open class BaseServiceImpl<T> (
    open val repository: BaseRepository<T>
) : BaseService<T> {
    override suspend fun findAll(lastTId: String?): List<T> = repository.findAll(lastTId)

    override suspend fun findById(id: String): T = repository.findById(id)

    override suspend fun save(entity: T): T {
        repository.save(entity, true)
        return entity
    }

    override suspend fun update(entity: T) {
        repository.update(entity)
    }

    override suspend fun delete(id: String) {
        repository.delete(id)
    }
}