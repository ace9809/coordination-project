package com.example.musinsa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class MusinsaApplication

fun main(args: Array<String>) {
	runApplication<MusinsaApplication>(*args)
}
