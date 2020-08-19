package com.ort.lastwolf.fw.interceptor

import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.LastwolfUserInfoUtil
import com.ort.lastwolf.fw.security.LastwolfUser
import org.dbflute.hook.AccessContext
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class LastwolfAccessContextInterceptor : HandlerInterceptorAdapter() {

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // [アクセス日時]
        val accessLocalDateTime = LastwolfDateUtil.currentLocalDateTime()

        // [アクセスユーザ]
        val userInfo: LastwolfUser? = LastwolfUserInfoUtil.getUserInfo()
        val accessUser = userInfo?.username ?: "not_login_user"
        val ipAddress = request.remoteAddr

        val context = AccessContext()
        context.accessLocalDateTime = accessLocalDateTime
        context.accessUser = "$accessUser: $ipAddress"
        AccessContext.setAccessContextOnThread(context)

        // Handlerメソッドを呼び出す場合はtrueを返却する
        return true
    }
}