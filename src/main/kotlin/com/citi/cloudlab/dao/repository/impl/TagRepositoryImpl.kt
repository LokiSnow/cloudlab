package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.dao.model.Tag
import com.citi.cloudlab.dao.model.tagId
import com.citi.cloudlab.dao.repository.TagRepository
import org.springframework.stereotype.Component

/**
 *
 *@author Loki
 *@date 2023/5/10
 */
@Component
class TagRepositoryImpl : BaseRepositoryImpl<Tag>(Tag::class.java), TagRepository {
    /**
     * set id by lower case tag name, and replace space to '-'
     */
    override suspend fun fillId(entity: Tag, incremental: Boolean): Tag  = entity.apply { id = name?.tagId() }
}