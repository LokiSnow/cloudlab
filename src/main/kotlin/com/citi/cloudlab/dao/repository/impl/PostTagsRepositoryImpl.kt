package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.dao.model.Post
import com.citi.cloudlab.dao.model.PostTags
import com.citi.cloudlab.dao.model.tagId
import com.citi.cloudlab.dao.repository.PostRepository
import com.citi.cloudlab.dao.repository.PostTagsRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

/**
 *
 *@author Loki
 *@date 2023/5/10
 */
@Component
class PostTagsRepositoryImpl(
    private val postRepository: PostRepository
) : PostTagsRepository, InitializingBean{
    @Autowired
    private lateinit var ddc: DynamoDbClient
    @Autowired
    private lateinit var ddb: DynamoDbEnhancedClient
    private lateinit var tableSchema: TableSchema<PostTags>
    private lateinit var mappedTable: DynamoDbTable<PostTags>

    override fun afterPropertiesSet() {
        tableSchema = TableSchema.fromBean(PostTags::class.java)
        mappedTable = ddb.table(PostTags::class.java.simpleName, tableSchema)
    }

    override suspend fun save(postId: String, tagId: String) {
        mappedTable.putItem(PostTags(tagId, postId))
    }
    override suspend fun save(postId: String, tags: List<String>?){
        if (tags.isNullOrEmpty()) return

        ddb.batchWriteItem { builder ->
            val batch = WriteBatch.builder(PostTags::class.java).mappedTableResource(mappedTable)
            tags.forEach { batch.addPutItem(PostTags(it.tagId(), postId)) }
            builder.addWriteBatch(batch.build())
        }
    }

    override suspend fun findPosts(tagId: String, lastPostId: String?) : List<Post> {
        val items = mappedTable.query { builder ->
            builder.limit(20)
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(tagId).build()))
                .scanIndexForward(false)
            lastPostId?.let {
                builder.exclusiveStartKey(
                    mapOf("tagId" to AttributeValue.fromS(tagId),
                        "postId" to AttributeValue.fromS(lastPostId))
                )
            }
        }.first().items().mapNotNull { it.postId }
        return postRepository.findByIds(items)
    }
}