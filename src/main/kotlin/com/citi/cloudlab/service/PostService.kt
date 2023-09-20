package com.citi.cloudlab.service

import com.nebula.dao.model.Post

/**
 *
 *@author Loki
 *@date 2023/5/10
 */
interface PostService {
    suspend fun findAll(lastPostId: String?): List<Post>
    suspend fun findById(id: String): Post
    suspend fun findByCategory(categoryCode: String, lastPostId: String?): List<Post>
    suspend fun save(post: Post): Post
    suspend fun update(post: Post)
    suspend fun delete(id: String)
}