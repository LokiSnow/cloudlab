package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.common.constant.Constants.Companion.CATEGORY_INDEX
import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.repository.PostRepository
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

/**
 *
 *@author Loki
 *@date 2023/4/20
 */
@Component
class PostRepositoryImpl() : BaseRepositoryImpl<Post>(Post::class.java), PostRepository {

    private val postListProjection = listOf("id", "previewContent", "title", "categoryCode", "author", "tags", "likes", "createdTime")
    override suspend fun findByCategory(categoryCode: String, lastEvaluatedKey: String?) : List<Post> {
        return mappedTable.index(CATEGORY_INDEX).query { builder ->
            builder.limit(20)
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(categoryCode).build()))
                .scanIndexForward(false)
                .attributesToProject(postListProjection)
            lastEvaluatedKey?.let {
                builder.exclusiveStartKey(mapOf("categoryCode" to AttributeValue.fromS(categoryCode), "id" to AttributeValue.fromS(lastEvaluatedKey)))
            }
        }.first().items()
    }

    override fun getListProjection(): List<String> = postListProjection
}

