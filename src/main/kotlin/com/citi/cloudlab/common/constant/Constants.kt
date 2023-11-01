package com.citi.cloudlab.common.constant

/**
 *
 *@author Loki
 *@date 2023/5/9
 */
class Constants {
    companion object {
        const val CATEGORY_INDEX = "category-index"
        const val COMMENT_INDEX = "comment-index"
        const val LIKE_INDEX = "like-index"

        val postListProjection = listOf("id", "previewContent", "title", "categoryCode", "author", "tags", "likes", "createdTime")
    }
}
