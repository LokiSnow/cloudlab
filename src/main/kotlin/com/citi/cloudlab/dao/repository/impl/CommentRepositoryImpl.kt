package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.common.constant.Constants
import com.citi.cloudlab.dao.model.Comment
import com.citi.cloudlab.dao.repository.CommentRepository
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.services.dynamodb.model.AttributeValue


/**
 *
 *@author Loki
 *@date 2023/5/11
 */
@Component
class CommentRepositoryImpl : BaseRepositoryImpl<Comment>(Comment::class.java), CommentRepository {
    override suspend fun findByPostId(postId: String, lastCommentId: String?): List<Comment> {
        return mappedTable.index(Constants.COMMENT_INDEX).query { builder ->
            builder.limit(20)
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(postId).build()))
                .scanIndexForward(false)
            lastCommentId?.let {
                builder.exclusiveStartKey(mapOf("postId" to AttributeValue.fromS(postId), "id" to AttributeValue.fromS(lastCommentId)))
            }
        }.first().items()
    }
}