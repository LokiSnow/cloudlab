package com.citi.cloudlab.dao.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
data class UserProfile (
    var userId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var bio: String? = null,
    var profilePic: String? = null
) : BaseEntity()
