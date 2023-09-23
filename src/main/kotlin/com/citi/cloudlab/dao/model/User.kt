package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey

@DynamoDbBean
data class User (
    var name: String? = null,
    var password: String? = null,
    @get:DynamoDbSecondaryPartitionKey(indexNames = [ "email-index" ])
    var email: String? = null
) : BaseEntity()
