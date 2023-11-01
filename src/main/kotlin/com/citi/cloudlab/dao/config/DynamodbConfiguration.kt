package com.citi.cloudlab.dao.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

/**
 *
 *@author Loki
 *@date 2023/4/20
 */
@Configuration
@ComponentScan(value = ["com.citi.cloudlab.dao"])
open class DynamodbConfiguration {
	@Bean
	open fun dynamoDbClient(): DynamoDbClient? {
		return DynamoDbClient.builder().build()
	}

	@Bean
	open fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build()
	}

	@Bean
	open fun dynamodbInitialization(dynamoDbEnhancedClient: DynamoDbEnhancedClient, dynamoDbClient: DynamoDbClient) : DynamodbInitialization {
		return DynamodbInitialization(dynamoDbEnhancedClient, dynamoDbClient)
	}
}