package com.ort.lastwolf.fw.interceptor

import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.LastwolfUserInfoUtil
import com.ort.lastwolf.fw.security.LastwolfUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.dbflute.hook.AccessContext
import org.springframework.web.servlet.HandlerInterceptor

class LastwolfAccessContextInterceptor : HandlerInterceptor {

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessLocalDateTime = LastwolfDateUtil.currentLocalDateTime()
        val userInfo: LastwolfUser? = LastwolfUserInfoUtil.getUserInfo()
        val accessUser = userInfo?.username ?: "not_login_user"
        val ipAddress = request.remoteAddr

        val context = AccessContext()
        context.accessLocalDateTime = accessLocalDateTime
        context.accessUser = "$accessUser: $ipAddress"
        AccessContext.setAccessContextOnThread(context)

        return true
    }
}
