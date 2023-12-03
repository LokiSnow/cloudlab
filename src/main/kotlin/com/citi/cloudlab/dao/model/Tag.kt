package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

/**
 * gather tags separately and count post of tag for UI display
 * id - lower case description, and replace space to '-'
 */
@DynamoDbBean
data class Tag(
    var name: String? = null,
    var postCount: Long? = 0
) : BaseEntity()

fun String.tagId() = this.lowercase().replace(' ', '-')
