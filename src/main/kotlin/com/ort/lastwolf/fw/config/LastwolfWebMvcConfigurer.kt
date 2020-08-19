package com.ort.lastwolf.fw.config

import com.ort.lastwolf.fw.interceptor.LastwolfAccessContextInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class LastwolfWebMvcConfigurer : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry!!.addInterceptor(LastwolfAccessContextInterceptor()).addPathPatterns("/**")
    }
}