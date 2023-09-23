package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.service.PostService
import com.citi.cloudlab.dao.repository.PostRepository
import org.springframework.stereotype.Component

/**
 *
 *@author Loki
 *@date 2023/5/10
 */
@Component
class PostServiceImpl(
    val postRepository: PostRepository
): PostService {
    override suspend fun save(post: Post): Post {
        postRepository.save(post, true)
        return post
    }

    override suspend fun update(post: Post) {
        postRepository.update(post)
    }

    override suspend fun delete(id: String) {
        postRepository.delete(id)
    }

    override suspend fun findAll(lastPostId: String?): List<Post> = postRepository.findAll(lastPostId)

    override suspend fun findById(id: String): Post {
        var post = postRepository.findById(id)
        //update views count
        post.views += 1
        postRepository.update(post)
        return post
    }

    override suspend fun findByCategory(categoryCode: String, lastPostId: String?): List<Post> = postRepository.findByCategory(categoryCode, lastPostId)

}