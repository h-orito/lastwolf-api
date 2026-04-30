package com.ort.lastwolf.fw.filter

import com.google.firebase.auth.FirebaseAuth
import com.ort.lastwolf.fw.security.LastwolfUserDetailService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class LoginFilter(
    val userService: LastwolfUserDetailService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        SecurityContextHolder.getContext().authentication =
            PreAuthenticatedAuthenticationToken(
                auth(request),
                null,
            )
        filterChain.doFilter(request, response)
    }

    private fun auth(request: HttpServletRequest): UserDetails? {
        val token: String? = getToken(request)
        token ?: return null

        try {
            val uid: String? = FirebaseAuth.getInstance().verifyIdToken(token)?.uid
            uid ?: throw BadCredentialsException("改竄リクエストまたはトークン有効期限切れです")

            return try {
                userService.loadUserByUsername(uid)
            } catch (e: UsernameNotFoundException) {
                userService.insertUser(uid)
            }
        } catch (e: Exception) {
            throw BadCredentialsException(e.message, e)
        }
    }

    private fun getToken(request: HttpServletRequest): String? {
        val token: String? = request.getHeader("Authorization")
        if (token == null || !token.startsWith("Bearer ")) {
            return null
        }
        return token.substring("Bearer ".length)
    }
}
