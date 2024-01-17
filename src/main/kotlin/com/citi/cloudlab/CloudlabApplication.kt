package com.citi.cloudlab

import com.citi.cloudlab.common.log.BasicDigestLogInterceptor
import com.citi.cloudlab.dao.config.DynamodbConfiguration
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono

@SpringBootApplication(scanBasePackages = ["com.citi.cloudlab"])
@Import(DynamodbConfiguration::class)
@EnableWebFluxSecurity
class CloudlabApplication {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * webflux have to use securityWebFilterChain to set up cors
     */
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")

        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        http.cors().configurationSource(source)
        return http.build()
    }

    @Bean
    fun controllerDigestLogger() : WebFilter = WebFilter { exchange, chain ->
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val stopWatch = StopWatch.createStarted()
        val filter = chain.filter(exchange)

        exchange.response.beforeCommit {
            var resp = exchange.response
            logger.info("[({},{},{}ms)({})(h-{})({})]",
                method, request.path,
                stopWatch.time,
                request.queryParams, resp.headers, resp.statusCode)
            Mono.empty()
        }

        filter
    }

    @Bean
    fun daoDigestLogger(): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = "within(com.citi.cloudlab.dao..*)"
        val daoDigestLogger = BasicDigestLogInterceptor("DAO-DIGEST-LOGGER", true)
        return DefaultPointcutAdvisor(pointcut, daoDigestLogger)
    }

    /*@Bean
    fun serviceDigestLogger(): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = "within(com.citi.cloudlab.service..*)"
        val serviceDigestLogger = BasicDigestLogInterceptor("SERVICE-DIGEST-LOGGER", printArguments = true, printResults = true)
        return DefaultPointcutAdvisor(pointcut, serviceDigestLogger)
    }*/
}

fun main(args: Array<String>) {
    runApplication<CloudlabApplication>(*args)
}
