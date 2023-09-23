package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.dao.model.Comment
import com.citi.cloudlab.dao.repository.CommentRepository


/**
 *
 *@author Loki
 *@date 2023/5/11
 */
class CommentRepositoryImpl : BaseRepositoryImpl<Comment>(Comment::class.java), CommentRepository {
}