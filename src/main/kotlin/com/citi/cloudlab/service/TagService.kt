package com.citi.cloudlab.service

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.model.Tag

interface TagService: BaseService<Tag> {
    suspend fun findPosts(id: String, lastPostId: String?): List<Post>
}