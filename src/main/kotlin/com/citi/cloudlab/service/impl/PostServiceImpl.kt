package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.model.PostTags
import com.citi.cloudlab.dao.model.Tag
import com.citi.cloudlab.service.PostService
import com.citi.cloudlab.dao.repository.PostRepository
import com.citi.cloudlab.dao.repository.PostTagsRepository
import com.citi.cloudlab.dao.repository.TagRepository
import org.springframework.stereotype.Component

/**
 *
 *@author Loki
 *@date 2023/5/10
 */
@Component
class PostServiceImpl(
    override val repository: PostRepository,
    val postTagsRepository: PostTagsRepository,
    val tagRepository: TagRepository
): PostService, BaseServiceImpl<Post>(repository) {

    override suspend fun findById(id: String): Post {
        var post = repository.findById(id)
        //update views count
        post.views += 1
        repository.update(post)
        return post
    }

    override suspend fun findByCategory(categoryCode: String, lastPostId: String?): List<Post> = repository.findByCategory(categoryCode, lastPostId)
    override suspend fun like(id: String): Post {
        var post = repository.findById(id)
        //update likes count
        post.likes += 1
        repository.update(post)
        return post
    }

    override suspend fun addTag(id: String, tag: Tag): Post {
        var post = repository.findById(id)
        post.apply {
            tags = tags?: ArrayList()
            tags = tags?.plus(tag.description!!)
        }

        postTagsRepository.save(id, tag.id!!)

        repository.update(post)
        return post
    }

}