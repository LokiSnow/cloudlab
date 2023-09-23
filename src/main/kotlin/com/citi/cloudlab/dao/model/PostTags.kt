package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

/**
 * relation of post and tags, redundant table for query by tag
 *@author Loki
 *@date 2023/5/9
 */
@DynamoDbBean
data class PostTags(
    @get:DynamoDbPartitionKey
    var tagId: String? = null,
    @get:DynamoDbSortKey
    var postId: String? = null,
)