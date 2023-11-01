package com.citi.cloudlab.service

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.model.Tag


/**
 *
 *@author Loki
 *@date 2023/5/10
 */
interface PostService : BaseService<Post>{
    suspend fun findByCategory(categoryCode: String, lastPostId: String?): List<Post>
    suspend fun like(id: String): Post
    suspend fun addTag(id: String, tag: Tag): Post
}