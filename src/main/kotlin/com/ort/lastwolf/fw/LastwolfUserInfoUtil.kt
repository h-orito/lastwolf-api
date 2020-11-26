package com.ort.lastwolf.fw

import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.security.core.context.SecurityContextHolder


class LastwolfUserInfoUtil private constructor() {
    companion object {
        fun getUserInfo(): LastwolfUser? {
            val authentication = SecurityContextHolder.getContext().authentication ?: return null
            return if (authentication.principal is LastwolfUser) {
                LastwolfUser::class.java.cast(authentication.principal)
            } else null
        }
    }
}