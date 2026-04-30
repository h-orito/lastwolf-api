package com.ort.lastwolf.fw.security

import com.ort.lastwolf.fw.filter.LoginFilter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "security")
class LastwolfSecurityConfig(
    private val loginFilter: LoginFilter,
) {
    // CORSを許可するドメイン
    lateinit var corsClientUrls: List<String>

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }.exceptionHandling { ex ->
                ex.authenticationEntryPoint(LastwolfAuthenticationEntryPoint())
                ex.accessDeniedHandler(LastwolfAccessDeniedHandler())
            }.csrf { it.disable() }
            .cors { it.configurationSource(getCorsConfigurationSource()) }
            .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    private fun getCorsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        this.corsClientUrls.forEach { corsConfiguration.addAllowedOrigin(it) }
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL)
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL)
        corsConfiguration.allowCredentials = true
        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", corsConfiguration)
        return corsSource
    }
}
