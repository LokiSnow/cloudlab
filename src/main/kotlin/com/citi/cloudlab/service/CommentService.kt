package com.citi.cloudlab.service

import com.citi.cloudlab.dao.model.Comment

interface CommentService: BaseService<Comment> {
    suspend fun findByPostId(postId: String, lastCommentId: String?): List<Comment>
}