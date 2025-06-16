package com.citi.cloudlab.common.log

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 *
 *@author Loki
 *@date 2023/5/12
 */
class BasicDigestLogInterceptor(
    loggerName: String? = null,
    private val printArguments: Boolean? = false,
    private val printResults: Boolean? = false
) : MethodInterceptor {
    private val logger = LoggerFactory.getLogger(loggerName ?: javaClass.simpleName)

    /**
     * format：
     *
     * [(simpleName.methodName,result,consuming time in ms)(arguments)(results)]
     *
     * set printArguments=true will print arguments, by default is false
     *
     */
    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val clazz = method.declaringClass
        var resultValue: Any? = null
        var result = "N"
        val watch = StopWatch.createStarted()
        try {
            resultValue = invocation.proceed()
            result = "Y"
        } finally {
            watch.stop()
            logger.info(
                "[({}.{},{},{}ms)({})({})]", clazz.simpleName, method.name, result,
                watch.time, getArgumentsString(invocation),
                getResultsString(resultValue)
            )
        }

        return resultValue
    }

    /**
     * simply join arguments by string util
     */
    private fun getArgumentsString(invocation: MethodInvocation): String {
        var arguments = "-"
        if (printArguments == true) {
            val args = invocation.arguments.filter { it !is kotlin.coroutines.Continuation<*>}
            arguments = StringUtils.join(args, ",")
        }
        return arguments
    }

    /**
     * simply return result string
     */
    private fun getResultsString(retValue: Any?): Any {
        return if (printResults == true) {
            retValue?.toString() ?: "null"
        } else {
            "-"
        }
    }
}