package com.citi.cloudlab.dao.repository

import com.citi.cloudlab.dao.model.Post

interface PostRepository : BaseRepository<Post> {
    suspend fun findByCategory(categoryCode: String, lastEvaluatedKey: String?): List<Post>
}