package com.citi.cloudlab
/*

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class StreamLambdaHandler : RequestStreamHandler {
    companion object {
        private var handler: SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse>? = null
            get() {
                if (field == null) {
                    try {
                        field = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(CloudlabApplication::class.java, "dev")
                    } catch (e: ContainerInitializationException) {
                        // if we fail here. We re-throw the exception to force another cold start
                        e.printStackTrace();
                        throw RuntimeException("Could not initialize Spring Boot application", e)
                    }
                }
                return field
            }
    }

    fun StreamLambdaHandler() {
        // we enable the timer for debugging. This SHOULD NOT be enabled in production.
        //Timer.enable()
    }

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream?, outputStream: OutputStream?, context: Context?) {
        handler!!.proxyStream(inputStream, outputStream, context)
    }
}*/
