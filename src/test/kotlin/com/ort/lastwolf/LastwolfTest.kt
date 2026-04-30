package com.ort.lastwolf

import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.LastwolfUserInfoUtil
import com.ort.lastwolf.fw.config.FirebaseConfig
import com.ort.lastwolf.fw.security.LastwolfUser
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import org.dbflute.hook.AccessContext
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean

open class LastwolfTest {
    @MockBean
    lateinit var firebaseConfig: FirebaseConfig

    @MockBean
    lateinit var firebaseDataSource: FirebaseDataSource

    @BeforeEach
    fun setUp() {
        whenever(firebaseConfig.init()).then { }
        whenever(firebaseConfig.firebaseDatabase()).thenReturn(null)
        whenever(firebaseDataSource.registerMessageLatest(any(), any(), any())).then { }
        whenever(firebaseDataSource.registerVillageLatest(any())).then { }

        setAccessContext()
    }

    private fun setAccessContext() {
        if (AccessContext.isExistAccessContextOnThread()) {
            return
        }
        val accessLocalDateTime = LastwolfDateUtil.currentLocalDateTime()
        val userInfo: LastwolfUser? = LastwolfUserInfoUtil.getUserInfo()
        val accessUser = userInfo?.username ?: "not login user"

        val context = AccessContext()
        context.accessLocalDateTime = accessLocalDateTime
        context.accessUser = accessUser
        AccessContext.setAccessContextOnThread(context)
    }
}
