package com.ort.lastwolf

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.LastwolfUserInfoUtil
import com.ort.lastwolf.fw.config.FirebaseConfig
import com.ort.lastwolf.fw.security.LastwolfUser
import com.ort.lastwolf.infrastructure.datasource.firebase.MessageLatestDatetimeDataSource
import org.dbflute.hook.AccessContext
import org.junit.Before
import org.springframework.boot.test.mock.mockito.MockBean


open class LastwolfTest {

    @MockBean
    lateinit var firebaseConfig: FirebaseConfig
    @MockBean
    lateinit var messageLatestDatetimeDataSource: MessageLatestDatetimeDataSource

    @Before
    fun setUp() {
        // firebase関連はmockにする
        whenever(firebaseConfig.init()).then { }
        whenever(firebaseConfig.firebaseDatabase()).thenReturn(null)
        whenever(messageLatestDatetimeDataSource.register(any(), any(), any())).then { }

        // set access context
        setAccessContext()
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun setAccessContext() {
        if (AccessContext.isExistAccessContextOnThread()) {
            // 既に設定されていたら何もしないで次へ
            // (二度呼び出しされたときのために念のため)
            return
        }
        // [アクセス日時]
        val accessLocalDateTime = LastwolfDateUtil.currentLocalDateTime()

        // [アクセスユーザ]
        val userInfo: LastwolfUser? = LastwolfUserInfoUtil.getUserInfo()
        val accessUser = userInfo?.username ?: "not login user"

        val context = AccessContext()
        context.accessLocalDateTime = accessLocalDateTime
        context.accessUser = accessUser
        AccessContext.setAccessContextOnThread(context)
    }
}