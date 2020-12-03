package com.ort.lastwolf.domain.model.winlose

import com.ort.dbflute.allcommon.CDef

data class WinLose(
    val code: String,
    val name: String
) {
    constructor(
        cdef: CDef.WinLose
    ) : this(
        code = cdef.code(),
        name = cdef.alias()
    )

    fun toCdef(): CDef.WinLose = CDef.WinLose.codeOf(code)
}