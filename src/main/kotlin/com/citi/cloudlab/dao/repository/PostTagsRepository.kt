package com.citi.cloudlab.dao.repository

import com.citi.cloudlab.dao.model.Post


/**
 *
 *@author Loki
 *@date 2023/5/10
 */
interface PostTagsRepository {
    suspend fun save(postId: String, tags: List<String>?)

    suspend fun save(postId: String, tagId: String)
    suspend fun findPosts(tagId: String, lastPostId: String?): List<Post>

}