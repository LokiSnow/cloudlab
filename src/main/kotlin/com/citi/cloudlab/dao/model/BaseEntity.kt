package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

open class BaseEntity : Serializable {
    @get:DynamoDbPartitionKey
    open var id:String?=null
    open var createdTime: LocalDateTime? = LocalDateTime.now()
    var updatedTime: LocalDateTime? = LocalDateTime.now()
    var logicDeleted: Boolean? = false
    var version: Int = 0
}
