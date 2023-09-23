package com.citi.cloudlab.dao.model

import com.citi.cloudlab.common.constant.Constants.Companion.COMMENT_INDEX
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import java.time.LocalDateTime

@DynamoDbBean
data class Comment (
    var userId: String? = null,
    @get:DynamoDbSecondaryPartitionKey(indexNames = [ COMMENT_INDEX ])
    var postId: String? = null,
    var content: String? = null,
    @get:DynamoDbSecondarySortKey(indexNames = [ COMMENT_INDEX ])
    override var createdTime: LocalDateTime? = LocalDateTime.now()
) : BaseEntity()
