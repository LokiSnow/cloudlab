package com.citi.cloudlab.dao.model

import com.citi.cloudlab.common.constant.Constants.Companion.CATEGORY_INDEX
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey

@DynamoDbBean
data class Post(
    var content: String? = null,
    var previewContent: String? = null,
    var title: String? = null,
    /** category code **/
    @get:DynamoDbSecondaryPartitionKey(indexNames = [ CATEGORY_INDEX ])
    var categoryCode: String? = null,
    var author: String? = null,
    /** actual tag name **/
    var tags: List<String>? = null,
    var views: Int = 0,
    var likes: Int = 0,
    var comments: Int = 0
) : BaseEntity() {
    @get:DynamoDbPartitionKey
    @get:DynamoDbSecondarySortKey(indexNames = [ CATEGORY_INDEX ])
    override var id:String? = null
}
