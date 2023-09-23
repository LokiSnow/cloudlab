package com.citi.cloudlab.dao.repository.impl

import com.citi.cloudlab.dao.model.BaseEntity
import com.citi.cloudlab.dao.repository.BaseRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.Instant
import java.util.*

/**
 *
 *@author Loki
 *@date 2023/4/20
 */
open class BaseRepositoryImpl<T: BaseEntity>(
    open val table: Class<T>
) : BaseRepository<T>, InitializingBean {
    protected val logger = LoggerFactory.getLogger(javaClass)
    @Autowired
    protected lateinit var ddc: DynamoDbClient
    @Autowired
    protected lateinit var ddb: DynamoDbEnhancedClient
    protected lateinit var tableSchema: TableSchema<T>
    protected lateinit var mappedTable: DynamoDbTable<T>

    override fun afterPropertiesSet() {
        tableSchema = TableSchema.fromBean(table)
        mappedTable = ddb.table(table.simpleName, tableSchema)
    }

    override suspend fun save(entity: T) = save(entity, false)

    override suspend fun save(entity: T, incremental: Boolean) = mappedTable.putItem(fillId(entity, incremental))

    private suspend fun fillId(entity: T, incremental: Boolean): T {
        entity.apply {
            id = if (incremental)
                loadAtomicId()
            else
                UUID.randomUUID().toString()
        }
        return entity
    }

    override suspend fun save(entities: List<T>, incremental: Boolean) {
        val filled = entities.map { fillId(it, incremental) }.toList() //ensure filling operation finished
        ddb.batchWriteItem { builder ->
            val batch = WriteBatch.builder(table).mappedTableResource(mappedTable)
            filled.forEach {
                batch.addPutItem(it)
            }
            builder.addWriteBatch(batch.build())
        }
    }

    override suspend fun loadAtomicId(): String {
        val now = Instant.now().epochSecond
        val key = "${table.simpleName.lowercase()}_id"
        return try {
            val response = ddc.updateItem {
                it.tableName("AtomicIds")
                    .key(mapOf("timestamp" to AttributeValue.fromN(now.toString())))
                    .updateExpression("ADD $key :step set expiry=:exp")
                    .expressionAttributeValues(mapOf(":step" to AttributeValue.fromN("1"), ":exp" to AttributeValue.fromN(now.plus(10).toString())))
                    .returnValues("UPDATED_NEW")
            }
            val id = response.attributes()?.get(key)?.n() ?: "0"
            "${now}${id.padStart(6, '0')}"
        } catch (e :Exception) {
            logger.error("caught exception while load incremental id - ${e.message}")
            UUID.randomUUID().toString()
        }
    }
    override suspend fun delete(id: String): T = mappedTable.deleteItem(Key.builder().partitionValue(id).build())

    override suspend fun update(entity: T): T = mappedTable.updateItem(entity.apply { version++ })

    override suspend fun findById(id: String) :T = mappedTable.getItem(Key.builder().partitionValue(id).build())

    override suspend fun findByIds(ids: List<String>): List<T> {
        return ddb.batchGetItem {builder ->
            val readBatch = ReadBatch.builder(table).mappedTableResource(mappedTable)
            ids.forEach { readBatch.addGetItem(Key.builder().partitionValue(it).build()) }
            builder.addReadBatch(readBatch.build())
        }.resultsForTable(mappedTable).toList().sortedBy{ it.id }
    }

    override suspend fun findAll(lastEvaluatedKey: String?) : List<T> = mappedTable.scan { r ->
        r.limit(20)
            .consistentRead(true)
        getListProjection()?.let {
            r.attributesToProject(it)
        }
        lastEvaluatedKey?.let {
            r.exclusiveStartKey(mapOf("id" to AttributeValue.fromS(lastEvaluatedKey)))
        }
    }.first().items()

    protected open fun getListProjection(): List<String>? = null

}
