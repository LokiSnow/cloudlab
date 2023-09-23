package com.citi.cloudlab.dao.model

import com.citi.cloudlab.common.constant.Constants.Companion.LIKE_INDEX
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import java.time.LocalDateTime

/**
 *
 *@author Loki
 *@date 2023/4/25
 */
//@DynamoDbBean
data class Like(
    @get:DynamoDbSecondaryPartitionKey(indexNames = [ LIKE_INDEX ])
    var userId: String? = null,
    var postId: String? = null,
    @get:DynamoDbSecondarySortKey(indexNames = [ LIKE_INDEX ])
    override var createdTime: LocalDateTime? = LocalDateTime.now()
): BaseEntity()
