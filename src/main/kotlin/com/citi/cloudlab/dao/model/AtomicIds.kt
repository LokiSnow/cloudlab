package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

/**
 *
 *@author Loki
 *@date 2023/5/9
 */
@DynamoDbBean
open class AtomicIds {
    @get:DynamoDbPartitionKey
    var timestamp: Long? = null
    var expiry: Long? = null
}