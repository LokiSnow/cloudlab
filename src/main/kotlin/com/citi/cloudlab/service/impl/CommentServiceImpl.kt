package com.citi.cloudlab.service.impl

import com.citi.cloudlab.dao.model.Comment
import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.repository.CommentRepository
import com.citi.cloudlab.service.CommentService
import org.springframework.stereotype.Component

@Component
class CommentServiceImpl(
    override val repository: CommentRepository
): CommentService, BaseServiceImpl<Comment>(repository) {
    override suspend fun findByPostId(postId: String, lastCommentId: String?): List<Comment> = repository.findByPostId(postId, lastCommentId)
}