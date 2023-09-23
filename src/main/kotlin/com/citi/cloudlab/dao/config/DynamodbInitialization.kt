package com.citi.cloudlab.dao.config

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

/**
 *
 *@author Loki
 *@date 2023/4/24
 */
open class DynamodbInitialization(
    val ddb: DynamoDbEnhancedClient,
    val ddc: DynamoDbClient
) {
    init {
        val tableNames = ddc.listTables().tableNames()
        ClassPathScanningCandidateComponentProvider(false).apply {
            addIncludeFilter(AnnotationTypeFilter(DynamoDbBean::class.java))
            findCandidateComponents("com.citi.cloudlab.dao.model")
                .map { it.beanClassName }
                .filter { !tableNames.contains(it?.split(".")?.last()) }.forEach {
                    crateTable(Class.forName(it))
                }
        }

    }

    private fun crateTable(clazz: Class<*>) {
        try {
            val table = clazz.simpleName
            var tableSchema = TableSchema.fromBean(clazz)
            var metadata = tableSchema.tableMetadata()

            val globalIndices = metadata.indices()
                .filter { it.partitionKey().get().name() != metadata.primaryPartitionKey() }
                .map { index ->
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName(index.name())
                        .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                        .build()
                }
            ddb.table(table, tableSchema).createTable {
                if (globalIndices.isNotEmpty()) {
                    it.globalSecondaryIndices(globalIndices)
                }
            }
            println("Create table $table API call is done")
            ddc.waiter().waitUntilTableExists { it.tableName(table) }
            println("Table is *actually* created!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error during creating a DynamoDB table")
        }
    }
}
