package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.model.Tag
import com.citi.cloudlab.dao.repository.PostTagsRepository
import com.citi.cloudlab.dao.repository.TagRepository
import com.citi.cloudlab.service.TagService
import org.springframework.stereotype.Component

@Component
class TagServiceImpl(
    override val repository: TagRepository,
    private val postTagsRepository: PostTagsRepository
) : TagService, BaseServiceImpl<Tag>(repository) {
    override suspend fun findPosts(id: String, lastPostId: String?): List<Post> = postTagsRepository.findPosts(id, lastPostId)
}