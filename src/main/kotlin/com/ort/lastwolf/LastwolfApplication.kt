package com.ort.lastwolf

import com.ort.dbflute.allcommon.DBFluteBeansJavaConfig
import com.ort.lastwolf.fw.config.FirebaseConfig
import com.ort.lastwolf.fw.config.LastwolfAppConfig
import com.ort.lastwolf.fw.config.LastwolfWebMvcConfigurer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
    DBFluteBeansJavaConfig::class,
    LastwolfAppConfig::class,
    LastwolfWebMvcConfigurer::class,
    FirebaseConfig::class
)
class LastwolfApplication

fun main(args: Array<String>) {
    runApplication<LastwolfApplication>(*args)
}