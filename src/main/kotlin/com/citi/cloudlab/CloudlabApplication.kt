package com.citi.cloudlab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CloudlabApplication

fun main(args: Array<String>) {
    runApplication<CloudlabApplication>(*args)
}
