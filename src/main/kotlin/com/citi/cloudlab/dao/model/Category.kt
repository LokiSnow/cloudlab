package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
data class Category (
    var code: String? = null,
    var description: String? = null
) : BaseEntity()
